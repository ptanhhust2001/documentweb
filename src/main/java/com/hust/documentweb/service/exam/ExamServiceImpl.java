package com.hust.documentweb.service.exam;

import com.graphbuilder.math.func.EFunction;
import com.hust.documentweb.constant.CommonConstrant;
import com.hust.documentweb.constant.ErrorCommon;
import com.hust.documentweb.constant.FunctionError;
import com.hust.documentweb.dto.ResponsePageDTO;
import com.hust.documentweb.dto.exam.*;
import com.hust.documentweb.dto.question.QuestionResDTO;
import com.hust.documentweb.entity.*;
import com.hust.documentweb.exception.BookException;
import com.hust.documentweb.repository.*;
import com.hust.documentweb.service.openai.GeminiService;
import com.hust.documentweb.utils.spec.BaseSpecs;
import com.hust.documentweb.utils.spec.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamServiceImpl implements IExamService {
    ExamRepository repository;
    ModelMapper examMapper;
    ClassEntityRepository classEntityRepository;
    UserRepository userRepository;
    SubjectRepository subjectRepository;
    QuestionRepository questionRepository;
    GeminiService geminiService;

    ModelMapper questionMapper;

    @Override
    public ResponsePageDTO<List<ExamResDTO>> findAll(String advanceSearch, Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("updateAt").descending());
        Page<Exam> datas = repository.findAll(BaseSpecs.searchQuery(advanceSearch), pageable);
        return ResponsePageDTO.success(Utils.mapList(examMapper, datas.getContent(), ExamResDTO.class), datas.getTotalElements());
    }

    @Override
    public ExamExResDTO findById(Long id) {
        Exam data = repository.findById(id).orElseThrow(
                () -> new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.EXAM_NOT_FOUND, List.of(id))));
        List<QuestionResDTO> questions = data.getQuestions().stream().map(question -> questionMapper.map(question, QuestionResDTO.class)).toList();
        ExamExResDTO resDTO = examMapper.map(data, ExamExResDTO.class);
        resDTO.setQuestions(questions);
        return resDTO;
    }

    @Override
    public ExamResResultDTO checkAnswer(ExamReqResultDTO answers) {

        final Integer[] totalAns = {0};
        final Integer[] totalAnsCorrect = {0};
        List<Long> answersInCorrect = new ArrayList<>();
        List<Long> answersCorrect = new ArrayList<>();
        ExamResResultDTO resDTO = new ExamResResultDTO();
        HashMap<Object, List<Object>> errorMap = new HashMap<>();
        answers.getAnswers().forEach(answer -> {
            questionRepository.findById(answer.getId()).ifPresentOrElse( question -> {
                totalAns[0]++;
                if (question.getCorrectAnswer().equals(answer.getAnswer().trim().toUpperCase())) {
                    totalAnsCorrect[0]++;
                    answersCorrect.add(question.getId());
                } else {
                    answersInCorrect.add(question.getId());
                }
                    },
                    () -> errorMap.computeIfAbsent(ErrorCommon.QUESTION_NOT_FOUND, k -> new ArrayList<>()).add(answer.getId()));
        });
        if (!errorMap.isEmpty()) throw new BookException(ErrorCommon.QUESTION_NOT_FOUND, errorMap);

        resDTO.setTotalAns(totalAns[0]);
        resDTO.setTotalAnsCorrect(totalAnsCorrect[0]);
        resDTO.setAnswersInCorrect(answersInCorrect);
        resDTO.setAnswersCorrect(answersCorrect);
        return resDTO;
    }

    @Override
    public ExamResDTO create(ExamReqDTO dto) {
        Map<Object, Object> errorMap = new HashMap<>();
        Exam data = validation(dto, errorMap);

        save(data, true);

        return examMapper.map(data, ExamResDTO.class);
    }

    @Override
    public ExamResDTO update(Long id, ExamUpdateDTO dto) {
        Exam data = repository.findById(id).orElseThrow(
                () -> new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.EXAM_NOT_FOUND, List.of(id))));
        examMapper.map(dto, data);

        save(data, false);
        return examMapper.map(data, ExamResDTO.class);
    }

    @Override
    public void deleteAllById(List<Long> ids) {
        List<Long> notFound = ids.stream().filter(id -> repository.findById(id).isEmpty()).toList();
        if (!notFound.isEmpty()) throw new BookException(FunctionError.DELETE_FAILED, Map.of(ErrorCommon.EXAM_NOT_FOUND, notFound));
        repository.deleteAllById(ids);
    }

    private Exam save(Exam data, boolean isCreate) {
        if (isCreate) {
            data.setCreateBy(Utils.getCurrentUser());
            data.setCreateAt(LocalDateTime.now());
        }
        data.setUpdateBy(Utils.getCurrentUser());
        data.setUpdateAt(LocalDateTime.now());
        return repository.save(data);
    }

    @Override
    public void upload(MultipartFile file, Long classId, Long subjectId) throws IOException {
        Queue<List<String>> questionList = readQuestion(file);
        Exam data = new Exam();
        Map<Object, Object> errorMap = new HashMap<Object, Object>();
        String name = "";
        if (!questionList.peek().isEmpty()) {
            name = questionList.poll().get(0);
        }
        if (name == null) errorMap.put(ErrorCommon.TITLE_NOT_NULL,List.of(""));
        Optional<ClassEntity> classEntity = classEntityRepository.findById(classId);
        Optional<Subject> subject = subjectRepository.findById(subjectId);
        if (classEntity.isEmpty()) errorMap.put(ErrorCommon.CLASS_DOES_NOT_EXIST, List.of(classId));
        if (subject.isEmpty()) errorMap.put(ErrorCommon.SUBJECT_DOES_NOT_EXIST, List.of(subjectId));
        if (!errorMap.isEmpty()) throw new BookException(FunctionError.CREATE_FAILED, errorMap);
        data.setName(name);
        data.setClassEntity(classEntity.get());
        data.setSubject(subject.get());
        data.setUser(userRepository.findByUsername(Utils.getCurrentUser()).get());

        List<Question> questions = readData(questionList);

        data = save(data, true);
        for (Question question : questions) {
            question.setExam(data);
        }
        questionRepository.saveAll(questions);
    }

    @Override
    public ExamResDTO createQuestionByOpenAi(ExamReqOpenAiDTO dto) throws JsonProcessingException {
        ExamResDTO resDTO = create(examMapper.map(dto, ExamReqDTO.class));
        Exam data = repository.findById(resDTO.getId()).orElse(null);
        String str = geminiService.generateContent(CommonConstrant.FORMAT + dto.getContent());
        List<Question> questions = stringToQuestions(str, data);
        questionRepository.saveAll(questions);
        return resDTO;
    }

    private List<Question> stringToQuestions(String input,Exam exam) {
        // Tách câu hỏi từ chuỗi input
        String[] questionsArray = input.split("\\*\\*Câu \\d+:\\*\\*");
        List<Question> questionsList = new ArrayList<>();

        // Bỏ qua phần đầu (tiêu đề) vì không có câu hỏi
        for (int i = 1; i < questionsArray.length; i++) {
            String questionBlock = questionsArray[i].trim();
            String[] parts = questionBlock.split("\n");

            // Câu hỏi
            String questionText = parts[0].trim();

            // Đáp án
            String firstAnswer = parts[1].replaceAll("^[\\*]?\\s?a\\) ", "").trim();
            String secondAnswer = parts[2].replaceAll("^[\\*]?\\s?b\\) ", "").trim();
            String thirdAnswer = parts[3].replaceAll("^[\\*]?\\s?c\\) ", "").trim();
            String fourthAnswer = parts[4].replaceAll("^[\\*]?\\s?d\\) ", "").trim();

            // Xác định đáp án đúng
            String correctAnswer = null;
            if (parts[1].startsWith("*")) correctAnswer = "A";
            else if (parts[2].startsWith("*")) correctAnswer = "B";
            else if (parts[3].startsWith("*")) correctAnswer = "C";
            else if (parts[4].startsWith("*")) correctAnswer = "D";

            // Tạo đối tượng Question và thêm vào danh sách
            questionsList.add(new Question(questionText, firstAnswer, secondAnswer, thirdAnswer, fourthAnswer, correctAnswer, exam));
        }
        return questionsList;
    }

    private List<Question> readData(Queue<List<String>> queue) {
        List<Question> listQuestion = new ArrayList<Question>();
        while (!queue.isEmpty()) {
            List<String> data = queue.poll();
            if (data.size() >= 4) {
              Question question = new Question();
              question.setQuestion(data.get(0));
              if (data.get(1).charAt(0) == '*') {
                  question.setCorrectAnswer("A");
                  question.setFirstAnswer(data.get(1).substring(1));
              } else {
                  question.setFirstAnswer(data.get(1));
              }
              if (data.get(2).charAt(0) == '*') {
                  question.setCorrectAnswer("B");
                  question.setSecondAnswer(data.get(2).substring(1));
              } else {
                  question.setSecondAnswer(data.get(2));
              }
              if (data.get(3).charAt(0) == '*') {
                  question.setCorrectAnswer("C");
                  question.setThirdAnswer(data.get(3).substring(1));
              } else {
                  question.setThirdAnswer(data.get(3));
              }
              if (data.size() >= 5 && data.get(4).charAt(0) == '*') {
                  question.setCorrectAnswer("D");
                  question.setFourthAnswer(data.get(4).substring(1));
              } else {
                  question.setFourthAnswer(data.get(4));
              }
              if (question.getCorrectAnswer() != null) {
                listQuestion.add(question);
              }
            }
        }
        return listQuestion;
    }

    private Queue<List<String>> readQuestion(MultipartFile file) {
        if (!file.getOriginalFilename().endsWith(".docx")) {
            throw new BookException(FunctionError.UPLOAD_FAILED, ErrorCommon.FILE_INVALID_FORMAT);
        }

        // Đọc nội dung file Word
        try (InputStream inputStream = file.getInputStream()) {
            XWPFDocument document = new XWPFDocument(inputStream);

            Queue<List<String>> questionList = new ArrayDeque<>();
            List<String> question = new ArrayList<>();
            document.getParagraphs().forEach(paragraph -> {
                if (paragraph.getText().isBlank()) {
                    questionList.add(new ArrayList<>(question));
                    question.clear();
                } else {
                    question.add(paragraph.getText());
                }
            });
            questionList.add(new ArrayList<>(question));
            return questionList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private Exam validation(ExamReqDTO dto, Map<Object, Object> errorMap) {
        Exam data = examMapper.map(dto, Exam.class);
        Optional<ClassEntity> classOpt = classEntityRepository.findById(dto.getClassEntityId());
        Optional<Subject> subjectOpt = subjectRepository.findById(dto.getSubjectId());
        Optional<User> userOpt = userRepository.findById(dto.getUserId());

        if (classOpt.isEmpty()) errorMap.put(ErrorCommon.CLASS_DOES_NOT_EXIST, List.of(dto.getClassEntityId()));
        if (subjectOpt.isEmpty()) errorMap.put(ErrorCommon.SUBJECT_DOES_NOT_EXIST, List.of(dto.getSubjectId()));
        if (userOpt.isEmpty()) errorMap.put(ErrorCommon.USER_DOES_NOT_EXIST, List.of(dto.getUserId()));

        if (!errorMap.isEmpty()) throw new BookException(FunctionError.CREATE_FAILED, errorMap);

        data.setClassEntity(classOpt.get()  );
        data.setSubject(subjectOpt.get());
        data.setUser(userOpt.get());
        return data;
    }

}

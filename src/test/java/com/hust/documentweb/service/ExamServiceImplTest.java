package com.hust.documentweb.service;

import com.hust.documentweb.dto.exam.ExamReqDTO;
import com.hust.documentweb.dto.exam.ExamResDTO;
import com.hust.documentweb.dto.exam.ExamUpdateDTO;
import com.hust.documentweb.entity.ClassEntity;
import com.hust.documentweb.entity.Exam;
import com.hust.documentweb.entity.Subject;
import com.hust.documentweb.entity.User;
import com.hust.documentweb.exception.BookException;
import com.hust.documentweb.repository.ExamRepository;
import com.hust.documentweb.repository.ClassEntityRepository;
import com.hust.documentweb.repository.SubjectRepository;
import com.hust.documentweb.repository.UserRepository;
import com.hust.documentweb.repository.QuestionRepository;
import com.hust.documentweb.service.exam.ExamServiceImpl;
import com.hust.documentweb.service.openai.GeminiService;
import com.hust.documentweb.utils.spec.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ExamServiceImplTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private ClassEntityRepository classEntityRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private GeminiService geminiService;

    @Mock
    private Utils utils;

    @InjectMocks
    private ExamServiceImpl examService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_ExamExists() {
        // GIVEN
        Long examId = 1L;
        Exam exam = new Exam();
        exam.setId(examId);

        // Mock repository trả về Exam
        when(examRepository.findById(examId)).thenReturn(Optional.of(exam));

        // WHEN
        ExamResDTO result = examService.findById(examId);

        // THEN
        assertNotNull(result);
        assertEquals(examId, result.getId());
    }

    @Test
    void testFindById_ExamNotFound() {
        Long examId = 1L;

        when(examRepository.findById(examId)).thenReturn(Optional.empty());

        BookException exception = assertThrows(BookException.class, () -> examService.findById(examId));
        assertEquals("EXAM_NOT_FOUND", exception.getErrorCode());
    }

    @Test
    void testCreate_ValidExamReqDTO() {
        ExamReqDTO examReqDTO = new ExamReqDTO();
        examReqDTO.setClassEntityId(1L);
        examReqDTO.setSubjectId(1L);
        examReqDTO.setUserId(1L);

        // Mocks
        when(classEntityRepository.findById(1L)).thenReturn(Optional.of(new ClassEntity()));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(new Subject()));
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(examRepository.save(any(Exam.class))).thenReturn(new Exam());

        ExamResDTO result = examService.create(examReqDTO);

        assertNotNull(result);
        verify(examRepository, times(1)).save(any(Exam.class));
    }

//    @Test
//    void testCreate_ValidationFailure() {
//        ExamReqDTO examReqDTO = new ExamReqDTO();
//        examReqDTO.setClassEntityId(999L);  // Invalid classId
//        examReqDTO.setSubjectId(1L);
//        examReqDTO.setUserId(1L);
//
//        when(classEntityRepository.findById(999L)).thenReturn(Optional.empty());
//
//        BookException exception = assertThrows(BookException.class, () -> examService.create(examReqDTO));
//        assertEquals("CREATE_FAILED", exception.getErrorCode());
//        assertTrue(exception.getErrorDetails().containsKey("CLASS_DOES_NOT_EXIST"));
//    }

    @Test
    void testUpdate_ExamExists() {
        Long examId = 1L;
        ExamUpdateDTO examUpdateDTO = new ExamUpdateDTO();
        examUpdateDTO.setName("Updated Exam");

        Exam existingExam = new Exam();
        existingExam.setId(examId);

        when(examRepository.findById(examId)).thenReturn(Optional.of(existingExam));
        when(examRepository.save(any(Exam.class))).thenReturn(existingExam);

        ExamResDTO result = examService.update(examId, examUpdateDTO);

assertNotNull(result);
assertEquals("Updated Exam", result.getName());
        }

@Test
void testUpdate_ExamNotFound() {
    Long examId = 1L;
    ExamUpdateDTO examUpdateDTO = new ExamUpdateDTO();

    when(examRepository.findById(examId)).thenReturn(Optional.empty());

    BookException exception = assertThrows(BookException.class, () -> examService.update(examId, examUpdateDTO));
    assertEquals("NOT_FOUND", exception.getErrorCode());
}

@Test
void testDeleteAllById_Success() {
    Long examId = 1L;
    when(examRepository.findById(examId)).thenReturn(Optional.of(new Exam()));

    examService.deleteAllById(List.of(examId));

    verify(examRepository, times(1)).deleteAllById(List.of(examId));
}

//    @Test
//    void testDeleteAllById_ExamNotFound() {
//        Long examId = 1L;
//        when(examRepository.findById(examId)).thenReturn(Optional.empty());
//
//        BookException exception = assertThrows(BookException.class, () -> examService.deleteAllById(List.of(examId)));
//        assertEquals("DELETE_FAILED", exception.getErrorCode());
//        assertTrue(exception.getErrorDetails().containsKey("EXAM_NOT_FOUND"));
//    }

// Other tests for upload and createQuestionByOpenAi can be written similarly...
}

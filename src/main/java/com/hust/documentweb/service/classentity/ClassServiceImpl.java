package com.hust.documentweb.service.classentity;

import java.util.*;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.hust.documentweb.constant.ErrorCommon;
import com.hust.documentweb.constant.FunctionError;
import com.hust.documentweb.dto.classenity.ClassReqDTO;
import com.hust.documentweb.dto.classenity.ClassResDTO;
import com.hust.documentweb.dto.subject.SubjectResDTO;
import com.hust.documentweb.entity.ClassEntity;
import com.hust.documentweb.entity.Subject;
import com.hust.documentweb.exception.BookException;
import com.hust.documentweb.repository.ClassEntityRepository;
import com.hust.documentweb.repository.SubjectRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassServiceImpl implements IClassService {
    ClassEntityRepository repository;
    SubjectRepository subjectRepository;
    ModelMapper classMapper;
    ModelMapper mapper;

    @Override
    public ClassResDTO create(ClassReqDTO dto) {
        ClassEntity data = classMapper.map(dto, ClassEntity.class);
        Map<Object, Object> errorMap = new HashMap<>();

        List<Subject> subjects = getAllSubjectByName(dto.getSubjects(), errorMap);

        if (repository.findByName(dto.getName()).isPresent())
            errorMap.put(ErrorCommon.CLASS_ALREADY_EXISTS, List.of(data.getName()));
        if (!errorMap.isEmpty()) throw new BookException(FunctionError.CREATE_FAILED, errorMap);
        data.setSubjects(subjects);
        repository.save(data);

        return classMapper.map(data, ClassResDTO.class);
    }

    @Override
    public List<ClassResDTO> findAll() {
        return repository.findAll().stream()
                .map(data -> classMapper.map(data, ClassResDTO.class))
                .map(data -> {
                    List<SubjectResDTO> subjectResDTOs = data.getSubjects().stream()
                            .map(subject -> mapper.map(subject, SubjectResDTO.class))
                            .toList();
                    data.setSubjects(subjectResDTOs);
                    return data;
                })
                .toList();
    }

    @Override
    public ClassResDTO findById(Long id) {
        Optional<ClassEntity> data = repository.findById(id);
        if (data.isEmpty()) throw new BookException(FunctionError.NOT_FOUND, List.of(id));
        return classMapper.map(data.get(), ClassResDTO.class);
    }

    @Override
    public ClassResDTO update(Long id, ClassReqDTO dto) {
        ClassEntity data =
                repository.findById(id).orElseThrow(() -> new BookException(FunctionError.NOT_FOUND, List.of(id)));

        classMapper.map(dto, data);
        Map<Object, Object> errorMap = new HashMap<>();

        List<Subject> subjects = getAllSubjectByName(dto.getSubjects(), errorMap);
        if (!errorMap.isEmpty()) throw new BookException(FunctionError.UPDATE_FAILED, errorMap);
        data.setSubjects(subjects);
        repository.save(data);
        return classMapper.map(data, ClassResDTO.class);
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {
        Map<Object, Object> errorMap = new HashMap<>();
        List<Long> notFound =
                ids.stream().filter(id -> repository.findById(id).isEmpty()).toList();
        if (!notFound.isEmpty()) errorMap.put(ErrorCommon.CLASS_NOT_FOUND, notFound);
        if (!errorMap.isEmpty()) throw new BookException(FunctionError.DELETE_FAILED, errorMap);

        repository.deleteAllById(ids);
    }

    private List<Subject> getAllSubjectByName(List<String> names, Map<Object, Object> errorMap) {
        List<Subject> subjects = new ArrayList<>();
        for (String str : names) {
            Optional<Subject> subject = subjectRepository.findByName(str);
            if (subject.isEmpty())
                errorMap.computeIfAbsent(ErrorCommon.SUBJECT_NOT_FOUND, k -> new ArrayList<>().add(str));
            else subjects.add(subject.get());
        }
        return subjects;
    }
}

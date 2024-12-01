package com.hust.documentweb.service.subject;

import com.hust.documentweb.constant.ErrorCommon;
import com.hust.documentweb.constant.FunctionError;
import com.hust.documentweb.dto.subject.SubjectReqDTO;
import com.hust.documentweb.dto.subject.SubjectResDTO;
import com.hust.documentweb.entity.Subject;
import com.hust.documentweb.exception.BookException;
import com.hust.documentweb.repository.SubjectRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectServiceImpl implements ISubjectService{
    SubjectRepository repository;
    ModelMapper mapper;


    //FAILED
    @Override
    public List<SubjectResDTO> findAll() {
        return repository.findAll().stream().map(subject -> mapper.map(subject, SubjectResDTO.class)).toList();
    }

    @Override
    public SubjectResDTO findById(Long id) {
        Subject data = repository.findById(id)
                .orElseThrow(()
                        -> new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.SUBJECT_NOT_FOUND, List.of(id))));
        return mapper.map(data, SubjectResDTO.class);
    }

    @Override
    public SubjectResDTO create(SubjectReqDTO dto) {
        Subject data = mapper.map(dto, Subject.class);
        if (repository.findByName(dto.getName()).isPresent()) throw new BookException(FunctionError.CREATE_FAILED, ErrorCommon.SUBJECT_EXISTED);
        repository.save(data);
        return mapper.map(repository.save(data), SubjectResDTO.class);
    }

    @Override
    public SubjectResDTO update(Long id, SubjectReqDTO dto) {
        Subject data = repository.findById(id)
                .orElseThrow(()
                        -> new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.SUBJECT_NOT_FOUND, List.of(id))));
        data =  mapper.map(dto, Subject.class);
        data.setId(id);
        repository.save(data);
        return mapper.map(data, SubjectResDTO.class);
    }
}
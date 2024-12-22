package com.hust.documentweb.service.subject;

import java.util.List;

import com.hust.documentweb.dto.subject.SubjectReqDTO;
import com.hust.documentweb.dto.subject.SubjectResDTO;

public interface ISubjectService {
    // FAILED
    List<SubjectResDTO> findAll();

    SubjectResDTO findById(Long id);

    SubjectResDTO create(SubjectReqDTO dto);

    SubjectResDTO update(Long id, SubjectReqDTO dto);
}

package com.hust.documentweb.service.subject;

import com.hust.documentweb.dto.subject.SubjectReqDTO;
import com.hust.documentweb.dto.subject.SubjectResDTO;

import java.util.List;

public interface ISubjectService {
    //FAILED
    List<SubjectResDTO> findAll();

    SubjectResDTO findById(Long id);

    SubjectResDTO create(SubjectReqDTO dto);

    SubjectResDTO update(Long id, SubjectReqDTO dto);
}

package com.hust.documentweb.service.classentity;

import java.util.List;

import com.hust.documentweb.dto.classenity.ClassReqDTO;
import com.hust.documentweb.dto.classenity.ClassResDTO;

public interface IClassService {
    ClassResDTO create(ClassReqDTO dto);

    List<ClassResDTO> findAll();

    ClassResDTO findById(Long id);

    ClassResDTO update(Long id, ClassReqDTO dto);

    void deleteAllByIds(List<Long> ids);
}

package com.hust.documentweb.service.exam;

import com.hust.documentweb.dto.exam.ExamReqDTO;
import com.hust.documentweb.dto.exam.ExamReqOpenAiDTO;
import com.hust.documentweb.dto.exam.ExamResDTO;
import com.hust.documentweb.dto.exam.ExamUpdateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IExamService {
    List<ExamResDTO> findAll(String advanceSearch);

    ExamResDTO findById(Long id);

    ExamResDTO create(ExamReqDTO dto);

    ExamResDTO update(Long id, ExamUpdateDTO dto);

    void deleteAllById(List<Long> ids);

    void upload(MultipartFile file , Long classId, Long subjectId) throws IOException;

    ExamResDTO createQuestionByOpenAi(ExamReqOpenAiDTO dto) throws JsonProcessingException;
}

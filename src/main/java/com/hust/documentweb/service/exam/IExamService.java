package com.hust.documentweb.service.exam;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hust.documentweb.dto.ResponsePageDTO;
import com.hust.documentweb.dto.exam.*;

public interface IExamService {
    ResponsePageDTO<List<ExamResDTO>> findAll(String advanceSearch, Pageable pageable);

    ExamExResDTO findById(Long id);

    ExamResResultDTO checkAnswer(ExamReqResultDTO answer);

    ExamResDTO create(ExamReqDTO dto);

    ExamResDTO update(Long id, ExamUpdateDTO dto);

    void deleteAllById(List<Long> ids);

    void upload(MultipartFile file, Long classId, Long subjectId) throws IOException;

    ExamResDTO createQuestionByOpenAi(ExamReqOpenAiDTO dto) throws JsonProcessingException;
}

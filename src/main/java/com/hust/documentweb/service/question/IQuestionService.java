package com.hust.documentweb.service.question;

import java.util.List;

import com.hust.documentweb.dto.question.QuestionReqDTO;
import com.hust.documentweb.dto.question.QuestionResDTO;
import com.hust.documentweb.dto.question.QuestionUpdateDTO;

public interface IQuestionService {
    List<QuestionResDTO> findAll(String advanceSearch);

    QuestionResDTO findById(Long id);

    QuestionResDTO create(QuestionReqDTO dto);

    QuestionResDTO update(Long id, QuestionUpdateDTO dto);

    void deleteAllById(List<Long> ids);
}

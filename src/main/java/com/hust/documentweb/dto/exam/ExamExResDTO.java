package com.hust.documentweb.dto.exam;

import com.hust.documentweb.dto.question.QuestionResDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ExamExResDTO{
    Long id;
    String name;
    Long classEntityId;
    Long subjectId;
    Long userId;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    String createBy;
    String updateBy;
    List<QuestionResDTO> questions = new ArrayList<>();
}

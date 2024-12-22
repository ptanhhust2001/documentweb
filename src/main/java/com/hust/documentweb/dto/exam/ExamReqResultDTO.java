package com.hust.documentweb.dto.exam;

import java.util.List;

import lombok.Data;

@Data
public class ExamReqResultDTO {
    List<QuestionAns> answers;
}

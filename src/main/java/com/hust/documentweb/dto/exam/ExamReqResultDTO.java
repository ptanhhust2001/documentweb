package com.hust.documentweb.dto.exam;

import lombok.Data;

import java.util.List;

@Data
public class ExamReqResultDTO {
    List<QuestionAns> answers;
}

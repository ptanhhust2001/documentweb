package com.hust.documentweb.dto.exam;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ExamResResultDTO {
    Integer totalAns;
    Integer totalAnsCorrect;
    List<Long> answersInCorrect = new ArrayList<>();
    List<Long> answersCorrect = new ArrayList<>();
}

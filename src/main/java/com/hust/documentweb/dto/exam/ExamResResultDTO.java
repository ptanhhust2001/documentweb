package com.hust.documentweb.dto.exam;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExamResResultDTO {
    Integer totalAns;
    Integer totalAnsCorrect;
    List<Long> answersInCorrect = new ArrayList<>();
    List<Long> answersCorrect = new ArrayList<>();
}

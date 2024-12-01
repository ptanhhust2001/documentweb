package com.hust.documentweb.dto.question;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionUpdateDTO {
    String question;
    String firstAnswer;
    String secondAnswer;
    String thirdAnswer;
    String fourthAnswer;
    String correctAnswer;
}

package com.hust.documentweb.dto.question;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionReqDTO {
    String question;
    String firstAnswer;
    String secondAnswer;
    String thirdAnswer;
    String fourthAnswer;
    String correctAnswer;

    Long examId;
}

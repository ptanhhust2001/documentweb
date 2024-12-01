package com.hust.documentweb.dto.exam;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamReqOpenAiDTO {
    String content;
    String name;
    Long classEntityId;
    Long subjectId;
    Long userId;
}

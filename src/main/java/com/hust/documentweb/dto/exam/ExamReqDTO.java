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
public class ExamReqDTO {
    String name;
    Long classEntityId;
    String classEntityName;
    Long subjectId;
    String subjectName;
    Long userId;
}

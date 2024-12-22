package com.hust.documentweb.dto.exam;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamResDTO {
    Long id;
    String name;
    Long classEntityId;
    String classEntityName;
    Long subjectId;
    String subjectName;
    Long userId;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    String createBy;
    String updateBy;
}

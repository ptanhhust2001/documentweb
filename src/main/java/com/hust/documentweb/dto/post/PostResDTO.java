package com.hust.documentweb.dto.post;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResDTO {
    Long id;

    String title;
    String content;
    String description;
    String author;
    String imageFilePath;

    Long subjectId;

    Long classEntityId;

    Long userId;

    LocalDateTime createAt;

    LocalDateTime updateAt;

    String createBy;

    String updateBy;
}

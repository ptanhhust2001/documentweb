package com.hust.documentweb.dto.post;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostReqDTO {
    String title;
    String content;
    String description;
    String imageFilePath;
    Long subjectId;
    Long classEntityId;
}

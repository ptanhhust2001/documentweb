package com.hust.documentweb.dto.post;

import java.time.LocalDateTime;
import java.util.List;

import com.hust.documentweb.dto.comment.CommentResDTO;
import com.hust.documentweb.dto.material.MaterialResDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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
    String subjectName;

    Long classEntityId;
    String classEntityName;

    Long userId;

    LocalDateTime createAt;

    LocalDateTime updateAt;

    String createBy;

    String updateBy;

    List<CommentResDTO> comments;

    List<MaterialResDTO> materials;
}

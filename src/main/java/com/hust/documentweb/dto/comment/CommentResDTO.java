package com.hust.documentweb.dto.comment;

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
public class CommentResDTO {
    Long id;

    String content;

    Long postId;

    LocalDateTime createAt;

    LocalDateTime updateAt;

    String createBy;

    String updateBy;
}

package com.hust.documentweb.dto.post;

import com.hust.documentweb.constant.enums.EType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostUpdateDTO {
    String title;
    String content;

    EType type;
    String description;
}

package com.hust.documentweb.dto.post;

import com.hust.documentweb.constant.CommonConstrant;
import com.hust.documentweb.constant.EError;
import com.hust.documentweb.constant.enums.EPostType;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = EError.NOT_NULL_OR_EMPTY)
    String title;

    @NotBlank(message = EError.NOT_NULL_OR_EMPTY)
    String content;
    String description;
    String imageFilePath;

    EPostType type;

    @NotBlank(message = EError.NOT_NULL_OR_EMPTY)
    Long subjectId;
    @NotBlank(message = EError.NOT_NULL_OR_EMPTY)
    Long classEntityId;
}

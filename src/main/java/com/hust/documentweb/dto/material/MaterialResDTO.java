package com.hust.documentweb.dto.material;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaterialResDTO {
    Long id;
    String name;
    String urlFile;
}

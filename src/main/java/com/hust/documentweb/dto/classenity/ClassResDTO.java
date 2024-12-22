package com.hust.documentweb.dto.classenity;

import java.util.List;

import com.hust.documentweb.dto.subject.SubjectResDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassResDTO {

    Long id;

    String name;

    List<SubjectResDTO> subjects;
}

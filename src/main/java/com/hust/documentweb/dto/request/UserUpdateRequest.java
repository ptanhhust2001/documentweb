package com.hust.documentweb.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.hust.documentweb.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    String firstName;
    String lastName;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;

}

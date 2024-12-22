package com.hust.documentweb.dto.User;

import static com.hust.documentweb.constant.EError.FORMAT_INVALID;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateDTO {
    private String username;

    @Size(min = 6, max = 20, message = FORMAT_INVALID)
    private String password;

    private String firstName;
    private String lastName;
    private LocalDate dob;
}

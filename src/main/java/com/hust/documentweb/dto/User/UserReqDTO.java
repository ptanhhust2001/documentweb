package com.hust.documentweb.dto.User;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

import com.hust.documentweb.constant.EError;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserReqDTO {
    private String username;

    @Size(min = 6, max = 20, message = EError.FORMAT_INVALID)
    private String password;

    private String firstName;
    private String lastName;
    private LocalDate dob;
}

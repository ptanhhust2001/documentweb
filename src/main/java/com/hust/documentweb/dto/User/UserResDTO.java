package com.hust.documentweb.dto.User;

import java.time.LocalDate;
import java.util.Set;

import com.hust.documentweb.entity.Role;

import lombok.Data;

@Data
public class UserResDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    Set<Role> roles;
}

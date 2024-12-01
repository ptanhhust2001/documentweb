package com.hust.documentweb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.hust.documentweb.dto.request.UserCreationRequest;
import com.hust.documentweb.dto.request.UserUpdateRequest;
import com.hust.documentweb.dto.response.UserResponse;
import com.hust.documentweb.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}

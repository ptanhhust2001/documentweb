package com.hust.documentweb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.hust.documentweb.dto.request.RoleRequest;
import com.hust.documentweb.dto.response.RoleResponse;
import com.hust.documentweb.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}

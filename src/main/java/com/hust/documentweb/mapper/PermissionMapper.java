package com.hust.documentweb.mapper;

import org.mapstruct.Mapper;

import com.hust.documentweb.dto.request.PermissionRequest;
import com.hust.documentweb.dto.response.PermissionResponse;
import com.hust.documentweb.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}

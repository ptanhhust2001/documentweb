package com.hust.documentweb.service;

import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hust.documentweb.constant.PredefinedRole;
import com.hust.documentweb.dto.request.UserCreationRequest;
import com.hust.documentweb.dto.request.UserUpdateRequest;
import com.hust.documentweb.dto.response.UserResponse;
import com.hust.documentweb.entity.Role;
import com.hust.documentweb.entity.User;
import com.hust.documentweb.exception.AppException;
import com.hust.documentweb.exception.ErrorCode;
import com.hust.documentweb.mapper.UserMapper;
import com.hust.documentweb.repository.RoleRepository;
import com.hust.documentweb.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapperStruct;
    PasswordEncoder passwordEncoder;
    ModelMapper userMapper;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapperStruct.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);

        return userMapperStruct.toUserResponse(userRepository.save(user));
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.map(user, UserResponse.class);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapperStruct.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapperStruct.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapperStruct::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(Long id) {
        return userMapperStruct.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}

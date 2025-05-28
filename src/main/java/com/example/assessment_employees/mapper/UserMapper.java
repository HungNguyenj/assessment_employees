package com.example.assessment_employees.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.assessment_employees.dto.request.UserRequest;
import com.example.assessment_employees.dto.response.UserResponse;
import com.example.assessment_employees.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "department", source = "department")
    UserResponse toResponse(User user);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserRequest userRequest);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "department.departmentId", source = "departmentId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity2(UserRequest userRequest);
} 
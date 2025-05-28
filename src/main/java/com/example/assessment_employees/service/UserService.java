package com.example.assessment_employees.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.assessment_employees.dto.request.UserRequest;
import com.example.assessment_employees.dto.response.UserResponse;
import com.example.assessment_employees.entity.Department;
import com.example.assessment_employees.entity.User;
import com.example.assessment_employees.exception.ResourceNotFoundException;
import com.example.assessment_employees.mapper.UserMapper;
import com.example.assessment_employees.repository.DepartmentRepository;
import com.example.assessment_employees.repository.UserRepository;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final DepartmentRepository departmentRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }

    public List<UserResponse> getUserByDepartmentId(Integer id) {
        return userRepository.findByDepartment_DepartmentId(id)
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername()))
            throw new EntityExistsException("Username already exists");
        if (userRepository.existsByEmail(userRequest.getEmail()))
            throw new EntityExistsException("Email already exists");

        departmentRepository.findById(userRequest.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + userRequest.getDepartmentId()));

        User user = userMapper.toEntity2(userRequest);
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Integer id, UserRequest userRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!existingUser.getUsername().equals(userRequest.getUsername()) 
            && userRepository.existsByUsername(userRequest.getUsername())) {
            throw new EntityExistsException("Username already exists");
        }

        if (!existingUser.getEmail().equals(userRequest.getEmail()) 
            && userRepository.existsByEmail(userRequest.getEmail())) {
            throw new EntityExistsException("Email already exists");
        }

        Department newDepartment = departmentRepository.findById(userRequest.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + userRequest.getDepartmentId()));

        existingUser.setUsername(userRequest.getUsername());
        existingUser.setEmail(userRequest.getEmail());
        existingUser.setFullName(userRequest.getFullName());
        existingUser.setDepartment(newDepartment);
        existingUser.setRole(userRequest.getRole() != null ? userRequest.getRole() : existingUser.getRole());

        existingUser = userRepository.save(existingUser);
        return userMapper.toResponse(existingUser);
    }

    @Transactional
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }


} 
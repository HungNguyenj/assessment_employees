package com.example.assessment_employees.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.assessment_employees.dto.ApiResponse;
import com.example.assessment_employees.dto.request.UserRequest;
import com.example.assessment_employees.dto.response.UserResponse;
import com.example.assessment_employees.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved successfully", users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Integer id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User retrieved successfully", user));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse createdUser = userService.createUser(userRequest);
        return new ResponseEntity<>(
            new ApiResponse<>(true, "User created successfully", createdUser),
            HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserRequest userRequest) {
        UserResponse updatedUser = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", null));
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUserByDepartmentId(@PathVariable Integer id) {
        List<UserResponse> users = userService.getUserByDepartmentId(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved successfully", users));
    }
}
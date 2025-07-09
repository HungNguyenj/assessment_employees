package com.example.assessment_employees.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.assessment_employees.dto.ApiResponse;
import com.example.assessment_employees.dto.request.UserRequest;
import com.example.assessment_employees.dto.response.UserResponse;
import com.example.assessment_employees.security.AuthorizationService;
import com.example.assessment_employees.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;
    
    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        // Chỉ trả về users mà current user có quyền truy cập
        List<UserResponse> users = userService.getAccessibleUsers();
        return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved successfully", users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Integer id) {
        // Kiểm tra quyền truy cập
        if (!authorizationService.canAccessUser(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(false, "Bạn không có quyền truy cập thông tin này", null));
        }
        
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User retrieved successfully", user));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest userRequest) {
        // Chỉ SUP mới có quyền tạo user mới
        if (authorizationService.getCurrentUser().getRole().name().equals("SUP")) {
            UserResponse createdUser = userService.createUser(userRequest);
            return new ResponseEntity<>(
                new ApiResponse<>(true, "User created successfully", createdUser),
                HttpStatus.CREATED
            );
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(false, "Bạn không có quyền tạo user mới", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserRequest userRequest) {
        // Kiểm tra quyền sửa user
        if (!authorizationService.canAccessUser(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(false, "Bạn không có quyền sửa thông tin này", null));
        }
        
        UserResponse updatedUser = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Integer id) {
        // Chỉ SUP mới có quyền xóa user
        if (authorizationService.getCurrentUser().getRole().name().equals("SUP")) {
            userService.deleteUser(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(false, "Bạn không có quyền xóa user", null));
        }
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUserByDepartmentId(@PathVariable Integer id) {
        // Kiểm tra quyền truy cập department
        if (!authorizationService.canManageDepartment(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(false, "Bạn không có quyền truy cập phòng ban này", null));
        }
        
        List<UserResponse> users = userService.getUserByDepartmentId(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved successfully", users));
    }
}
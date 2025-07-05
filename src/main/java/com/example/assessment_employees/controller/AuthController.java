package com.example.assessment_employees.controller;

import com.example.assessment_employees.entity.User;
import com.example.assessment_employees.repository.UserRepository;
import com.example.assessment_employees.repository.DepartmentRepository;
import com.example.assessment_employees.security.JwtUtil;
import com.example.assessment_employees.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DepartmentRepository departmentRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Sử dụng AuthenticationManager để xác thực an toàn
            // Điều này tự động sử dụng UserDetailsService và PasswordEncoder
            // Tránh được SQL injection và các lỗ hổng bảo mật khác
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );
            
            // Nếu xác thực thành công, lấy thông tin user từ database
            Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Người dùng không tồn tại", null));
            }
            
            User user = userOpt.get();
            
            // Tạo JWT token
            String token = jwtUtil.generateToken(
                user.getUsername(), 
                user.getRole().name(), 
                user.getDepartment().getDepartmentId(),
                user.getIsDepartmentManager()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                "userId", user.getUserId(),
                "username", user.getUsername(),
                "fullName", user.getFullName(),
                "email", user.getEmail(),
                "role", user.getRole().name(),
                "departmentId", user.getDepartment().getDepartmentId(),
                "departmentName", user.getDepartment().getDepartmentName(),
                "isDepartmentManager", user.getIsDepartmentManager()
            ));
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Đăng nhập thành công", response));
            
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Tên đăng nhập hoặc mật khẩu không đúng", null));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Xác thực thất bại: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(false, "Lỗi server: " + e.getMessage(), null));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentUser(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7); // Remove "Bearer "
            String username = jwtUtil.extractUsername(token);
            
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "User không tồn tại", null));
            }
            
            User user = userOpt.get();
            Map<String, Object> userInfo = Map.of(
                "userId", user.getUserId(),
                "username", user.getUsername(),
                "fullName", user.getFullName(),
                "email", user.getEmail(),
                "role", user.getRole().name(),
                "departmentId", user.getDepartment().getDepartmentId(),
                "departmentName", user.getDepartment().getDepartmentName(),
                "isDepartmentManager", user.getIsDepartmentManager()
            );
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Thành công", userInfo));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Token không hợp lệ", null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Map<String, Object>>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Kiểm tra username đã tồn tại
            if (userRepository.existsByUsername(registerRequest.getUsername())) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Tên đăng nhập đã tồn tại", null));
            }
            
            // Kiểm tra email đã tồn tại
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Email đã được sử dụng", null));
            }
            
            // Tạo user mới
            User newUser = new User();
            newUser.setUsername(registerRequest.getUsername());
            newUser.setEmail(registerRequest.getEmail());
            newUser.setFullName(registerRequest.getFullName());
            newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            
            // Set department (cần kiểm tra department có tồn tại)
            Optional<com.example.assessment_employees.entity.Department> departmentOpt = 
                departmentRepository.findById(registerRequest.getDepartmentId());
            if (departmentOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Phòng ban không tồn tại", null));
            }
            newUser.setDepartment(departmentOpt.get());
            
            // Set role mặc định là EMPL
            newUser.setRole(com.example.assessment_employees.entity.UserRole.EMPL);
            newUser.setIsDepartmentManager(false);
            
            // Lưu user
            User savedUser = userRepository.save(newUser);
            
            // Tạo response
            Map<String, Object> response = new HashMap<>();
            response.put("userId", savedUser.getUserId());
            response.put("username", savedUser.getUsername());
            response.put("email", savedUser.getEmail());
            response.put("fullName", savedUser.getFullName());
            response.put("departmentId", savedUser.getDepartment().getDepartmentId());
            response.put("departmentName", savedUser.getDepartment().getDepartmentName());
            response.put("role", savedUser.getRole().name());
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Đăng ký thành công", response));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(false, "Lỗi server: " + e.getMessage(), null));
        }
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
    
    public static class RegisterRequest {
        @NotBlank(message = "Tên đăng nhập không được để trống")
        private String username;
        
        @NotBlank(message = "Mật khẩu không được để trống")
        private String password;
        
        @NotBlank(message = "Email không được để trống")
        @Email(message = "Email không hợp lệ")
        private String email;
        
        @NotBlank(message = "Họ tên không được để trống")
        private String fullName;
        
        @NotNull(message = "ID phòng ban không được để trống")
        private Integer departmentId;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public Integer getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(Integer departmentId) {
            this.departmentId = departmentId;
        }
    }
}
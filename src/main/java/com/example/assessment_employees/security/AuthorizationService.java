package com.example.assessment_employees.security;

import com.example.assessment_employees.entity.User;
import com.example.assessment_employees.entity.UserRole;
import com.example.assessment_employees.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorizationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Lấy thông tin user hiện tại từ JWT token
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetailsService.CustomUserPrincipal) {
            CustomUserDetailsService.CustomUserPrincipal principal = 
                (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
            return principal.getUser();
        }
        return null;
    }

    /**
     * Kiểm tra user có quyền xem/sửa thông tin của targetUserId không
     */
    public boolean canAccessUser(Integer targetUserId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        // SUP có quyền truy cập tất cả
        if (currentUser.getRole() == UserRole.SUP) {
            return true;
        }

        // EMPL chỉ được truy cập thông tin của chính mình
        if (currentUser.getRole() == UserRole.EMPL) {
            return currentUser.getUserId().equals(targetUserId);
        }

        // MANA được truy cập nhân viên trong phòng ban mình quản lý
        if (currentUser.getRole() == UserRole.MANA && currentUser.getIsDepartmentManager()) {
            Optional<User> targetUser = userRepository.findById(targetUserId);
            if (targetUser.isPresent()) {
                return currentUser.getDepartment().getDepartmentId()
                    .equals(targetUser.get().getDepartment().getDepartmentId());
            }
        }

        return false;
    }

    /**
     * Lấy danh sách user mà current user có quyền truy cập
     */
    public List<User> getAccessibleUsers() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return List.of();
        }

        // SUP có quyền xem tất cả
        if (currentUser.getRole() == UserRole.SUP) {
            return userRepository.findAll();
        }

        // EMPL chỉ xem được thông tin của mình
        if (currentUser.getRole() == UserRole.EMPL) {
            return List.of(currentUser);
        }

        // MANA xem được nhân viên trong phòng ban mình quản lý
        if (currentUser.getRole() == UserRole.MANA && currentUser.getIsDepartmentManager()) {
            return userRepository.findByDepartment_DepartmentId(
                currentUser.getDepartment().getDepartmentId());
        }

        return List.of();
    }

    /**
     * Kiểm tra user có quyền quản lý department không
     */
    public boolean canManageDepartment(Integer departmentId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        // SUP có quyền quản lý tất cả department
        if (currentUser.getRole() == UserRole.SUP) {
            return true;
        }

        // MANA chỉ quản lý được department của mình
        if (currentUser.getRole() == UserRole.MANA && currentUser.getIsDepartmentManager()) {
            return currentUser.getDepartment().getDepartmentId().equals(departmentId);
        }

        return false;
    }

    /**
     * Kiểm tra user có quyền tạo/sửa assessment template không
     */
    public boolean canManageAssessmentTemplate() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        // Chỉ MANA và SUP mới có quyền quản lý template
        return currentUser.getRole() == UserRole.MANA || currentUser.getRole() == UserRole.SUP;
    }

    /**
     * Kiểm tra user có quyền đánh giá targetUserId không
     */
    public boolean canAssessUser(Integer targetUserId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        // SUP có thể đánh giá tất cả
        if (currentUser.getRole() == UserRole.SUP) {
            return true;
        }

        // MANA có thể đánh giá nhân viên trong phòng ban
        if (currentUser.getRole() == UserRole.MANA && currentUser.getIsDepartmentManager()) {
            Optional<User> targetUser = userRepository.findById(targetUserId);
            if (targetUser.isPresent()) {
                return currentUser.getDepartment().getDepartmentId()
                    .equals(targetUser.get().getDepartment().getDepartmentId());
            }
        }

        return false;
    }
}
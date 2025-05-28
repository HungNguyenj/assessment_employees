package com.example.assessment_employees.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.assessment_employees.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    List<User> findByDepartment_DepartmentId(Integer departmentId);
}
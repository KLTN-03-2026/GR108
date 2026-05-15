package com.example.demo.repository;

import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IStudentRepository extends JpaRepository<Student, Integer> {
        List<Student> findByStudentCodeContainingIgnoreCaseOrClassNameContainingIgnoreCaseOrFacultyContainingIgnoreCase(
                String studentCode,
                String className,
                String faculty
        );
        boolean existsByCitizenId(String citizenId);
        Student findByUser(User user);
        boolean existsByStudentCode(String studentCode);
}
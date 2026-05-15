package com.example.demo.service;

import com.example.demo.entity.Student;
import com.example.demo.entity.User;

import java.util.List;
import java.util.Optional;

public interface IStudentService {

    List<Student> findAll();

    Optional<Student> findById(Integer id);

    Student save(Student student);

    void delete(Integer id);
    List<Student> search(String keyword);
    long count();
    Student findByUser(User user);
}
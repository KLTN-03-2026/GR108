package com.example.demo.service;

import com.example.demo.entity.Student;
import com.example.demo.repository.IStudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService implements IStudentService {

    private final IStudentRepository studentRepository;

    public StudentService(IStudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> findById(Integer id) {
        return studentRepository.findById(id);
    }

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void delete(Integer id) {
        studentRepository.deleteById(id);
    }
    @Override
    public List<Student> search(String keyword) {

        return studentRepository
                .findByStudentCodeContainingIgnoreCaseOrClassNameContainingIgnoreCaseOrFacultyContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword
                );
    }
}
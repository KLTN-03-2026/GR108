package com.example.demo.service;

import com.example.demo.entity.Student;
import com.example.demo.entity.User;
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

        if (student.getStudentId() != null) {
            return studentRepository.save(student);
        }
        Student existingStudent =
                studentRepository.findByUser(student.getUser());
        if (existingStudent != null) {

            student.setStudentId(existingStudent.getStudentId());

            return studentRepository.save(student);
        }
        if (studentRepository.existsByCitizenId(student.getCitizenId())) {
            throw new RuntimeException("CCCD đã tồn tại trong hệ thống");
        }

        if (studentRepository.existsByStudentCode(student.getStudentCode())) {
            throw new RuntimeException("Mã sinh viên đã tồn tại trong hệ thống");
        }

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
    @Override
    public long count() {
        return studentRepository.count();
    }
    @Override
    public Student findByUser(User user) {
        return studentRepository.findByUser(user);
    }
}
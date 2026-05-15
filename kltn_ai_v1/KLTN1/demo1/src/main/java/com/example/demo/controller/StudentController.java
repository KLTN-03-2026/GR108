package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.service.IStudentService;
import com.example.demo.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final IUserService userService;
    private final IStudentService studentService;

    public StudentController(IStudentService studentService,
                             IUserService userService) {
        this.studentService = studentService;
        this.userService = userService;
    }

    @GetMapping
    public String listStudents(Model model,
                               @RequestParam(value = "keyword", required = false) String keyword) {

        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("students", studentService.search(keyword.trim()));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("students", studentService.findAll());
            model.addAttribute("keyword", "");
        }

        return "admin/student/list";
    }

    // FORM THÊM
    @GetMapping("/create")
    public String createStudent(Model model) {
        model.addAttribute("student", new Student());
        return "admin/student/add";
    }

    @PostMapping("/save")
    public String saveStudent(@ModelAttribute Student student, Model model) {

        try {
            User user = userService.findById(student.getUser().getUserId());

            if (user == null) {
                model.addAttribute("error", "Không tìm thấy user");
                return "guest/booking/form";
            }

            student.setUser(user);

            studentService.save(student);

            return "redirect:/students";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "guest/booking/form";
        }
    }
    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable Integer id, Model model) {
        Student student = studentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));

        model.addAttribute("student", student);
        return "admin/student/edit";
    }

    @GetMapping("/detail/{id}")
    public String detailStudent(@PathVariable Integer id, Model model) {
        Student student = studentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));

        model.addAttribute("student", student);
        return "admin/student/detail";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Integer id) {
        studentService.delete(id);
        return "redirect:/students";
    }
}
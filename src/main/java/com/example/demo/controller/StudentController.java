package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.service.IStudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final IStudentService studentService;

    public StudentController(IStudentService studentService) {
        this.studentService = studentService;
    }

    // LIST
    @GetMapping
    public String list(Model model) {

        model.addAttribute("students", studentService.findAll());

        return "admin/student/list";
    }

    // CREATE FORM
    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("student", new Student());

        return "admin/student/add";
    }

    // SAVE
    @PostMapping("/save")
    public String save(@ModelAttribute Student student) {

        studentService.save(student);

        return "redirect:/students";
    }

    // EDIT FORM
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {

        Student student = studentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        model.addAttribute("student", student);

        return "admin/student/edit";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {

        studentService.delete(id);

        return "redirect:/students";
    }

    // DETAIL
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {

        Student student = studentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        model.addAttribute("student", student);

        return "admin/student/detail";
    }
    // cập nhật thông tin sinh viên qua mã sinh viên
    @PostMapping("/update")
    public String update(@ModelAttribute Student student) {

        studentService.save(student);

        return "redirect:/students";
    }
    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {

        model.addAttribute("students",
                studentService.search(keyword));

        return "admin/student/list";
    }
}
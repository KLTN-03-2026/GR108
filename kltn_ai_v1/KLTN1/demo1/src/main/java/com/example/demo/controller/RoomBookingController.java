package com.example.demo.controller;

import com.example.demo.emumm.BookingStatus;
import com.example.demo.emumm.RoomGender;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomBooking;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.service.IRoomBookingService;
import com.example.demo.service.IRoomService;
import com.example.demo.service.IStudentService;
import com.example.demo.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/booking")
public class RoomBookingController {

    private final IRoomBookingService bookingService;
    private final IRoomService roomService;
    private final IStudentService studentService;
    private final IUserService userService;

    public RoomBookingController(
            IRoomBookingService bookingService,
            IRoomService roomService,
            IStudentService studentService,
            IUserService userService
    ) {
        this.bookingService = bookingService;
        this.roomService = roomService;
        this.studentService = studentService;
        this.userService = userService;
    }
    @GetMapping("/start/{roomId}")
    public String startBooking(@PathVariable Integer roomId,
                               Model model,
                               Principal principal) {

        Room room = roomService.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng"));

        User user = userService.findByUsername(principal.getName());
        if (user == null) throw new RuntimeException("Không tìm thấy user");

        Student student = studentService.findByUser(user);

        model.addAttribute("room", room);
        model.addAttribute("user", user);

        if (student != null) {
            model.addAttribute("student", student);
            model.addAttribute("skipForm", true);
        } else {
            model.addAttribute("student", new Student());
            model.addAttribute("skipForm", false);
        }

        return "guest/booking/form";
    }

    @PostMapping("/confirm")
    public String confirmBooking(
            @RequestParam Integer roomId,
            @RequestParam Integer userId,
            @RequestParam(required = false) String studentCode,
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String faculty,
            @RequestParam(required = false) String citizenId,
            Model model
    ) {

        Room room = roomService.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng"));

        User user = userService.findById(userId);

        if (user == null) {
            throw new RuntimeException("Không tìm thấy user");
        }
        String gender = user.getGender();

        if (gender == null) {
            model.addAttribute("error", "Chưa cập nhật giới tính!");
            return returnForm(model, room, user);
        }

        gender = gender.trim().toUpperCase();

        RoomGender userGender;

        switch (gender) {
            case "MALE":
            case "NAM":
            case "M":
                userGender = RoomGender.MALE;
                break;

            case "FEMALE":
            case "NU":
            case "F":
                userGender = RoomGender.FEMALE;
                break;

            default:
                model.addAttribute("error", "Giới tính không hợp lệ!");
                return returnForm(model, room, user);
        }

        RoomGender roomGender = room.getGender();

        if (roomGender == null) {
            model.addAttribute("error", "Phòng chưa thiết lập giới tính!");
            return returnForm(model, room, user);
        }

        if (userGender == RoomGender.MALE
                && roomGender == RoomGender.FEMALE) {

            model.addAttribute("error", "Nam không được đặt phòng nữ!");
            return returnForm(model, room, user);
        }

        if (userGender == RoomGender.FEMALE
                && roomGender == RoomGender.MALE) {

            model.addAttribute("error", "Nữ không được đặt phòng nam!");
            return returnForm(model, room, user);
        }
        Student student = studentService.findByUser(user);
        if (student != null
                && bookingService.existsActiveBooking(student)) {

            model.addAttribute(
                    "error",
                    "Bạn đã có yêu cầu đặt phòng đang hoạt động!"
            );

            return returnForm(model, room, user);
        }

        if (student == null) {

            if (studentCode == null || studentCode.isBlank()) {

                model.addAttribute(
                        "error",
                        "Vui lòng nhập mã sinh viên!"
                );

                return returnForm(model, room, user);
            }
            student = new Student();
            student.setUser(user);
            student.setStudentCode(studentCode);
            student.setClassName(className);
            student.setFaculty(faculty);
            student.setCitizenId(citizenId);

            try {

                student = studentService.save(student);

            } catch (Exception e) {

                model.addAttribute("error", e.getMessage());

                return returnForm(model, room, user);
            }
        }
        BigDecimal deposit =
                room.getRoomPrice().multiply(BigDecimal.valueOf(0.05));
        model.addAttribute("room", room);
        model.addAttribute("student", student);
        model.addAttribute("depositAmount", deposit);

        return "guest/booking/confirm";
    }
    @PostMapping("/payment")
    public String payment(
            @RequestParam Integer roomId,
            @RequestParam Integer studentId,
            Model model
    ) {
        Room room = roomService.findById(roomId).orElseThrow();
        Student student = studentService.findById(studentId).orElseThrow();

        BigDecimal deposit = room.getRoomPrice().multiply(BigDecimal.valueOf(0.2));

        model.addAttribute("room", room);
        model.addAttribute("student", student);
        model.addAttribute("deposit", deposit);
        model.addAttribute("bankInfo", "VietinBank - 0703710434 - NGUYEN HUU TRUNG");

        return "guest/booking/payment";
    }
    @PostMapping("/submit")
    public String submitPayment(
            @RequestParam Integer roomId,
            @RequestParam Integer studentId,
            @RequestParam BigDecimal depositAmount,
            @RequestParam("paymentProof") MultipartFile paymentProof
    ) throws IOException {

        Room room = roomService.findById(roomId).orElseThrow();
        Student student = studentService.findById(studentId).orElseThrow();

        String filename = System.currentTimeMillis() + "_" + paymentProof.getOriginalFilename();
        Path uploadPath = Paths.get("uploads");

        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        Files.copy(paymentProof.getInputStream(), uploadPath.resolve(filename));

        RoomBooking booking = new RoomBooking();
        booking.setRoom(room);
        booking.setStudent(student);
        booking.setDepositAmount(depositAmount);
        booking.setPaymentProofImage(filename);
        booking.setStatus(BookingStatus.PENDING);
        booking.setBookingDate(LocalDateTime.now());

        bookingService.save(booking);

        return "redirect:/booking/success";
    }

    @GetMapping("/success")
    public String success() {
        return "guest/booking/success";
    }

    @GetMapping("/my-bookings/{studentId}")
    public String myBookings(@PathVariable Integer studentId, Model model) {

        Student student = studentService.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy student"));

        model.addAttribute("bookings", bookingService.findByStudent(student));

        return "guest/booking/my-bookings";
    }

    private String returnForm(Model model, Room room, User user) {

        Student student = studentService.findByUser(user);

        model.addAttribute("room", room);
        model.addAttribute("user", user);

        if (student != null) {
            model.addAttribute("student", student);
            model.addAttribute("skipForm", true);
        } else {
            model.addAttribute("student", new Student());
            model.addAttribute("skipForm", false);
        }

        return "guest/booking/form";
    }
}
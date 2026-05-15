package com.example.demo.controller;

import com.example.demo.emumm.BookingStatus;
import com.example.demo.emumm.RoomGender;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomBooking;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.service.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final IRoomService roomService;
    private final IUserService userService;
    private final INotificationService notificationService;
    private final IRoomBookingService bookingService;
    private final IStudentService studentService;

    public HomeController(IRoomService roomService,
                          IUserService userService,
                          INotificationService notificationService,
                          IRoomBookingService bookingService,
                          IStudentService studentService) {

        this.roomService = roomService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.bookingService = bookingService;
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String home(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) RoomGender gender,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            Model model,
            Principal principal
    ) {

        Pageable pageable = PageRequest.of(page, 6);

        User user = null;
        RoomGender userGender = null;

        // ================= USER INFO =================
        if (principal != null) {

            user = userService.findByUsername(principal.getName());

            model.addAttribute(
                    "notifications",
                    notificationService.findByUser(user)
            );

            if (user != null && user.getGender() != null) {

                String genderStr = user.getGender().trim().toUpperCase();

                switch (genderStr) {
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
                }
            }
        }
        Set<Integer> bookedRoomIds = null;

        if (user != null) {

            Student student = studentService.findByUser(user);

            if (student != null) {

                List<RoomBooking> bookings = bookingService.findByStudent(student);

                bookedRoomIds = bookings.stream()
                        .filter(b ->
                                b.getStatus() == BookingStatus.PENDING ||
                                        b.getStatus() == BookingStatus.APPROVED
                        )
                        .map(b -> b.getRoom().getRoomId())
                        .collect(Collectors.toSet());
            }
        }

        Page<Room> rooms = roomService.search(
                keyword,
                minPrice,
                maxPrice,
                gender,
                status,
                pageable
        );

        model.addAttribute("rooms", rooms);
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("gender", gender);
        model.addAttribute("status", status);

        model.addAttribute("userGender", userGender);
        model.addAttribute("genders", RoomGender.values());
        model.addAttribute("bookedRoomIds", bookedRoomIds);

        return "home";
    }
}
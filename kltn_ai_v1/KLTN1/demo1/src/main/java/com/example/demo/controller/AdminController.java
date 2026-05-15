package com.example.demo.controller;

import com.example.demo.emumm.BookingStatus;
import com.example.demo.entity.Notification;
import com.example.demo.entity.RoomBooking;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {
    @Autowired
    private IRoomService roomService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IRoomBookingService bookingService;
    @Autowired
    private INotificationService notificationService;
    @GetMapping("/admin/home")
    public String adminHome(Model model) {
        model.addAttribute(
                "rooms",
                roomService.findAll()
        );
        model.addAttribute(
                "roomCount",
                roomService.count()
        );
        model.addAttribute(
                "studentCount",
                studentService.count()
        );
        model.addAttribute(
                "accountCount",
                accountService.count()
        );

        return "admin/home-admin";
    }

    @GetMapping("/admin/bookings")
    public String bookingList(Model model) {

        model.addAttribute(
                "bookings",
                bookingService.findAll()
        );

        model.addAttribute(
                "pendingCount",
                bookingService.countByStatus(
                        BookingStatus.PENDING
                )
        );
        return "admin/booking/list";
    }

    @GetMapping("/admin/students")
    public String listStudents(Model model) {

        model.addAttribute(
                "students",
                studentService.findAll()
        );

        return "admin/students/list";
    }

    @PostMapping("/admin/bookings/{id}/approve")
    public String approve(@PathVariable Integer id) {

        RoomBooking booking =
                bookingService.approveBooking(id);

        createNotification(
                booking,
                BookingStatus.APPROVED,
                null
        );

        return "redirect:/admin/bookings";
    }

    @PostMapping("/admin/bookings/{id}/reject")
    public String reject(
            @PathVariable Integer id,
            @RequestParam String note
    ) {

        RoomBooking booking =
                bookingService.rejectBooking(id, note);

        createNotification(
                booking,
                BookingStatus.REJECTED,
                note
        );

        return "redirect:/admin/bookings";
    }

    @PostMapping("/admin/bookings/{id}/cancel")
    public String cancel(@PathVariable Integer id) {

        RoomBooking booking =
                bookingService.cancelBooking(id);

        createNotification(
                booking,
                BookingStatus.CANCELLED,
                null
        );

        return "redirect:/admin/bookings";
    }

    @PostMapping("/admin/bookings/{id}/check-in")
    public String checkIn(@PathVariable Integer id) {

        RoomBooking booking =
                bookingService.checkIn(id);

        createNotification(
                booking,
                BookingStatus.CHECKED_IN,
                null
        );

        return "redirect:/admin/bookings";
    }

    @PostMapping("/admin/bookings/{id}/check-out")
    public String checkOut(@PathVariable Integer id) {

        RoomBooking booking =
                bookingService.checkOut(id);

        createNotification(
                booking,
                BookingStatus.CHECKED_OUT,
                null
        );

        return "redirect:/admin/bookings";
    }

    @PostMapping("/admin/bookings/{id}/expire")
    public String expire(@PathVariable Integer id) {

        RoomBooking booking =
                bookingService.expireBooking(id);

        createNotification(
                booking,
                BookingStatus.EXPIRED,
                null
        );

        return "redirect:/admin/bookings";
    }
    private void createNotification(RoomBooking booking,
                                    BookingStatus status,
                                    String note) {

        Notification notification = new Notification();

        notification.setUser(
                booking.getStudent().getUser()
        );

        notification.setStatus(status);

        String roomNumber = booking.getRoom().getRoomNumber();

        switch (status) {

            case APPROVED:
                notification.setTitle("Đặt phòng được duyệt");
                notification.setContent(
                        "Yêu cầu đặt phòng " +
                                roomNumber +
                                " của bạn đã được duyệt."
                );
                break;

            case REJECTED:
                notification.setTitle("Đặt phòng bị từ chối");
                notification.setContent(
                        "Yêu cầu đặt phòng " +
                                roomNumber +
                                " đã bị từ chối. Lý do: " +
                                note
                );
                break;

            case CANCELLED:
                notification.setTitle("Đặt phòng đã hủy");
                notification.setContent(
                        "Yêu cầu đặt phòng " +
                                roomNumber +
                                " đã bị hủy."
                );
                break;

            case CHECKED_IN:
                notification.setTitle("Đã nhận phòng");
                notification.setContent(
                        "Bạn đã check-in vào phòng " +
                                roomNumber +
                                "."
                );
                break;

            case CHECKED_OUT:
                notification.setTitle("Đã trả phòng");
                notification.setContent(
                        "Bạn đã check-out khỏi phòng " +
                                roomNumber +
                                "."
                );
                break;

            case EXPIRED:
                notification.setTitle("Đặt phòng quá hạn");
                notification.setContent(
                        "Yêu cầu đặt phòng " +
                                roomNumber +
                                " đã quá hạn xử lý."
                );
                break;

            default:
                notification.setTitle("Thông báo đặt phòng");
                notification.setContent("Trạng thái đặt phòng đã thay đổi.");
        }

        notification.setCreatedAt(java.time.LocalDateTime.now());

        notificationService.save(notification);
    }
}
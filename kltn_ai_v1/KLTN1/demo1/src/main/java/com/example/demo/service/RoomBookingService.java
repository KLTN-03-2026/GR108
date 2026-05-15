package com.example.demo.service;

import com.example.demo.emumm.BookingStatus;
import com.example.demo.entity.Notification;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomBooking;
import com.example.demo.entity.Student;
import com.example.demo.repository.IRoomBookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoomBookingService implements IRoomBookingService {

    private final IRoomBookingRepository bookingRepository;
    private final INotificationService notificationService;

    public RoomBookingService(
            IRoomBookingRepository bookingRepository,
            INotificationService notificationService
    ) {
        this.bookingRepository = bookingRepository;
        this.notificationService = notificationService;
    }

    @Override
    public List<RoomBooking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Optional<RoomBooking> findById(Integer id) {
        return bookingRepository.findById(id);
    }

    @Override
    public void save(RoomBooking booking) {
        bookingRepository.save(booking);
    }

    @Override
    public void delete(Integer id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<RoomBooking> findByStudent(Student student) {
        return bookingRepository.findByStudent(student);
    }

    @Override
    public List<RoomBooking> findByRoom(Room room) {
        return bookingRepository.findByRoom(room);
    }

    @Override
    public List<RoomBooking> findByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    @Override
    public RoomBooking findLatestBooking(Student student) {
        return bookingRepository.findTopByStudentOrderByBookingDateDesc(student);
    }

    @Override
    public boolean existsActiveBooking(Student student) {
        return bookingRepository.existsByStudentAndStatusIn(
                student,
                List.of(
                        BookingStatus.PENDING,
                        BookingStatus.APPROVED,
                        BookingStatus.CHECKED_IN
                )
        );
    }

    @Override
    public List<RoomBooking> findActiveBookings(Student student) {
        return bookingRepository.findByStudentAndStatusIn(
                student,
                List.of(
                        BookingStatus.PENDING,
                        BookingStatus.APPROVED,
                        BookingStatus.CHECKED_IN
                )
        );
    }

    @Override
    public long countByStatus(BookingStatus status) {
        return bookingRepository.countByStatus(status);
    }

    @Override
    @Transactional
    public RoomBooking approveBooking(Integer bookingId) {

        RoomBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(BookingStatus.APPROVED);
        booking.setApprovedAt(LocalDateTime.now());
        RoomBooking savedBooking = bookingRepository.save(booking);
        Notification notification = new Notification();

        notification.setUser(
                booking.getStudent().getUser()
        );

        notification.setTitle("Đặt phòng được duyệt");

        notification.setContent(
                "Yêu cầu đặt phòng "
                        + booking.getRoom().getRoomNumber()
                        + " đã được admin duyệt."
        );

        notification.setCreatedAt(LocalDateTime.now());

        notificationService.save(notification);

        return savedBooking;
    }
    @Override
    @Transactional
    public RoomBooking rejectBooking(Integer bookingId, String note) {

        RoomBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(BookingStatus.REJECTED);
        booking.setAdminNote(note);
        booking.setApprovedAt(LocalDateTime.now());

        RoomBooking savedBooking = bookingRepository.save(booking);

        Notification notification = new Notification();

        notification.setUser(
                booking.getStudent().getUser()
        );

        notification.setTitle("Đặt phòng bị từ chối");

        notification.setContent(
                "Yêu cầu đặt phòng của bạn đã bị từ chối."
                        + (note != null ? " Lý do: " + note : "")
        );

        notification.setCreatedAt(LocalDateTime.now());

        notificationService.save(notification);

        return savedBooking;
    }
    @Override
    @Transactional
    public RoomBooking cancelBooking(Integer bookingId) {

        RoomBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(BookingStatus.CANCELLED);

        RoomBooking savedBooking = bookingRepository.save(booking);

        Notification notification = new Notification();

        notification.setUser(
                booking.getStudent().getUser()
        );

        notification.setTitle("Đặt phòng đã bị hủy");

        notification.setContent(
                "Yêu cầu đặt phòng "
                        + booking.getRoom().getRoomNumber()
                        + " đã bị hủy."
        );

        notification.setStatus(BookingStatus.CANCELLED);

        notification.setCreatedAt(LocalDateTime.now());

        notification.setRead(false);

        notificationService.save(notification);

        return savedBooking;
    }
    @Override
    @Transactional
    public RoomBooking checkIn(Integer bookingId) {

        RoomBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(BookingStatus.CHECKED_IN);
        booking.setCheckInDate(LocalDateTime.now());

        RoomBooking savedBooking = bookingRepository.save(booking);

        Notification notification = new Notification();

        notification.setUser(
                booking.getStudent().getUser()
        );

        notification.setTitle("Check-in thành công");

        notification.setContent(
                "Bạn đã check-in vào phòng "
                        + booking.getRoom().getRoomNumber()
                        + "."
        );

        notification.setStatus(BookingStatus.CHECKED_IN);

        notification.setCreatedAt(LocalDateTime.now());

        notification.setRead(false);

        notificationService.save(notification);

        return savedBooking;
    }

    @Override
    @Transactional
    public RoomBooking checkOut(Integer bookingId) {

        RoomBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(BookingStatus.CHECKED_OUT);
        booking.setCheckOutDate(LocalDateTime.now());

        RoomBooking savedBooking = bookingRepository.save(booking);

        Notification notification = new Notification();

        notification.setUser(
                booking.getStudent().getUser()
        );

        notification.setTitle("Check-out thành công");

        notification.setContent(
                "Bạn đã trả phòng "
                        + booking.getRoom().getRoomNumber()
                        + "."
        );

        notification.setStatus(BookingStatus.CHECKED_OUT);

        notification.setCreatedAt(LocalDateTime.now());

        notification.setRead(false);

        notificationService.save(notification);

        return savedBooking;
    }

    @Override
    @Transactional
    public RoomBooking expireBooking(Integer bookingId) {

        RoomBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(BookingStatus.EXPIRED);

        RoomBooking savedBooking = bookingRepository.save(booking);

        Notification notification = new Notification();

        notification.setUser(
                booking.getStudent().getUser()
        );

        notification.setTitle("Đặt phòng đã hết hạn");

        notification.setContent(
                "Yêu cầu đặt phòng "
                        + booking.getRoom().getRoomNumber()
                        + " đã hết hạn."
        );

        notification.setStatus(BookingStatus.EXPIRED);

        notification.setCreatedAt(LocalDateTime.now());

        notification.setRead(false);

        notificationService.save(notification);

        return savedBooking;
    }
}
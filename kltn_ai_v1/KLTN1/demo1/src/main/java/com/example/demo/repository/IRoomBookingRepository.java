package com.example.demo.repository;

import com.example.demo.emumm.BookingStatus;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomBooking;
import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRoomBookingRepository extends JpaRepository<RoomBooking, Integer> {
    List<RoomBooking> findByStudent(Student student);
    List<RoomBooking> findByRoom(Room room);

    List<RoomBooking> findByStatus(BookingStatus status);

    RoomBooking findTopByStudentOrderByBookingDateDesc(
            Student student
    );

    boolean existsByStudentAndStatusIn(
            Student student,
            List<BookingStatus> statuses
    );

    List<RoomBooking> findByStudentAndStatus(
            Student student,
            BookingStatus status
    );

    List<RoomBooking> findByRoomAndStatus(
            Room room,
            BookingStatus status
    );

    long countByStatus(BookingStatus status);

    List<RoomBooking> findByStudentAndStatusIn(
            Student student,
            List<BookingStatus> statuses
    );
}

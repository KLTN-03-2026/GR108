package com.example.demo.service;

import com.example.demo.emumm.BookingStatus;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomBooking;
import com.example.demo.entity.Student;

import java.util.List;
import java.util.Optional;

public interface IRoomBookingService {
    List<RoomBooking> findAll();

    Optional<RoomBooking> findById(Integer id);

    void save(RoomBooking booking);

    void delete(Integer id);

    List<RoomBooking> findByStudent(Student student);

    List<RoomBooking> findByRoom(Room room);

    List<RoomBooking> findByStatus(BookingStatus status);

    RoomBooking findLatestBooking(Student student);

    boolean existsActiveBooking(Student student);

    List<RoomBooking> findActiveBookings(Student student);

    long countByStatus(BookingStatus status);
    RoomBooking approveBooking(Integer id);

    RoomBooking rejectBooking(Integer bookingId, String note);

    RoomBooking cancelBooking(Integer bookingId);

    RoomBooking checkIn(Integer bookingId);

    RoomBooking checkOut(Integer bookingId);

    RoomBooking expireBooking(Integer bookingId);

}

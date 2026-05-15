package com.example.demo.service;

import com.example.demo.emumm.RoomGender;
import com.example.demo.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IRoomService {
    List<Room> findAll();

    Optional<Room> findById(Integer id);

    void save(Room room);

    void delete(Integer id);

    Page<Room> findAll(Pageable pageable);

    Page<Room> search(
            String keyword,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            RoomGender gender,
            String status,
            Pageable pageable
    );

    long count();

    List<Room> findByGender(RoomGender gender);

    List<Room> findAvailableRooms();

    List<Room> findFullRooms();

    boolean isAvailable(Integer roomId);
}
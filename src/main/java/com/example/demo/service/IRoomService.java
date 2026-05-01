package com.example.demo.service;

import com.example.demo.entity.Room;

import java.util.List;
import java.util.Optional;

public interface IRoomService {
    List<Room> findAll();
    Optional<Room> findById(Integer id);
    void save(Room room);
    void delete(Integer id);
}

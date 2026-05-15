package com.example.demo.service;

import com.example.demo.emumm.RoomGender;
import com.example.demo.entity.Room;
import com.example.demo.repository.IRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService implements IRoomService {

    @Autowired
    private IRoomRepository roomRepository;

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public Optional<Room> findById(Integer id) {
        return roomRepository.findById(id);
    }

    @Override
    public void save(Room room) {
        roomRepository.save(room);
    }

    @Override
    public void delete(Integer id) {
        roomRepository.deleteById(id);
    }

    @Override
    public Page<Room> findAll(Pageable pageable) {
        return roomRepository.findAll(pageable);
    }

    @Override
    public Page<Room> search(
            String keyword,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            RoomGender gender,
            String status,
            Pageable pageable
    ) {

        return roomRepository.search(
                keyword,
                minPrice,
                maxPrice,
                gender,
                status,
                pageable
        );
    }

    @Override
    public long count() {
        return roomRepository.count();
    }

    @Override
    public List<Room> findByGender(RoomGender gender) {

        return roomRepository.findByGender(gender);
    }

    @Override
    public List<Room> findAvailableRooms() {

        return roomRepository.findAvailableRooms();
    }

    @Override
    public List<Room> findFullRooms() {

        return roomRepository.findFullRooms();
    }

    @Override
    public boolean isAvailable(Integer roomId) {

        Optional<Room> optionalRoom =
                roomRepository.findById(roomId);

        if (optionalRoom.isEmpty()) {
            return false;
        }

        Room room = optionalRoom.get();

        return room.getCurrentOccupancy()
                < room.getCapacity();
    }
}
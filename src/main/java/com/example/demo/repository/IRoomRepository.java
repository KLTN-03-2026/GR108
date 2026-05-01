package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Room;

public interface IRoomRepository extends JpaRepository<Room, Integer> {

}

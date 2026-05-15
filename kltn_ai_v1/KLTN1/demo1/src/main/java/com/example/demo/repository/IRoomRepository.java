package com.example.demo.repository;

import com.example.demo.emumm.RoomGender;
import com.example.demo.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface IRoomRepository
        extends JpaRepository<Room, Integer> {
    @Query("""
SELECT r FROM Room r
WHERE (:keyword IS NULL OR :keyword = ''
       OR LOWER(r.roomNumber) LIKE LOWER(CONCAT('%', :keyword, '%')))

AND (:minPrice IS NULL OR r.roomPrice >= :minPrice)

AND (:maxPrice IS NULL OR r.roomPrice <= :maxPrice)

AND (:gender IS NULL OR r.gender = :gender)

AND (
    :status IS NULL OR :status = ''
    OR (:status = 'available' AND r.currentOccupancy < r.capacity)
    OR (:status = 'full' AND r.currentOccupancy >= r.capacity)
)
""")
    Page<Room> search(
            @Param("keyword") String keyword,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("gender") RoomGender gender,
            @Param("status") String status,
            Pageable pageable
    );
    List<Room> findByGender(RoomGender gender);

    @Query("""
    SELECT r FROM Room r
    WHERE r.currentOccupancy < r.capacity
    """)
    List<Room> findAvailableRooms();

    @Query("""
    SELECT r FROM Room r
    WHERE r.currentOccupancy >= r.capacity
    """)
    List<Room> findFullRooms();
}
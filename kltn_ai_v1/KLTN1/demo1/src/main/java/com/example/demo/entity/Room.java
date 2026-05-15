package com.example.demo.entity;

import com.example.demo.emumm.RoomGender;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@Setter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomId;

    @NotBlank(message = "Không được để trống số phòng")
    @Column(unique = true)
    private String roomNumber;

    @Min(value = 1, message = "Sức chứa phải > 0")
    private Integer capacity;

    @Min(value = 0, message = "Số người hiện tại >= 0")
    private Integer currentOccupancy = 0;

    @NotNull(message = "Giá phòng không được null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải > 0")
    private BigDecimal roomPrice;

    @Lob
    private String image;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private RoomGender gender;

    private Boolean active = true;

    @OneToMany(mappedBy = "room")
    private List<RoomBooking> bookings;
}
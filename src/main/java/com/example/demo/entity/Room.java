package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "rooms")
@Getter
@Setter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomId;

    private String roomNumber;

    private Integer capacity;

    private Integer currentOccupancy = 0;

    private BigDecimal roomPrice;
    @Column(name = "image")
    private String image;
    @Lob
    private String description;

}
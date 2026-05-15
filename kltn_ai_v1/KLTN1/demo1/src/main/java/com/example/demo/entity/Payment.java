package com.example.demo.entity;

import com.example.demo.emumm.PaymentStatus;
import com.example.demo.emumm.PaymentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private RoomBooking booking;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private Integer paymentMonth;

    private Integer paymentYear;

    private BigDecimal amount;

    private String paymentMethod;

    private String transactionCode;

    private String paymentImage;

    private LocalDateTime paymentDate;
}
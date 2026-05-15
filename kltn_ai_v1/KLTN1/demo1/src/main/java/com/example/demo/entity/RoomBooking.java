package com.example.demo.entity;

import com.example.demo.emumm.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "room_bookings")
@Getter
@Setter
public class RoomBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingId;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @CreationTimestamp
    private LocalDateTime bookingDate;

    private LocalDateTime checkInDate;

    private LocalDateTime checkOutDate;

    private BigDecimal depositAmount;
    @Column(name = "payment_proof_image")  // ← thêm dòng này
    private String paymentProofImage;

    private LocalDateTime approvedAt;

    @Column(columnDefinition = "TEXT")
    private String adminNote;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

    @OneToMany(mappedBy = "booking",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Payment> payments;
}
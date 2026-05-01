package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "access_statistics")
@Getter
@Setter
public class AccessStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer statisticId;

    private LocalDateTime accessDate;

    private Integer visitCount;
}

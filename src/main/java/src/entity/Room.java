package src.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import src.enumm.RoomStatus;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    // số phòng: A101, B202
    @Column(unique = true, nullable = false)
    String roomNumber;

    // loại phòng: 4 người, 6 người
    String type;

    // sức chứa tối đa
    int capacity;

    // số người đang ở
    int currentOccupancy;

    // giá phòng
    BigDecimal price;
    String description;

    // trạng thái phòng
    @Enumerated(EnumType.STRING)
    RoomStatus status;

    // 1 phòng có nhiều sinh viên
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    List<Student> students;
}
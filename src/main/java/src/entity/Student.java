package src.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import src.enumm.StudentStatus;

import java.time.LocalDate;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotBlank
    String name;

    @Column(unique = true, nullable = false)
    String mssv;

    @NotBlank
    String phone;

    String address;

    LocalDate dateOfBirth;

    LocalDate dateOfEntry;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    Account account;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    Room room;
    @Enumerated(EnumType.STRING)
    StudentStatus status;
}
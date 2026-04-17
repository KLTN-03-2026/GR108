package src.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ACCOUNT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

// Lớp Account đại diện cho tài khoản người dùng trong hệ thống, với các thuộc tính như id, username, password, email và role. Các ràng buộc được áp dụng để đảm bảo tính hợp lệ của dữ liệu, ví dụ như username và email phải là duy nhất và không được để trống, password cũng không được để trống, và email phải có định dạng hợp lệ.
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Column(unique = true, nullable = false)
    String username;
    @NotBlank(message = "Mật khẩu không được để trống")
    String password;
    @Email(message = "Email không hợp lệ")
    @Column(unique = true, nullable = false)
    String email;
    String role;
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    Student student;
}

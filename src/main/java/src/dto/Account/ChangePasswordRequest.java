package src.dto.Account;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data // Lombok sẽ tự động tạo getter, setter, toString, equals và hashCode
public class ChangePasswordRequest {

    @NotBlank(message = "Username không được để trống")
    private String username;

    @NotBlank(message = "Mật khẩu cũ không được để trống")
    private String oldPassword;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    private String newPassword;
}

package src.dto.Account;

import lombok.Builder;
import lombok.Data;

@Data // @data dùng để tự động tạo các phương thức getter, setter, toString, equals và hashCode
@Builder // @Builder giúp tạo đối tượng một cách linh hoạt và dễ đọc hơn
public class AccountRespone {
    private Integer id;
    private String username;
    private String email;
    private String role;
}

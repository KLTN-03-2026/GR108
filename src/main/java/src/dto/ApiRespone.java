package src.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
// Buider giúp tạo đối tượng một cách linh hoạt và dễ đọc hơn
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiRespone<T> {
    private Integer code;
    private String message;
    T data;
}

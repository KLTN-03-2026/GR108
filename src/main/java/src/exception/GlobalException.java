package src.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import src.dto.ApiRespone;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
// @controlleradvice  giúp xử lý ngoại lệ trên toàn bộ ứng dụng trong Spring Boot.
public class GlobalException {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleApiException(ApiException e) {
        // Xử lý ngoại lệ ApiException ở đây
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(
                ApiRespone.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }
    @ExceptionHandler(MethodArgumentNotValidException.class) // bắt ngoại lệ của validation
    @ResponseStatus(HttpStatus.BAD_REQUEST) // trả về mã 400
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Map để lưu lỗi dưới dạng: { fieldName: message }
        Map<String, String> errors = new HashMap<>();

        // Lặp qua toàn bộ lỗi validation
        ex.getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField(); // tên thuộc tính bị lỗi
            String errorMessage = error.getDefaultMessage();    // thông báo lỗi (từ annotation)
            errors.put(fieldName, errorMessage);
        });

        // Trả về ResponseEntity với mã 4000 và body có cấu trúc chuẩn
        return ResponseEntity.badRequest().body(ApiRespone.builder()
                .data(errors)                // chứa map lỗi { field: message }
                .message("Invalid input")      // thông báo chung
                .code(4000)                    // mã lỗi tùy chọn (do bạn định nghĩa)
                .build()
        );
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatus(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(
                ApiRespone.builder()
                        .code(e.getStatusCode().value())
                        .message(e.getReason())
                        .build()
        );
    }
}

package src.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import src.dto.Account.AccountRequest;
import src.dto.Account.AccountRespone;
import src.dto.Account.RegisterRequest;
import src.dto.ApiRespone;
import src.entity.Account;
import src.mapper.IUserMapper;
import src.respontory.IUserRespontory;
import src.service.IUserService;
import src.dto.Account.ChangePasswordRequest;

import java.util.Optional;

@RestController
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class LoginController {
    IUserService userService; // gọi IUserService để sử dụng các phương thức liên quan đến người dùng
    IUserMapper userMapper; // gọi IUserMapper để chuyển đổi giữa các đối tượng người dùng
    IUserRespontory userRepository;
    // Đăng nhập tài khoản
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AccountRequest request) {
        Optional<AccountRespone> account = userService.login(request.getUsername(), request.getPassword());
        ApiRespone<AccountRespone> response = ApiRespone.<AccountRespone>builder()
                .code(200)
                .message("Đăng nhập thành công")
                .data(account.orElse(null)) // trả về null nếu không tìm thấy tài khoản
                .build();
        return ResponseEntity.ok(response);
    }
    // Đăng ký tài khoản mới
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        // Kiểm tra nếu tên đăng nhập đã tồn tại
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Tên đăng nhập đã tồn tại");
        }
        // kiểm tra nếu email đã tồn tại
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email đã tồn tại");
        }
        // Chuyển đổi RegisterRequest thành Account entity
        Account newAccount = userMapper.toAccount(request);
        // Lưu tài khoản mới vào cơ sở dữ liệu
        Account savedAccount = userService.save(newAccount);
        // Chuyển đổi Account entity thành AccountRespone
        AccountRespone accountRespone = userMapper.toAccountRespone(savedAccount);
        ApiRespone<AccountRespone> response = ApiRespone.<AccountRespone>builder()
                .code(201)
                .message("Đăng ký thành công")
                .data(accountRespone)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    // đổi mật khẩu tài khoản, yêu cầu cung cấp mật khẩu cũ và mật khẩu mới
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        // 1. Kiểm tra tài khoản có tồn tại không
        Account account = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tài khoản không tồn tại"));

        // 2. Kiểm tra mật khẩu cũ có đúng không
        if (!account.getPassword().equals(request.getOldPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Mật khẩu cũ không chính xác");
        }
        // 3. Kiểm tra mật khẩu mới có trùng mật khẩu cũ không
        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Mật khẩu mới không được trùng với mật khẩu cũ");
        }
        // 4. Cập nhật mật khẩu mới
        account.setPassword(request.getNewPassword()); // nếu có mã hoá mật khẩu thì mã hoá ở đây
        Account savedAccount = userService.save(account);

        // 5. Map sang response
        AccountRespone accountRespone = userMapper.toAccountRespone(savedAccount);
        ApiRespone<AccountRespone> response = ApiRespone.<AccountRespone>builder()
                .code(200)
                .message("Đổi mật khẩu thành công")
                .data(accountRespone)
                .build();
        return ResponseEntity.ok(response);
    }
    // Đăng xuất tài khoản, yêu cầu cung cấp tên đăng nhập
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody AccountRequest request) {
        // Validate username
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Username không được bỏ trống");
        }
        // 1. Kiểm tra tài khoản có tồn tại không
        Account account = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tài khoản không tồn tại"));
        // 2. Thực hiện đăng xuất (nếu có session hoặc token thì xóa ở đây)
        // Ví dụ: session.invalidate() hoặc tokenService.invalidateToken(account)
        ApiRespone<Void> response = ApiRespone.<Void>builder()
                .code(200)
                .message("Đăng xuất thành công")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
    // Xóa tài khoản, yêu cầu cung cấp tên đăng nhập và mật khẩu để xác thực trước khi xóa
    @PostMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@RequestBody AccountRequest request) {
        // Validate username and password
        if (request.getUsername() == null || request.getUsername().trim().isEmpty() ||
                request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Username và Password không được bỏ trống");
        }
        // 1. Kiểm tra tài khoản có tồn tại không
        Account account = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tài khoản không tồn tại"));
        // 2. Kiểm tra mật khẩu có đúng không
        if (!account.getPassword().equals(request.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Mật khẩu không chính xác");
        }
        // 3. Thực hiện xóa tài khoản
        userService.delete(account);
        ApiRespone<Void> response = ApiRespone.<Void>builder()
                .code(200)
                .message("Xóa tài khoản thành công")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }


}

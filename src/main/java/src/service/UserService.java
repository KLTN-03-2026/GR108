package src.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import src.dto.Account.AccountRespone;
import src.entity.Account;
import src.respontory.IUserRespontory;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    // PHẢI là final (hoặc @Autowired). Với @RequiredArgsConstructor thì Spring mới inject.
    private final IUserRespontory userRepository;

    @Override
    public Optional<AccountRespone> login(String username, String password) {
        Account acc = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException( // orElseThrow: nếu không tìm thấy thì ném ra ngoại lệ
                        HttpStatus.UNAUTHORIZED, "Đăng nhập thất bại, tên đăng nhập không tồn tại")); // UNAUTHORIZED là mã trạng thái HTTP 401

        if (!acc.getPassword().equals(password)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Mật khẩu không dúng");
        }
        return Optional.of(AccountRespone.builder()
                .id(acc.getId())
                .username(acc.getUsername())
                .email(acc.getEmail())
                .role(acc.getRole())
                .build());

        // return userRepository.findByUsernameAndPassword(username, password)
        //     .orElseThrow(() -> new ResponseStatusException(
        //             HttpStatus.UNAUTHORIZED, "Invalid username or password"));
    }

    @Override
    public Account save(Account account) {
        return userRepository.save(account);
    }

    @Override
    public void delete(Account account) {
        userRepository.delete(account);
    }

}

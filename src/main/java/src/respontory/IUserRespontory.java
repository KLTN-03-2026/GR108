package src.respontory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.entity.Account;
import lombok.*;

import java.util.Optional;
@Repository

public interface IUserRespontory extends JpaRepository<Account, Integer> {
    Optional<Account> findByUsernameAndPassword(String username, String password);
    // Optional giúp tránh lỗi NullPointerException, đây là 1 cách an toàn để xử lý giá trị có thể null
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

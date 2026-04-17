package src.service;

import src.dto.Account.AccountRespone;
import src.entity.Account;

import java.util.Optional;

public interface IUserService {
    Optional<AccountRespone> login(String username, String password);
    Account save(Account account);
    void delete(Account account);
}

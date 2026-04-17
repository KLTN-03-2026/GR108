package src.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import src.dto.Account.AccountRequest;
import src.dto.Account.AccountRespone;
import src.dto.Account.RegisterRequest;
import src.entity.Account;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    AccountRespone accountToAccountRespone(Account account);
    Account accountRequestToAccount(AccountRequest accountRespone);
    // đăng ký
    // chuyển đổi entity -> response
    AccountRespone toAccountRespone(Account account);

    // register request -> entity
    @Mapping(target = "id", ignore = true) // target là id trong entity, ignore giúp bỏ qua trường này
    // chuyển đổi RegisterRequest thành Account entity
    Account toAccount(RegisterRequest request);

}

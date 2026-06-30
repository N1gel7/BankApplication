package test.bankapplication.dto.mapper;

import test.bankapplication.dto.response.AccountResponseDTO;
import test.bankapplication.entity.Account;

public class AccountMapper {

    private AccountMapper() {
    }

    public static AccountResponseDTO toAccountResponse(Account account) {
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setAccountId(account.getId());
        dto.setUser(UserMapper.toUserDTO(account.getUser()));
        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        return dto;
    }
}

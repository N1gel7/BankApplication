package test.bankapplication.dto.request;

import jakarta.validation.constraints.NotBlank;
import test.bankapplication.enums.AccountType;

public class AccountRequestDTO {
    @NotBlank
    private AccountType accountType;

    public AccountRequestDTO() {
        this.accountType = accountType;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}

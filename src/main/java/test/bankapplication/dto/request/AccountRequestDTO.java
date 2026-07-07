package test.bankapplication.dto.request;

import jakarta.validation.constraints.NotNull;
import test.bankapplication.enums.AccountType;

public class AccountRequestDTO {
    @NotNull
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

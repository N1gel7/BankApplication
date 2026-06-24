package test.bankapplication.dto.request;

import test.bankapplication.enums.AccountType;

public class AccountRequestDTO {
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

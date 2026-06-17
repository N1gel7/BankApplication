package test.bankapplication.dto.response;

import test.bankapplication.enums.AccountType;

import java.math.BigDecimal;

public class AccountResponseDTO {
    private Integer accountId;
    private UserDTO user;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;

    public AccountResponseDTO(Integer accountId, UserDTO user, String accountNumber, AccountType accountType, BigDecimal balance) {
        this.accountId = accountId;
        this.user = user;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;

    }

    public AccountResponseDTO() {
        
    }


    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


}

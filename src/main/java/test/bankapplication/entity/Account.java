package test.bankapplication.entity;

import jakarta.persistence.*;
import test.bankapplication.enums.AccountType;

import java.math.BigDecimal;
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    private User user;
    private String accountNumber;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private BigDecimal balance;

    public Account() {
    }

    public Account(User user, String accountNumber, AccountType accountType, BigDecimal balance) {
        this.user = user;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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
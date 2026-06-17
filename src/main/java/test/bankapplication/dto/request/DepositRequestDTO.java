package test.bankapplication.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class DepositRequestDTO {
    @NotBlank
    private BigDecimal amount;
    private String accountNumber;

    public DepositRequestDTO() {
    }

    public DepositRequestDTO(BigDecimal amount, String accountNumber) {
        this.amount = amount;
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}

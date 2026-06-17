package test.bankapplication.dto.request;

import java.math.BigDecimal;

public class TransferRequestDTO {
    private BigDecimal amount;
    private String accountNumber;

    public TransferRequestDTO() {
    }

    public TransferRequestDTO(BigDecimal amount, String accountNumber) {
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

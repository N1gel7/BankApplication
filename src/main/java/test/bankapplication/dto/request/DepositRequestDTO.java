package test.bankapplication.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class DepositRequestDTO {
    @NotBlank
    private BigDecimal amount;


    public DepositRequestDTO() {
    }

    public DepositRequestDTO(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

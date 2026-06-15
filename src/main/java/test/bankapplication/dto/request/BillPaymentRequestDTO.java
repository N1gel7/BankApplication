package test.bankapplication.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class BillPaymentRequestDTO {
    @NotBlank
    private BigDecimal amount;
    @NotBlank
    private Integer receiverId;

    public BillPaymentRequestDTO() {
    }

    public BillPaymentRequestDTO(BigDecimal amount, Integer receiverId) {
        this.amount = amount;
        this.receiverId = receiverId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }
}

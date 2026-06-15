package test.bankapplication.dto.request;

import java.math.BigDecimal;

public class TransferRequestDTO {
    private BigDecimal amount;
    private Integer receiverId;

    public TransferRequestDTO() {
    }

    public TransferRequestDTO(BigDecimal amount, Integer receiverId) {
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

package test.bankapplication.dto.response;

import test.bankapplication.enums.TransactionStatus;
import test.bankapplication.enums.TransactionType;

import java.math.BigDecimal;

public class TransactionResponseDTO {
    private Integer id;
    private BigDecimal amount;
    private TransactionType transactionType;
    private BigDecimal fee;
    private BigDecimal totalDeducted;
    private TransactionStatus transactionStatus;
    private java.time.LocalDateTime timestamp;

    public TransactionResponseDTO() {
    }

    public TransactionResponseDTO(BigDecimal amount, TransactionType transactionType, BigDecimal fee) {
        this.amount = amount;
        this.transactionType = transactionType;
        this.fee = fee;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public BigDecimal getTotalDeducted() {
        return totalDeducted;
    }

    public void setTotalDeducted(BigDecimal totalDeducted) {
        this.totalDeducted = totalDeducted;
    }

    public java.time.LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(java.time.LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

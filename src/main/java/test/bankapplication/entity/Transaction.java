package test.bankapplication.entity;

import jakarta.persistence.*;
import test.bankapplication.enums.TransactionStatus;
import test.bankapplication.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @ManyToOne
    private Account sender;
    @ManyToOne
    private Account receiver;
    @Column(nullable = false)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private BigDecimal fee;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;


    public Transaction() {
    }

    public Transaction(Account sender, Account receiver, BigDecimal amount, TransactionType transactionType, BigDecimal fee, LocalDateTime createdAt, TransactionStatus transactionStatus) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.transactionType = transactionType;
        this.fee = fee;
        this.createdAt = createdAt;
        this.transactionStatus = transactionStatus;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public Account getReceiver() {
        return receiver;
    }

    public void setReceiver(Account receiver) {
        this.receiver = receiver;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}

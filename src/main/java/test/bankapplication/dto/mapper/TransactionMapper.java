package test.bankapplication.dto.mapper;

import test.bankapplication.dto.response.TransactionResponseDTO;
import test.bankapplication.entity.Transaction;

import java.math.BigDecimal;

public class TransactionMapper {

    private TransactionMapper() {
    }

    public static TransactionResponseDTO toTransactionResponse(Transaction transaction, BigDecimal totalDeducted) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setFee(transaction.getFee());
        dto.setTotalDeducted(totalDeducted);
        dto.setTransactionStatus(transaction.getTransactionStatus());
        dto.setTimestamp(transaction.getCreatedAt());
        return dto;
    }
}

package test.bankapplication.dto.request;

import java.math.BigDecimal;

public class TransactionRequestDTO {
    private BigDecimal amount;


    public TransactionRequestDTO() {
    }

    public TransactionRequestDTO(BigDecimal amount) {
        this.amount = amount;
    }

    
}

package test.bankapplication.controller;

import org.springframework.web.bind.annotation.*;
import test.bankapplication.dto.request.DepositRequestDTO;
import test.bankapplication.dto.request.TransferRequestDTO;
import test.bankapplication.dto.response.TransactionResponseDTO;
import test.bankapplication.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public TransactionResponseDTO transfer(@RequestBody TransferRequestDTO transferRequestDTO, String email){
        return transactionService.transfer(transferRequestDTO,email);
    }

    @PostMapping("/deposit")
    public TransactionResponseDTO deposit(@RequestBody DepositRequestDTO depositRequestDTO){
        return transactionService.deposit(depositRequestDTO);
    }

    @GetMapping("/me")
    public List<TransactionResponseDTO> getTransactions(String email){
        return transactionService.getMyTransactions(email);
    }



}

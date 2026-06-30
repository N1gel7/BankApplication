package test.bankapplication.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import test.bankapplication.dto.request.DepositRequestDTO;
import test.bankapplication.dto.request.TransferRequestDTO;
import test.bankapplication.dto.response.TransactionResponseDTO;
import test.bankapplication.service.TransactionService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public TransactionResponseDTO transfer(@Valid @RequestBody TransferRequestDTO transferRequestDTO, Principal principal){
        return transactionService.transfer(transferRequestDTO,principal.getName());
    }

    @PostMapping("/deposit")
    public TransactionResponseDTO deposit(@Valid @RequestBody DepositRequestDTO depositRequestDTO){
        return transactionService.deposit(depositRequestDTO);
    }

    @GetMapping("/me")
    public List<TransactionResponseDTO> getTransactions(Principal principal){
        return transactionService.getMyTransactions(principal.getName());
    }

    @GetMapping("")
    public List<TransactionResponseDTO> getAllTransactions(@RequestParam(required = false) Integer userId){
        return  transactionService.getAllTransactions(userId);
    }


}

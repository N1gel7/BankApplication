package test.bankapplication.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<TransactionResponseDTO> transfer(@Valid @RequestBody TransferRequestDTO transferRequestDTO, Principal principal){
        return ResponseEntity.ok(transactionService.transfer(transferRequestDTO,principal.getName()));
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDTO> deposit(@Valid @RequestBody DepositRequestDTO depositRequestDTO){
        return ResponseEntity.ok(transactionService.deposit(depositRequestDTO));
    }

    @GetMapping("/me")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactions(Principal principal){
        return ResponseEntity.ok(transactionService.getMyTransactions(principal.getName()));
    }

    @GetMapping("")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions(@RequestParam(required = false) Integer userId){
        return ResponseEntity.ok(transactionService.getAllTransactions(userId)) ;
    }


}

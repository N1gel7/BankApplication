package test.bankapplication.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.bankapplication.dto.request.AccountRequestDTO;
import test.bankapplication.dto.response.AccountResponseDTO;
import test.bankapplication.service.AccountService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(Principal principal, @Valid @RequestBody AccountRequestDTO accountRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(principal.getName(), accountRequestDTO));
    }
    @GetMapping("/me")
    public ResponseEntity<AccountResponseDTO> getMyAccount(Principal principal){
        return ResponseEntity.ok(accountService.getAccountDetails(principal.getName()));
    }


}

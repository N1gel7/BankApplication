package test.bankapplication.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDTO createAccount(Principal principal, @Valid @RequestBody AccountRequestDTO accountRequestDTO){
        return accountService.createAccount(principal.getName(),accountRequestDTO);
    }
    @GetMapping("/me")
    public AccountResponseDTO getMyAccount(Principal principal){
        return accountService.getAccountDetails(principal.getName());
    }


}

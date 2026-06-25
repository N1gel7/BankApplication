package test.bankapplication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import test.bankapplication.dto.request.AccountRequestDTO;
import test.bankapplication.dto.response.AccountResponseDTO;
import test.bankapplication.service.AccountService;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDTO createAccount(java.security.Principal principal, @RequestBody AccountRequestDTO accountRequestDTO){
        return accountService.createAccount(principal.getName(),accountRequestDTO);
    }
    @GetMapping("/me")
    public AccountResponseDTO getMyAccount(java.security.Principal principal){
        return accountService.getAccountDetails(principal.getName());
    }


}

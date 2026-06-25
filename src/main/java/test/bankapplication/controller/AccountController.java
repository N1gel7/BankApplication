package test.bankapplication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import test.bankapplication.dto.request.AccountRequestDTO;
import test.bankapplication.dto.response.AccountResponseDTO;
import test.bankapplication.service.AccountService;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private AccountService accountService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDTO createAccount(String email,AccountRequestDTO accountRequestDTO){
        return accountService.createAccount(email,accountRequestDTO);
    }
    @GetMapping("/me")
    public AccountResponseDTO getMyAccount(String email){
        return accountService.getAccountDetails(email);
    }


}

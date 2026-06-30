package test.bankapplication.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import test.bankapplication.dto.mapper.AccountMapper;
import test.bankapplication.dto.request.AccountRequestDTO;
import test.bankapplication.dto.response.AccountResponseDTO;
import test.bankapplication.entity.Account;
import test.bankapplication.entity.User;
import test.bankapplication.enums.KycStatus;
import test.bankapplication.exception.ResourceNotFoundException;
import test.bankapplication.exception.UnauthorizedException;
import test.bankapplication.repository.AccountRepository;
import test.bankapplication.repository.UserRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public AccountService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }
    @Value("${account.balance.limit}")
    private BigDecimal minimumBalance;

  public AccountResponseDTO createAccount(String email,AccountRequestDTO accountRequestDTO){

      User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("This user does not Exist"));
      if(user.getKycStatus() != KycStatus.APPROVED){
          throw new UnauthorizedException("KYC not approved");
      }
      Account account = new Account();
      account.setUser(user);
      account.setAccountType(accountRequestDTO.getAccountType());
      account.setAccountNumber(generateAccountNumber());
      account.setBalance(minimumBalance);
      Account savedAccount = accountRepository.save(account);
      return AccountMapper.toAccountResponse(savedAccount);

  }

  public AccountResponseDTO getAccountDetails(String email){
      User user = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("This user does not exist"));
      Account account = accountRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException("This account does not exist"));
      return AccountMapper.toAccountResponse(account);
  }

  private String generateAccountNumber(){
      return UUID.randomUUID().toString().substring(0,13);
  }
}

package test.bankapplication.service;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import test.bankapplication.dto.request.AccountRequestDTO;
import test.bankapplication.dto.response.AccountResponseDTO;
import test.bankapplication.dto.response.UserDTO;
import test.bankapplication.entitiy.Account;
import test.bankapplication.entitiy.User;
import test.bankapplication.enums.KycStatus;
import test.bankapplication.exception.ResourceNotFoundException;
import test.bankapplication.exception.UnauthorizedException;
import test.bankapplication.repository.AccountRepository;
import test.bankapplication.repository.UserRepository;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AccountService {
    private UserRepository userRepository;
    private AccountRepository accountRepository;
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
      return toAccountResponse(savedAccount);

  }

  public AccountResponseDTO getAccountDetails(String email){
      User user = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("This user does not exist"));
      Account account = accountRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException("This account does not exist"));
      return toAccountResponse(account);
  }

  private String generateAccountNumber(){
      return UUID.randomUUID().toString().substring(0,13);
  }

  private AccountResponseDTO toAccountResponse(Account account){
      AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
      accountResponseDTO.setAccountId(account.getId());
      accountResponseDTO.setUser(toUserDTO(account.getUser()));
      accountResponseDTO.setAccountNumber(account.getAccountNumber());
      accountResponseDTO.setAccountType(account.getAccountType());
      accountResponseDTO.setBalance(account.getBalance());
      return accountResponseDTO;
  }

    private UserDTO toUserDTO(User user){
        return getUserDTO(user);
    }

    @NonNull
    static UserDTO getUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setDOB(user.getDOB());
        userDTO.setKycStatus(user.getKycStatus());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setRole(user.getRole());
        return userDTO;
    }
}

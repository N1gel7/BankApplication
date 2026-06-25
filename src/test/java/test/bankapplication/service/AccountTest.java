package test.bankapplication.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.bankapplication.dto.request.AccountRequestDTO;
import test.bankapplication.dto.response.AccountResponseDTO;
import test.bankapplication.entitiy.Account;
import test.bankapplication.entitiy.User;
import test.bankapplication.enums.AccountType;
import test.bankapplication.enums.KycStatus;
import test.bankapplication.enums.UserRole;
import test.bankapplication.exception.ResourceNotFoundException;
import test.bankapplication.repository.AccountRepository;
import test.bankapplication.repository.UserRepository;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountService accountService;
    private Account account;
    private AccountRequestDTO accountRequestDTO;
    private User user;


    @BeforeEach
    public void setUp(){
        user = new User();
        user = new User();
        user.setId(1);
        user.setFirstName("Nigel");
        user.setLastName("Peck");
        user.setDOB(LocalDate.of(2005,10,5));
        user.setEmail("nigel@gmail.com");
        user.setPassword("password");
        user.setPhoneNumber("+233444444444");
        user.setKycStatus(KycStatus.APPROVED);
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(UserRole.CUSTOMER);

        account = new Account();
        account.setId(1);
        account.setUser(user);
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(BigDecimal.valueOf(1000));

        accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setAccountType(AccountType.SAVINGS);

    }

    @Test
    public void testCreateAccount_ValidUser_Success(){
        when(userRepository.findByEmail("nigel@gmail.com")).thenReturn(Optional.of(user));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        AccountResponseDTO result = accountService.createAccount("nigel@gmail.com",accountRequestDTO);
        assertThat(result).isNotNull();
        assertThat(result.getAccountType()).isEqualTo(AccountType.SAVINGS);
        verify(accountRepository,times(1)).save(any(Account.class));
    }
    @Test
    public void testCreateAccount_InvalidUser_ThrowsException(){
        when(userRepository.findByEmail("nigel@gmail.com")).thenReturn(Optional.empty());
        assertThatThrownBy(()-> accountService.createAccount("nigel@gmail.com",accountRequestDTO)).isInstanceOf(ResourceNotFoundException.class).hasMessage("This user does not Exist");
    }
    


}

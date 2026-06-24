package test.bankapplication.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.bankapplication.dto.request.DepositRequestDTO;
import test.bankapplication.dto.request.TransferRequestDTO;
import test.bankapplication.entitiy.Account;
import test.bankapplication.entitiy.Transaction;
import test.bankapplication.entitiy.User;
import test.bankapplication.enums.AccountType;
import test.bankapplication.enums.KycStatus;
import test.bankapplication.enums.UserRole;
import test.bankapplication.repository.AccountRepository;
import test.bankapplication.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class TransactionTest {
    @Mock
    private Transaction transaction;
    @Mock
    private Account account;
    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;
    private Account sender;
    private Account receiver;
    private User user;
    private User user2;
    private TransferRequestDTO transferRequestDTO;
    private DepositRequestDTO depositRequestDTO;
    

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


        user2 = new User();
        user2.setId(2);
        user2.setFirstName("Papa");
        user2.setLastName("Nortey");
        user2.setDOB(LocalDate.of(2005,10,5));
        user2.setEmail("papa@gmail.com");
        user2.setPassword("password");
        user2.setPhoneNumber("+233444444444");
        user2.setKycStatus(KycStatus.APPROVED);
        user2.setCreatedAt(LocalDateTime.now());
        user2.setRole(UserRole.CUSTOMER);

        sender = new Account();
        receiver = new Account();
        sender.setId(1);
        sender.setUser(user);
        sender.setAccountNumber("1111111111");
        sender.setAccountType(AccountType.SAVINGS);
        sender.setBalance(BigDecimal.valueOf(1000));
        receiver.setId(2);
        receiver.setUser(user2);
        receiver.setAccountNumber("2222222222");
        receiver.setAccountType(AccountType.SAVINGS);
        receiver.setBalance(BigDecimal.valueOf(1000));

        transferRequestDTO.setAmount(BigDecimal.valueOf(100));
        transferRequestDTO.setAccountNumber("2222222222");

        depositRequestDTO.setAmount(BigDecimal.valueOf(100));
        depositRequestDTO.setAccountNumber("2222222222");
    }






}

package test.bankapplication.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.bankapplication.dto.request.DepositRequestDTO;
import test.bankapplication.dto.request.TransferRequestDTO;
import test.bankapplication.dto.response.TransactionResponseDTO;
import test.bankapplication.entity.Account;
import test.bankapplication.entity.Transaction;
import test.bankapplication.entity.User;
import test.bankapplication.enums.*;
import test.bankapplication.exception.ResourceNotFoundException;
import test.bankapplication.repository.AccountRepository;
import test.bankapplication.repository.TransactionRepository;
import static org.assertj.core.api.Assertions.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionTest {
    private Transaction transactionTransfer;
    private Transaction transactionDeposit;
    @Mock
    private Account account;
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

        transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.setAmount(BigDecimal.valueOf(100));
        transferRequestDTO.setAccountNumber("2222222222");

        depositRequestDTO = new DepositRequestDTO();
        depositRequestDTO.setAmount(BigDecimal.valueOf(100));
        depositRequestDTO.setAccountNumber("2222222222");

        transactionService = new TransactionService(
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(0.02),
                transactionRepository,
                accountRepository
        );

        transactionTransfer = new Transaction();
        transactionTransfer.setId(1);
        transactionTransfer.setSender(sender);
        transactionTransfer.setReceiver(receiver);
        transactionTransfer.setAmount(transferRequestDTO.getAmount());
        transactionTransfer.setTransactionType(TransactionType.TRANSFER);
        transactionTransfer.setFee(BigDecimal.valueOf(10));
        transactionTransfer.setTransactionStatus(TransactionStatus.COMPLETED);

        transactionDeposit = new Transaction();
        transactionDeposit.setId(2);
        transactionDeposit.setReceiver(receiver);
        transactionDeposit.setAmount(depositRequestDTO.getAmount());
        transactionDeposit.setTransactionType(TransactionType.DEPOSIT);
        transactionDeposit.setFee(BigDecimal.ZERO);
        transactionDeposit.setTransactionStatus(TransactionStatus.COMPLETED);
    }

    @Test
    public void testTransaction_ValidAccount_Success(){
        when(accountRepository.findByUserEmail("nigel@gmail.com")).thenReturn(Optional.of(sender));
        when(accountRepository.findByAccountNumber("2222222222")).thenReturn(Optional.of(receiver));
        when(accountRepository.save(sender)).thenReturn(sender);
        when(accountRepository.save(receiver)).thenReturn(receiver);


        TransactionResponseDTO result = transactionService.transfer(transferRequestDTO,"nigel@gmail.com");
        assertThat(result).isNotNull();
        assertThat(result.getTransactionStatus()).isEqualTo(TransactionStatus.COMPLETED);

    }
    @Test
    public void testTransfer_InvalidAccount_ThrowsError(){
        when(accountRepository.findByUserEmail("nigel@gmail.com")).thenReturn(Optional.of(sender));
        when(accountRepository.findByAccountNumber("2222222222")).thenReturn(Optional.empty());
        assertThatThrownBy(()-> transactionService.transfer(transferRequestDTO,"nigel@gmail.com")).isInstanceOf(ResourceNotFoundException.class).hasMessage("Receiver account not found");


    }

    @Test
    public void testDeposit_ValidAccount_Success(){
        when(accountRepository.findByAccountNumber("2222222222")).thenReturn(Optional.of(receiver));
        TransactionResponseDTO result = transactionService.deposit(depositRequestDTO);
        assertThat(result).isNotNull();
        assertThat(result.getTransactionStatus()).isEqualTo(TransactionStatus.COMPLETED);
    }

    @Test
    public void testDeposit_InvalidAccount_throwsException(){
        when(accountRepository.findByAccountNumber("2222222222")).thenReturn(Optional.empty());
        assertThatThrownBy(()-> transactionService.deposit(depositRequestDTO)).isInstanceOf(ResourceNotFoundException.class).hasMessage("Account not found");
    }






}

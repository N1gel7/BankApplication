package test.bankapplication.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import test.bankapplication.dto.mapper.TransactionMapper;
import test.bankapplication.dto.request.DepositRequestDTO;
import test.bankapplication.dto.request.TransferRequestDTO;
import test.bankapplication.dto.response.TransactionResponseDTO;
import test.bankapplication.entity.Account;
import test.bankapplication.entity.Transaction;
import test.bankapplication.enums.TransactionStatus;
import test.bankapplication.enums.TransactionType;
import test.bankapplication.exception.InsufficientFundsException;
import test.bankapplication.exception.InvalidAmountException;
import test.bankapplication.exception.ResourceNotFoundException;
import test.bankapplication.exception.TransferLimitExceededException;
import test.bankapplication.repository.AccountRepository;
import test.bankapplication.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TransactionService {
    private final BigDecimal transferLimit;
    private final BigDecimal minimumBalance;
    private final BigDecimal transferFee;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private  EmailService emailService;

    public TransactionService(
            @Value("${account.transfer.limit}") BigDecimal transferLimit,
            @Value("${account.balance.limit}") BigDecimal minimumBalance,
            @Value("${account.transfer.fee}") BigDecimal transferFee,
            TransactionRepository transactionRepository,
            AccountRepository accountRepository, EmailService emailService) {
        this.transferLimit = transferLimit;
        this.minimumBalance = minimumBalance;
        this.transferFee = transferFee;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.emailService = emailService;
    }



    public TransactionResponseDTO transfer(TransferRequestDTO transferRequestDTO, String email) {
        Account sender = accountRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        Account receiver = accountRepository.findByAccountNumber(transferRequestDTO.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver account not found"));

        if (sender.getAccountNumber().equals(receiver.getAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer money to your own account");
        }

        if (transferRequestDTO.getAmount().compareTo(transferLimit) > 0) {
            throw new TransferLimitExceededException("Exceeds transfer limit");
        }
        if (transferRequestDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }

        BigDecimal fee = transferRequestDTO.getAmount().multiply(transferFee);
        BigDecimal totalDeducted = transferRequestDTO.getAmount().add(fee);

        if (sender.getBalance().compareTo(totalDeducted) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        sender.setBalance(sender.getBalance().subtract(totalDeducted));
        receiver.setBalance(receiver.getBalance().add(transferRequestDTO.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(transferRequestDTO.getAmount());
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setFee(fee);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);
        
        try {
            emailService.sendTransferNotification(sender.getUser().getEmail(), transferRequestDTO.getAmount());
            emailService.sendDepositNotification(receiver.getUser().getEmail(), transferRequestDTO.getAmount());
        } catch (Exception e) {
            System.err.println("Warning: Failed to send email notifications. Please check your Gmail App Password. Transfer was still successful.");
        }
        
        return TransactionMapper.toTransactionResponse(transaction, totalDeducted);
    }

    public TransactionResponseDTO deposit(DepositRequestDTO depositRequestDTO) {
        Account account = accountRepository.findByAccountNumber(depositRequestDTO.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (depositRequestDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }

        account.setBalance(account.getBalance().add(depositRequestDTO.getAmount()));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setReceiver(account);
        transaction.setAmount(depositRequestDTO.getAmount());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setFee(BigDecimal.ZERO);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);

        return TransactionMapper.toTransactionResponse(transaction, depositRequestDTO.getAmount());
    }

    public Page<TransactionResponseDTO> getMyTransactions(String email, Pageable pageable){
        Page<Transaction> transactions = transactionRepository.findAllTransactionsByEmail(email,pageable);
        return transactions.map(t->TransactionMapper.toTransactionResponse(t,t.getAmount().add(t.getFee())));

    }

    public Page<TransactionResponseDTO> getAllTransactions(Integer userId, Pageable pageable){
        Page<Transaction> transaction;
        if (userId != null) {
           transaction = transactionRepository.findAllTransactionsByUserId(userId,pageable);
        }
        else{
            transaction = transactionRepository.findAll(pageable);
        }

        return transaction.map(t->TransactionMapper.toTransactionResponse(t, t.getAmount().add(t.getFee())))
    ;

    }

}


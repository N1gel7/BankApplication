package test.bankapplication.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import test.bankapplication.dto.request.DepositRequestDTO;
import test.bankapplication.dto.request.TransferRequestDTO;
import test.bankapplication.dto.response.TransactionResponseDTO;
import test.bankapplication.entitiy.Account;
import test.bankapplication.entitiy.Transaction;
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

@Service
@Transactional
public class TransactionService {
    private final BigDecimal transferLimit;
    private final BigDecimal minimumBalance;
    private final BigDecimal transferFee;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(
            @Value("${account.transfer.limit}") BigDecimal transferLimit,
            @Value("${account.balance.limit}") BigDecimal minimumBalance,
            @Value("${account.transfer.fee}") BigDecimal transferFee,
            TransactionRepository transactionRepository,
            AccountRepository accountRepository) {
        this.transferLimit = transferLimit;
        this.minimumBalance = minimumBalance;
        this.transferFee = transferFee;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }



    public TransactionResponseDTO transfer(TransferRequestDTO transferRequestDTO,Integer senderId){
        Account sender = accountRepository.findById(senderId).orElseThrow(()->new ResourceNotFoundException("This Account Does not exist"));
        Account receiver = accountRepository.findByAccountNumber(transferRequestDTO.getAccountNumber()).orElseThrow(()-> new ResourceNotFoundException("This Account does not exist"));

        if(transferRequestDTO.getAmount().compareTo(transferLimit)>0){
            throw  new TransferLimitExceededException("Exceeds Daily transfer Limit");
        }
        if(transferRequestDTO.getAmount().compareTo(minimumBalance)<0){
            throw  new InsufficientFundsException("Insufficient Funds");
        }

        BigDecimal fee = transferRequestDTO.getAmount().multiply(transferFee);
        BigDecimal totalDeducted = transferRequestDTO.getAmount().add(fee);

        if(sender.getBalance().compareTo(totalDeducted)<0){
            throw new InsufficientFundsException("Insufficient Funds");
        }

        sender.setBalance(sender.getBalance().subtract(totalDeducted));
        receiver.setBalance(receiver.getBalance().add(transferRequestDTO.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction transaction = new Transaction();
        transaction.setReceiver(receiver);
        transaction.setSender(sender);
        transaction.setAmount(transferRequestDTO.getAmount());
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setFee(fee);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);
        return toTransactionResponse(transaction);
    }

    public TransactionResponseDTO deposit(DepositRequestDTO depositRequestDTO){
        Account account = accountRepository.findByAccountNumber(depositRequestDTO.getAccountNumber()).orElseThrow(()-> new ResourceNotFoundException("This Account does not exist"));
        if(depositRequestDTO.getAmount().compareTo(BigDecimal.ZERO)<= 0){
            throw new InvalidAmountException("The number must be positive");
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

        return toTransactionResponse(transaction);


    }

    private TransactionResponseDTO toTransactionResponse(Transaction transaction){
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
        transactionResponseDTO.setId(transaction.getId());
        transactionResponseDTO.setAmount(transaction.getAmount());
        transactionResponseDTO.setFee(transaction.getFee());
        transactionResponseDTO.setTransactionStatus(transaction.getTransactionStatus());
        return transactionResponseDTO;

    }

}

package test.bankapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import test.bankapplication.entitiy.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
    @Query("SELECT t from Transaction where t.sender.user.email = :email OR t.receiver.user = :email")
    List<Transaction> findAllTransactionsByEmail(@Param("email") String email);

    @Query("SELECT t FROM Transaction t WHERE t.sender.user.id = :userId OR t.receiver.user.id = :userId")
    List<Transaction> findAllTransactionsByUserId(@Param("userId") Integer userId);
}

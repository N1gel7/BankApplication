package test.bankapplication.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import test.bankapplication.entity.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
    @Query("SELECT t FROM Transaction t WHERE t.sender.user.email = :email OR t.receiver.user.email = :email")
    Page<Transaction> findAllTransactionsByEmail(@Param("email") String email, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.sender.user.id = :userId OR t.receiver.user.id = :userId")
    Page<Transaction> findAllTransactionsByUserId(@Param("userId") Integer userId,Pageable pageable);
}

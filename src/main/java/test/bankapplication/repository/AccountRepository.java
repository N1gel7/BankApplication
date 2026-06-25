package test.bankapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.bankapplication.entitiy.Account;
import test.bankapplication.entitiy.User;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Integer> {

    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findByUser(User user);

    Optional<Account> findByUserEmail(String email);
}

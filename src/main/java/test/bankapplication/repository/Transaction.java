package test.bankapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Transaction extends JpaRepository<Transaction,Integer> {
}

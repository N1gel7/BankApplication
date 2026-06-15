package test.bankapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KycDocument extends JpaRepository<test.bankapplication.entitiy.KycDocument,Integer> {
}

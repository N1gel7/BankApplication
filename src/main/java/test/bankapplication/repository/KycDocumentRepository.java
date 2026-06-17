package test.bankapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.bankapplication.entitiy.KycDocument;
import test.bankapplication.enums.DocumentStatus;

import java.util.List;
import java.util.Optional;

public interface KycDocumentRepository extends JpaRepository<test.bankapplication.entitiy.KycDocument,Integer> {
    List<KycDocument> findByStatus(DocumentStatus pending);
}

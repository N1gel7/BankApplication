package test.bankapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.bankapplication.entity.KycDocument;
import test.bankapplication.enums.DocumentStatus;

import java.util.List;

public interface KycDocumentRepository extends JpaRepository<test.bankapplication.entity.KycDocument,Integer> {
    List<KycDocument> findByStatus(DocumentStatus pending);
}

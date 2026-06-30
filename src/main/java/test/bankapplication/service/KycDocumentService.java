package test.bankapplication.service;

import org.springframework.stereotype.Service;
import test.bankapplication.entity.KycDocument;
import test.bankapplication.entity.User;
import test.bankapplication.enums.DocumentStatus;
import test.bankapplication.enums.KycStatus;
import test.bankapplication.exception.ResourceNotFoundException;
import test.bankapplication.repository.KycDocumentRepository;
import test.bankapplication.repository.UserRepository;

import java.util.List;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class KycDocumentService {
    private final UserRepository userRepository;
    private final KycDocumentRepository kycDocumentRepository;

    public KycDocumentService(UserRepository userRepository, KycDocumentRepository kycDocumentRepository) {
        this.userRepository = userRepository;
        this.kycDocumentRepository = kycDocumentRepository;
    }

    @Transactional
    public void submitKyc(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("This user does not exist"));

        KycDocument kycDocument = new KycDocument();
        kycDocument.setUser(user);
        kycDocument.setStatus(DocumentStatus.PENDING);
        kycDocumentRepository.save(kycDocument);
        
        user.setKycStatus(KycStatus.PENDING);
        userRepository.save(user);
    }

    @Transactional
    public void adminApproveKyc(Integer docId){
        KycDocument kycDocument = kycDocumentRepository.findById(docId).orElseThrow(()-> new ResourceNotFoundException("Document not found"));
        kycDocument.setStatus(DocumentStatus.APPROVED);
        kycDocumentRepository.save(kycDocument);

        User user = kycDocument.getUser();
        user.setKycStatus(KycStatus.APPROVED);
        userRepository.save(user);
    }

    @Transactional
    public void adminRejectKyc(Integer docId, String rejectionMessage){
        KycDocument kycDocument = kycDocumentRepository.findById(docId).orElseThrow(()-> new ResourceNotFoundException("This document does not exist"));
        kycDocument.setStatus(DocumentStatus.REJECTED);
        kycDocument.setRejectionMessage(rejectionMessage);

        User user = kycDocument.getUser();
        user.setKycStatus(KycStatus.REJECTED);
        userRepository.save(user);

    }

    public List<KycDocument> getAllPendingDocuments(){
        return kycDocumentRepository.findByStatus(DocumentStatus.PENDING);
    }

}

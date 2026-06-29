package test.bankapplication.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.bankapplication.entity.KycDocument;
import test.bankapplication.entity.User;
import test.bankapplication.enums.DocumentStatus;
import test.bankapplication.enums.KycStatus;
import test.bankapplication.enums.UserRole;
import test.bankapplication.exception.ResourceNotFoundException;
import test.bankapplication.repository.KycDocumentRepository;
import test.bankapplication.repository.UserRepository;
import static org.assertj.core.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KycServiceTest {
    @Mock
    private KycDocument kycDocument;
    @Mock
    private KycDocumentRepository kycDocumentRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private KycDocumentService kycDocumentService;
    private User user;

    @BeforeEach
    public void setUp(){

        user = new User();
        user.setId(1);
        user.setFirstName("Nigel");
        user.setLastName("Peck");
        user.setDOB(LocalDate.of(2005,10,5));
        user.setEmail("nigel@gmail.com");
        user.setPassword("password");
        user.setPhoneNumber("+233444444444");
        user.setKycStatus(KycStatus.PENDING);
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(UserRole.ADMIN);

        kycDocument = new KycDocument();
        kycDocument.setId(1);
        kycDocument.setUser(user);
        kycDocument.setStatus(DocumentStatus.PENDING);




    }

    @Test
    public void testSubmitKyc_ValidId_Success(){
        when(userRepository.findByEmail("nigel@gmail.com")).thenReturn(Optional.of(user));
        when(kycDocumentRepository.save(any(KycDocument.class))).thenReturn(kycDocument);
        kycDocumentService.submitKyc("nigel@gmail.com");
        verify(kycDocumentRepository,times(1)).save(any(KycDocument.class));

    }

    @Test
    public void testSubmitKyc_InvalidId_ThrowsError()
    {
        when(kycDocumentRepository.findById(999)).thenReturn(Optional.empty());
        assertThatThrownBy(()-> kycDocumentService.adminApproveKyc(999)).isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    public void testAdminApproveKyc_ValidId_Success(){
        when(kycDocumentRepository.findById(1)).thenReturn(Optional.of(kycDocument));
        kycDocumentService.adminApproveKyc(1);
        assertThat(kycDocument.getStatus()).isEqualTo(DocumentStatus.APPROVED);
        assertThat(kycDocument.getUser().getKycStatus()).isEqualTo(KycStatus.APPROVED);
    }


    @Test
    public void testAdminApproveKyc_InvalidId_ThrowsException(){
        when(kycDocumentRepository.findById(999)).thenReturn(Optional.empty());
        assertThatThrownBy(()-> kycDocumentService.adminApproveKyc(999)).isInstanceOf(ResourceNotFoundException.class).hasMessage("Document not found");
    }

    @Test
    public void testAdminRejectKyc_ValidId_Success(){
        when(kycDocumentRepository.findById(1)).thenReturn(Optional.of(kycDocument));
        kycDocumentService.adminRejectKyc(1,"Incorrect Ghana card");
        assertThat(kycDocument.getStatus()).isEqualTo(DocumentStatus.REJECTED);
        assertThat(kycDocument.getUser().getKycStatus()).isEqualTo(KycStatus.REJECTED);
    }

    @Test
    public void testAdminRejectKyc_InvalidId_ThrowsException(){
        when(kycDocumentRepository.findById(999)).thenReturn(Optional.empty());
        assertThatThrownBy(()-> kycDocumentService.adminRejectKyc(999,"Invalid Ghana card")).isInstanceOf(ResourceNotFoundException.class).hasMessage("This document does not exist");
    }

    @Test
    public void testGetAllPendingDocuments_Success(){
        when(kycDocumentRepository.findByStatus(DocumentStatus.PENDING)).thenReturn(Collections.singletonList(kycDocument));
        List<KycDocument> result = kycDocumentService.getAllPendingDocuments();
        assertThat(result).isNotNull();
        assertThat(result.get(0).getStatus()).isEqualTo(DocumentStatus.PENDING);


    }

}

package test.bankapplication.controller;

import org.springframework.web.bind.annotation.*;
import test.bankapplication.entitiy.KycDocument;
import test.bankapplication.service.KycDocumentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/kyc")
public class KycController {
    private KycDocumentService kycDocumentService;

    @PostMapping("/submit")
    public void submitKyc(String email){
         kycDocumentService.submitKyc(email);
    }
    @PatchMapping("/{docId}/approve")
    public void approveKyc(Integer docId){
         kycDocumentService.adminApproveKyc(docId);
    }

    @PatchMapping("/{docId}/reject")
  public void rejectKyc(Integer docId,String rejectionMessage){
        kycDocumentService.adminRejectKyc(docId,rejectionMessage);
  }

  @GetMapping("/pending")
  public List<KycDocument> getAllPendingDocument(){
        return kycDocumentService.getAllPendingDocuments();
  }

}

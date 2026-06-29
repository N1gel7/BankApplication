package test.bankapplication.controller;

import org.springframework.web.bind.annotation.*;
import test.bankapplication.entity.KycDocument;
import test.bankapplication.service.KycDocumentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/kyc")
public class KycController {
    private final KycDocumentService kycDocumentService;

    public KycController(KycDocumentService kycDocumentService) {
        this.kycDocumentService = kycDocumentService;
    }

    @PostMapping("/submit")
    public void submitKyc(java.security.Principal principal){
         kycDocumentService.submitKyc(principal.getName());
    }
    @PatchMapping("/{docId}/approve")
    public void approveKyc(@PathVariable("docId") Integer docId){
         kycDocumentService.adminApproveKyc(docId);
    }

    @PatchMapping("/{docId}/reject")
  public void rejectKyc(@PathVariable("docId") Integer docId, @RequestParam(required = false) String rejectionMessage){
        kycDocumentService.adminRejectKyc(docId,rejectionMessage);
  }

  @GetMapping("/pending")
  public List<KycDocument> getAllPendingDocument(){
        return kycDocumentService.getAllPendingDocuments();
  }

}

package test.bankapplication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.bankapplication.entity.KycDocument;
import test.bankapplication.service.KycDocumentService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/kyc")
public class KycController {
    private final KycDocumentService kycDocumentService;

    public KycController(KycDocumentService kycDocumentService) {
        this.kycDocumentService = kycDocumentService;
    }

    @PostMapping("/submit")
    public ResponseEntity<Void> submitKyc(Principal principal){
         kycDocumentService.submitKyc(principal.getName());
         return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{docId}/approve")
    public ResponseEntity<Void> approveKyc(@PathVariable("docId") Integer docId){
         kycDocumentService.adminApproveKyc(docId);
         return ResponseEntity.ok().build();
    }

    @PatchMapping("/{docId}/reject")
  public ResponseEntity<Void> rejectKyc(@PathVariable("docId") Integer docId, @RequestParam(required = false) String rejectionMessage){
        kycDocumentService.adminRejectKyc(docId,rejectionMessage);
        return ResponseEntity.ok().build();
  }

  @GetMapping("/pending")
  public ResponseEntity<List<KycDocument>> getAllPendingDocument(){
        return ResponseEntity.ok(kycDocumentService.getAllPendingDocuments());
  }

}

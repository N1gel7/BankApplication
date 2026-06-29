package test.bankapplication.entity;

import jakarta.persistence.*;
import test.bankapplication.enums.DocumentStatus;

@Entity
public class KycDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToOne
    private User user;
    @Enumerated(EnumType.STRING)
    private DocumentStatus status;
    private String rejectionMessage;


    public KycDocument() {
    }

    public KycDocument(User user, String rejectionMessage) {
        this.user = user;
        this.rejectionMessage = rejectionMessage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public String getRejectionMessage() {
        return rejectionMessage;
    }

    public void setRejectionMessage(String rejectionMessage) {
        this.rejectionMessage = rejectionMessage;
    }
}

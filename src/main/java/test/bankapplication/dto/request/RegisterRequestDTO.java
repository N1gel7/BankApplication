package test.bankapplication.dto.request;

import test.bankapplication.enums.KycStatus;
import test.bankapplication.enums.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RegisterRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate DOB;
    private KycStatus kycStatus;
    private LocalDateTime createdAt;
    private UserRole role;

    public RegisterRequestDTO() {
    }

    public RegisterRequestDTO(String firstName, String lastName, String email, LocalDate DOB) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.DOB = DOB;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDOB() {
        return DOB;
    }

    public void setDOB(LocalDate DOB) {
        this.DOB = DOB;
    }

    public KycStatus getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(KycStatus kycStatus) {
        this.kycStatus = kycStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}

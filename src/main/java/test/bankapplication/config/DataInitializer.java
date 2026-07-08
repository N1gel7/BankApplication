package test.bankapplication.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import test.bankapplication.entity.User;
import test.bankapplication.enums.KycStatus;
import test.bankapplication.enums.UserRole;
import test.bankapplication.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByEmail("admin@bank.com")) {
                User admin = new User();
                admin.setFirstName("Admin");
                admin.setLastName("User");
                admin.setEmail("admin@bank.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setPhoneNumber("1234567890");
                admin.setDOB(LocalDate.of(1990, 1, 1));
                admin.setKycStatus(KycStatus.APPROVED);
                admin.setRole(UserRole.ADMIN);
                admin.setCreatedAt(LocalDateTime.now());

                userRepository.save(admin);
                System.out.println("");
                System.out.println("");
                System.out.println("");
            }
        };
    }
}

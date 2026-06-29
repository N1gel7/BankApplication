package test.bankapplication.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import test.bankapplication.dto.request.AdminCreateRequestDTO;
import test.bankapplication.dto.request.AuthRequestDTO;
import test.bankapplication.dto.request.RegisterRequestDTO;
import test.bankapplication.dto.response.AuthResponseDTO;
import test.bankapplication.dto.response.RegisterResponseDTO;
import test.bankapplication.entity.User;
import test.bankapplication.enums.KycStatus;
import test.bankapplication.enums.UserRole;
import test.bankapplication.exception.DuplicateUserException;
import test.bankapplication.repository.UserRepository;
import test.bankapplication.security.JwtUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthService authService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    private RegisterRequestDTO registerRequestDTO;
    private AuthRequestDTO authRequestDTO;
    private User testUser;
    private AdminCreateRequestDTO adminCreateRequestDTO;
    private User adminUser ;

    @BeforeEach
    public void SetUp(){
        testUser = new User();
        testUser.setId(1);
        testUser.setFirstName("Nigel");
        testUser.setLastName("Peck");
        testUser.setDOB(LocalDate.of(2005,10,5));
        testUser.setEmail("nigel@gmail.com");
        testUser.setPassword("password123");
        testUser.setPhoneNumber("+233543249807");
        testUser.setKycStatus(KycStatus.APPROVED);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setRole(UserRole.CUSTOMER);

        registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setFirstName("Nigel");
        registerRequestDTO.setLastName("Peck");
        registerRequestDTO.setPassword("password123");
        registerRequestDTO.setEmail("nigel@gmail.com");
        registerRequestDTO.setDOB(LocalDate.of(2005,10,5));
        registerRequestDTO.setPhoneNumber("+233555555555");
        authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setEmail("nigel@gmail.com");
        authRequestDTO.setPassword("password123");


        adminUser = new User();
        adminUser.setId(2);
        adminUser.setFirstName("admin");
        adminUser.setLastName("1");
        adminUser.setDOB(LocalDate.of(2005,10,5));
        adminUser.setEmail("admin@gmail.com");
        adminUser.setPassword("admin123");
        adminUser.setPhoneNumber("+233543249807");
        adminUser.setKycStatus(KycStatus.PENDING);
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setRole(UserRole.ADMIN);

        adminCreateRequestDTO = new AdminCreateRequestDTO();
        adminCreateRequestDTO.setFirstName("admin");
        adminCreateRequestDTO.setLastName("1");
        adminCreateRequestDTO.setDOB(LocalDate.of(2005,10,5));
        adminCreateRequestDTO.setEmail("admin@gmail.com");
        adminCreateRequestDTO.setPassword("admin123");
        adminCreateRequestDTO.setPhoneNumber("+233543249807");
    }
    @Test
    public void testRegister_ValidUser_Success(){
        when(userRepository.existsByEmail("nigel@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");

        RegisterResponseDTO result = authService.register(registerRequestDTO);
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Nigel");
        assertThat(result.getLastName()).isEqualTo("Peck");
        assertThat(result.getEmail()).isEqualTo("nigel@gmail.com");
        assertThat(result.getRole()).isEqualTo(UserRole.CUSTOMER);
        assertThat(result.getKycStatus()).isEqualTo(KycStatus.PENDING);
        verify(userRepository,times(1)).save(any(User.class));
    }

    @Test
    public void testRegister_ExistingUser_ThrowsException(){
        when(userRepository.existsByEmail("nigel@gmail.com")).thenReturn(true);
        assertThatThrownBy(()-> authService.register(registerRequestDTO)).isInstanceOf(DuplicateUserException.class).hasMessage("This user exists");
    }

    @Test
    public void testCreateAdmin_ValidUser_Success(){
        when(userRepository.existsByEmail("admin@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("admin123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(adminUser);

        AuthResponseDTO result = authService.createAdmin(adminCreateRequestDTO);
        assertThat(result).isNotNull();
        assertThat(result.getUserDTO().getEmail()).isEqualTo("admin@gmail.com");
        assertThat(result.getUserDTO().getPhoneNumber()).isEqualTo("+233543249807");
        assertThat(result.getUserDTO().getKycStatus()).isEqualTo(KycStatus.PENDING);
        assertThat(result.getUserDTO().getRole()).isEqualTo(UserRole.ADMIN);
        verify(userRepository,times(1)).save(any(User.class));
    }

    @Test
    public void testCreateAdmin_ExistingUser_ThrowsException(){
        when(userRepository.existsByEmail("admin@gmail.com")).thenReturn(true);
        assertThatThrownBy(()-> authService.createAdmin(adminCreateRequestDTO)).isInstanceOf(DuplicateUserException.class).hasMessage("This user exists");
    }

    @Test
    public void testLogin_ValidUser_Success(){
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(new UsernamePasswordAuthenticationToken("nigel@gmail.com","password123"));
        when(userRepository.findByEmail("nigel@gmail.com")).thenReturn(Optional.of(testUser));


        AuthResponseDTO result = authService.login(authRequestDTO);
        assertThat(result).isNotNull();
        assertThat(result.getUserDTO().getEmail()).isEqualTo("nigel@gmail.com");
        assertThat(result.getUserDTO().getRole()).isEqualTo(UserRole.CUSTOMER);
        verify(userRepository,times(1)).findByEmail("nigel@gmail.com");
    }

    @Test
    public void testLogin_InvalidUser_ThrowsException(){
        when(userRepository.findByEmail("nigel@gmail.com")).thenReturn(Optional.empty());
        assertThatThrownBy(()-> authService.login(authRequestDTO)).isInstanceOf(UsernameNotFoundException.class);
    }









}

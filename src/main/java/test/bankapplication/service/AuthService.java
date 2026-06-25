package test.bankapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import test.bankapplication.dto.request.AdminCreateRequestDTO;
import test.bankapplication.dto.request.AuthRequestDTO;
import test.bankapplication.dto.request.RegisterRequestDTO;
import test.bankapplication.dto.response.AuthResponseDTO;
import test.bankapplication.dto.response.RegisterResponseDTO;
import test.bankapplication.dto.response.UserDTO;
import test.bankapplication.entitiy.User;
import test.bankapplication.enums.KycStatus;
import test.bankapplication.enums.UserRole;
import test.bankapplication.exception.DuplicateUserException;
import test.bankapplication.repository.UserRepository;
import test.bankapplication.security.JwtUtil;

import java.time.LocalDateTime;

import static test.bankapplication.service.AccountService.getUserDTO;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO){
       if(userRepository.existsByEmail(registerRequestDTO.getEmail())){
           throw new DuplicateUserException("This user exists");
       }
        User user = new User();
        user.setFirstName(registerRequestDTO.getFirstName());
        user.setLastName(registerRequestDTO.getLastName());
        user.setEmail(registerRequestDTO.getEmail());
        user.setDOB(registerRequestDTO.getDOB());
        user.setPhoneNumber(registerRequestDTO.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setKycStatus(KycStatus.PENDING);
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(UserRole.CUSTOMER);
        userRepository.save(user);
        return toRegisterResponse(user);
    }

    public AuthResponseDTO createAdmin(AdminCreateRequestDTO adminCreateRequestDTO){
        if(userRepository.existsByEmail(adminCreateRequestDTO.getEmail())){
            throw new DuplicateUserException("This user exists");
        }
        User user = new User();
        user.setFirstName(adminCreateRequestDTO.getFirstName());
        user.setLastName(adminCreateRequestDTO.getLastName());
        user.setDOB(adminCreateRequestDTO.getDOB());
        user.setPassword(passwordEncoder.encode(adminCreateRequestDTO.getPassword()));
        user.setEmail(adminCreateRequestDTO.getEmail());
        user.setPhoneNumber(adminCreateRequestDTO.getPhoneNumber());
        user.setKycStatus(KycStatus.PENDING);
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(UserRole.ADMIN);
        User savedUser = userRepository.save(user);
        return toAuthResponse(savedUser);
    }


    public AuthResponseDTO login(AuthRequestDTO authRequestDTO){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(),authRequestDTO.getPassword()));
        User user = userRepository.findByEmail(authRequestDTO.getEmail()).orElseThrow(()-> new UsernameNotFoundException("This User does not exist"));
        return toAuthResponse(user);
    }


    private RegisterResponseDTO toRegisterResponse(User user){
        RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO();
                registerResponseDTO.setId(user.getId());
                registerResponseDTO.setFirstName(user.getFirstName());
                registerResponseDTO.setLastName(user.getLastName());
                registerResponseDTO.setEmail(user.getEmail());
                registerResponseDTO.setDOB(user.getDOB());
                registerResponseDTO.setKycStatus(user.getKycStatus());
                registerResponseDTO.setCreatedAt(user.getCreatedAt());
                registerResponseDTO.setRole( user.getRole());
                return registerResponseDTO;
    }


    private UserDTO toUserDTO(User user){
        return getUserDTO(user);
    }
    private AuthResponseDTO toAuthResponse(User user){
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        UserDTO userDTO = toUserDTO(user);
        authResponseDTO.setUserDTO(userDTO);
        return authResponseDTO;
    }



}

package test.bankapplication.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.bankapplication.dto.request.AdminCreateRequestDTO;
import test.bankapplication.dto.request.AuthRequestDTO;
import test.bankapplication.dto.request.RegisterRequestDTO;
import test.bankapplication.dto.response.AuthResponseDTO;
import test.bankapplication.dto.response.RegisterResponseDTO;
import test.bankapplication.dto.response.UserDTO;
import test.bankapplication.exception.RateLimitException;
import test.bankapplication.security.JwtUtil;
import test.bankapplication.service.AuthService;
import test.bankapplication.service.RateLimitingService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final RateLimitingService rateLimitingService;

    public AuthController(AuthService authService, JwtUtil jwtUtil, RateLimitingService rateLimitingService) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.rateLimitingService = rateLimitingService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response, HttpServletRequest request){
        String ipAddress = request.getRemoteAddr();
        String email = authRequestDTO.getEmail();

        if(!rateLimitingService.resolveLoginIpBucket(ipAddress).tryConsume(1)){
            throw new RateLimitException("Too many login attempts try in 15 minutes");
        }
        if(!rateLimitingService.resolveLoginEmailBucket(email).tryConsume(1)){
            throw new RateLimitException("Too many login attempts try again in 15 minutes");
        }

        AuthResponseDTO authResponseDTO = authService.login(authRequestDTO);
        UserDTO user = authResponseDTO.getUserDTO();
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        Cookie cookie = new Cookie("jwt" , token);

        cookie.setMaxAge(60 * 60 * 24);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok(authResponseDTO);
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response){
        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<AuthResponseDTO> createAdmin(@Valid @RequestBody AdminCreateRequestDTO adminCreateRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body( authService.createAdmin(adminCreateRequestDTO));
    }

}

package test.bankapplication.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import test.bankapplication.dto.request.AdminCreateRequestDTO;
import test.bankapplication.dto.request.AuthRequestDTO;
import test.bankapplication.dto.request.RegisterRequestDTO;
import test.bankapplication.dto.response.AuthResponseDTO;
import test.bankapplication.dto.response.RegisterResponseDTO;
import test.bankapplication.dto.response.UserDTO;
import test.bankapplication.security.JwtUtil;
import test.bankapplication.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponseDTO register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO){
        return authService.register(registerRequestDTO);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDTO login(@RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response){
        AuthResponseDTO authResponseDTO = authService.login(authRequestDTO);
        UserDTO user = authResponseDTO.getUserDTO();
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        Cookie cookie = new Cookie("jwt" , token);

        cookie.setMaxAge(60 * 60 * 24);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return authResponseDTO;
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
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDTO createAdmin(@Valid @RequestBody AdminCreateRequestDTO adminCreateRequestDTO){
        return authService.createAdmin(adminCreateRequestDTO);
    }

}

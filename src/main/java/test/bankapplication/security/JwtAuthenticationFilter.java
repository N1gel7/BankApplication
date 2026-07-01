package test.bankapplication.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import test.bankapplication.service.CustomerDetailsService;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final CustomerDetailsService customerDetailsService;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(CustomerDetailsService customerDetailsService, JwtUtil jwtUtil) {
        this.customerDetailsService = customerDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String email = jwtUtil.extractEmail(token);

            if (email == null) {
                log.warn("JWT token present but email could not be extracted");
                filterChain.doFilter(request, response);
                return;
            }

            
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customerDetailsService.loadUserByUsername(email);

                if (jwtUtil.isTokenValid(token, email)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    log.debug("Authenticated user: {}", email);
                } else {
                    log.warn("Invalid JWT token for user: {}", email);
                }
            }
        } catch (UsernameNotFoundException ex) {
            log.warn("JWT token references unknown user: {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("Error processing JWT token: {}", ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}


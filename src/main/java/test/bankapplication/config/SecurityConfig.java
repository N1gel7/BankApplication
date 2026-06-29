package test.bankapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import test.bankapplication.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/*.html", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/api/v1/auth/register", "/api/v1/auth/login", "/api/v1/auth/logout").permitAll()
                .requestMatchers("/api/v1/auth/admin/create").hasRole("ADMIN")
                .requestMatchers("/api/v1/kyc/submit").hasRole("CUSTOMER")
                .requestMatchers("/api/v1/kyc/pending", "/api/v1/kyc/{docId}/approve", "/api/v1/kyc/{docId}/reject").hasRole("ADMIN")
                .requestMatchers("/api/v1/accounts/me").hasAnyRole("CUSTOMER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/accounts").hasRole("CUSTOMER")
                .requestMatchers("/api/v1/transactions/transfer", "/api/v1/transactions/deposit", "/api/v1/transactions/me").hasRole("CUSTOMER")
                .requestMatchers(HttpMethod.GET, "/api/v1/transactions").hasRole("ADMIN")
                
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

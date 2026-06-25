package test.bankapplication.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import test.bankapplication.service.CustomerDetailsService;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
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
         if(header != null && header.startsWith("Bearer ")){
             token = header.substring(7);
         }

         if(request.getCookies()!= null ){
             for(Cookie cookie : request.getCookies()){
                 if("jwt".equals(cookie.getName())){
                     token = cookie.getValue();
                     break;
                 }
             }
         }
         if(token == null){
             filterChain.doFilter(request,response);
             return;
         }

         String email = jwtUtil.extractEmail(token);
         UserDetails userDetails = customerDetailsService.loadUserByUsername(email);

         try{
             if(jwtUtil.isTokenValid(token,email)){
                 UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                         userDetails,
                         null,
                         userDetails.getAuthorities()
                 );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
             }
         } catch (UsernameNotFoundException ex) {

         }
         try{
             filterChain.doFilter(request,response);
         }finally{
             SecurityContextHolder.clearContext();
        }
    }
}

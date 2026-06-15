package test.bankapplication.security;

import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import test.bankapplication.enums.UserRole;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("{jwt.expiration}")
    private long expiration;

    public JwtUtil(String secretKey, long expiration) {
        this.secretKey = secretKey;
        this.expiration = expiration;
    }

    public Key getSigningKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    public String extractEmail(String token){
        return extractClaim(token , Claims::getSubject);
    }

    public String generateToken(String email,UserRole role){
        Map<String , Object> claims= new HashMap<>();
        claims.put("role" , role.toString());

       return Jwts.builder().claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValid(String token,String email){
        return extractClaim(token,Claims::getSubject).equals(email) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token){
        return extractClaim(token, claims -> claims.getExpiration().before(new Date(System.currentTimeMillis())));
    }

     private <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey)getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    return claimsResolver.apply(claims);
     }

}

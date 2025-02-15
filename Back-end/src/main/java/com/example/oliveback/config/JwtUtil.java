package com.example.oliveback.config;

//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
//
//    @Value("${jwt.secret}")
//    private String secretKey;
//
//    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24시간
//
//    private SecretKey getSigningKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public String generateToken(String username, String role) {
//        return Jwts.builder()
//                .setSubject(username)  // ✅ .subject() → .setSubject() (JJWT 0.11.5 스타일)
//                .claim("role", role)
//                .setIssuedAt(new Date())  // ✅ .issuedAt() → .setIssuedAt()
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // ✅ .expiration() → .setExpiration()
//                .signWith(getSigningKey())
//                .compact();
//    }
//
//    public Claims extractClaims(String token) {
//        return Jwts.parser()  // ✅ `parserBuilder()` 대신 `parser()` 사용
//                .setSigningKey(getSigningKey())  // ✅ `verifyWith()` 대신 `setSigningKey()` 사용
//                .parseClaimsJws(token)  // ✅ `parseSignedClaims()` 대신 `parseClaimsJws()` 사용
//                .getBody();  // ✅ `getPayload()` 대신 `getBody()` 사용
//    }
//
//    public String extractUsername(String token) {
//        return extractClaims(token).getSubject();
//    }
//
//    public String extractRole(String token) {
//        return extractClaims(token).get("role", String.class);
//    }
//
//    public boolean isTokenValid(String token) {
//        try {
//            return extractClaims(token).getExpiration().after(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }
}



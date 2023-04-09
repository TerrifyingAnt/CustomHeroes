package jg.actionfigures.server.Utils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.http.HttpHeaders;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jg.actionfigures.server.Models.User;

public class JwtUtils {
    
    private static final String JWT_SECRET = "$2a$10$Gl76HlY4KBi/9cCB4RP5suTnPN9yCvlwVCccljYp21RAMgKo2mkJ.";

    public String generateToken(User user) {
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + TimeUnit.HOURS.toMillis(1));

        Claims claims = Jwts.claims().setSubject(user.getLogin());
        claims.put("roles", user.getType());

        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
        
        return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expirationTime)
        .signWith(key)
        .compact();
    }

    // Extract token from the request
    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.replace("Bearer ", "");
        }
        return null;
    }


    // Validate token signature and expiration
    public boolean validateToken(String token) {
        try {
            ((JwtParserBuilder) Jwts.parserBuilder().setSigningKey(JWT_SECRET.getBytes())).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Extract username from the token
    public String extractUsername(String token) {
        Claims claims = ((JwtParserBuilder) Jwts.parserBuilder().setSigningKey(JWT_SECRET.getBytes())).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    // Extract roles from the token
    public List<String> extractRoles(String token) {
        Claims claims = ((JwtParserBuilder) Jwts.parserBuilder().setSigningKey(JWT_SECRET.getBytes())).build().parseClaimsJws(token).getBody();
        String role = claims.get("roles").toString();
        return Collections.singletonList(role);
    }

}

package jg.actionfigures.server.AuthModule.Utils;

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
import jg.actionfigures.server.Models.PostgerSql.User;

public class JwtUtils {
    
    private static final String JWT_SECRET = "$2a$10$Gl76HlY4KBi/9cCB4RP5suTnPN9yCvlwVCccljYp21RAMgKo2mkJ.";
    private static final String JWT_REFRESH_SECRET = "qwrfdsa23rt2./1234\123451341sdgt24gs$*(#HJKujhfsd;)asdrtewf";


    // * Сгенерировать токен
    public String generateToken(User user, TokenEnum tokenEnum) {
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + TimeUnit.DAYS.toMillis(365));

        Claims claims = Jwts.claims().setSubject(user.getLogin());
        claims.put("roles", user.getType());
        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

        switch(tokenEnum){
            case ACCESS:
                key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
                break;

            case REFRESH:
                key = Keys.hmacShaKeyFor(JWT_REFRESH_SECRET.getBytes());
                break;
        }
        
        return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expirationTime)
        .signWith(key)
        .compact();
    }

    // * Получить токен из запроса
    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.replace("Bearer ", "");
        }
        return null;
    }


    // * Проверить токен на валидность
    public boolean validateToken(String token, TokenEnum tokenEnum) {
        Claims claims;
        switch(tokenEnum) { 
            case ACCESS:
                try {
                    claims = ((JwtParserBuilder) Jwts.parserBuilder().setSigningKey(JWT_SECRET.getBytes())).build().parseClaimsJws(token).getBody();
                    Date expiration = claims.getExpiration();
                    return expiration.after(new Date());
                }
                catch (JwtException | IllegalArgumentException e) {
                    return false;
                }
            case REFRESH:
                try {
                    claims = ((JwtParserBuilder) Jwts.parserBuilder().setSigningKey(JWT_REFRESH_SECRET.getBytes())).build().parseClaimsJws(token).getBody();
                    Date expiration = claims.getExpiration();
                    return expiration.after(new Date());
                }
                catch (JwtException | IllegalArgumentException e) {
                    return false;
                }

        }
        return false;
    }



    // * Получить логин из токена
    public String extractUsername(String token, TokenEnum tokenEnum) {
        Claims claims;
        switch(tokenEnum) {
            case ACCESS: 
                claims = ((JwtParserBuilder) Jwts.parserBuilder().setSigningKey(JWT_SECRET.getBytes())).build().parseClaimsJws(token).getBody();
                return claims.getSubject();
            case REFRESH:
                claims = ((JwtParserBuilder) Jwts.parserBuilder().setSigningKey(JWT_REFRESH_SECRET.getBytes())).build().parseClaimsJws(token).getBody();
                return claims.getSubject();
            
        }
        return null;
    }

    // * Получить роли из токена
    public List<String> extractRoles(String token) {
        Claims claims = ((JwtParserBuilder) Jwts.parserBuilder().setSigningKey(JWT_SECRET.getBytes())).build().parseClaimsJws(token).getBody();
        String role = claims.get("roles").toString();
        return Collections.singletonList(role);
    }
}

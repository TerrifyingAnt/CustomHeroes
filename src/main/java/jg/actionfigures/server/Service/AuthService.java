package jg.actionfigures.server.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jg.actionfigures.server.API.UserRepository;
import jg.actionfigures.server.Models.User;

@Service
public class AuthService {

    private static final String JWT_SECRET = "secret_key";
    private static final long JWT_EXPIRATION_MS = 3600000L;
    private static final String REDIS_USER_KEY_PREFIX = "user:";
    private static final long REDIS_USER_EXPIRATION_MS = 86400000L;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public String register(User user) throws Exception {
        if (userRepository.findByLogin(user.getLogin()) != null) {
            throw new Exception("User with the given login already exists.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        String token = generateToken(user.getLogin(), user.getType());
        storeUserInRedis(user, token);

        return token;
    }

    public String login(String login, String password) throws Exception {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new Exception("User with the given login not found.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Invalid password.");
        }

        String token = generateToken(login, user.getType());
        storeUserInRedis(user, token);

        return token;
    }

    public void logout(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        String userLogin = claims.getSubject();
        redisTemplate.delete(REDIS_USER_KEY_PREFIX + userLogin);
    }

    public User getUserFromToken(String token) throws Exception {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        String userLogin = claims.getSubject();

        User user = (User) redisTemplate.opsForValue().get(REDIS_USER_KEY_PREFIX + userLogin);
        if (user == null) {
            throw new Exception("User not found in Redis.");
        }

        return user;
    }

    private String generateToken(String login, String userType) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + JWT_EXPIRATION_MS);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userType", userType);

        return Jwts.builder()
                .setSubject(login)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    private void storeUserInRedis(User user, String token) {
        redisTemplate.opsForValue().set(
                REDIS_USER_KEY_PREFIX + user.getLogin(),
                user,
                REDIS_USER_EXPIRATION_MS,
                TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(
                token,
                user.getLogin(),
                JWT_EXPIRATION_MS,
                TimeUnit.MILLISECONDS);
    }
}
package jg.actionfigures.server.Controllers;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jg.actionfigures.server.API.UserRepository;
import jg.actionfigures.server.DTO.UserLoginRequest;
import jg.actionfigures.server.DTO.UserRegistrationRequest;
import jg.actionfigures.server.Models.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest registrationRequest) {
        // Check if user with same login already exists
        if (userRepository.findByLogin(registrationRequest.getLogin()) != null) {
            return ResponseEntity.badRequest().body("User with the same login already exists");
        }
        
        // Create new user
        User newUser = new User();
        newUser.setName(registrationRequest.getName());
        newUser.setLogin(registrationRequest.getLogin());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        newUser.setPhoneNumber(registrationRequest.getPhoneNumber());
        newUser.setAvatarSourcePath(registrationRequest.getAvatarSourcePath());
        newUser.setType(registrationRequest.getType());
        
        // Save user to database
        userRepository.save(newUser);
        
        // Generate JWT token
        String token = generateToken(newUser);
        
        // Store user and token in Redis
        storeUserInRedis(newUser.getLogin(), token);
        
        return ResponseEntity.ok(token);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        // Check if user with given login exists
        User user = userRepository.findByLogin(userLoginRequest.getLogin());
        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid login or password");
        }
        
        // Check if password matches
        if (!passwordEncoder.matches(userLoginRequest.getLogin(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid login or password");
        }
        
        // Generate JWT token
        String token = generateToken(user);
        
        // Store user and token in Redis
        storeUserInRedis(user.getLogin(), token);
        
        return ResponseEntity.ok(token);
    }
    
    private String generateToken(User user) {
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + TimeUnit.HOURS.toMillis(1));
        
        return Jwts.builder()
            .setSubject(user.getLogin())
            .claim("type", user.getType())
            .setIssuedAt(now)
            .setExpiration(expirationTime)
            .signWith(SignatureAlgorithm.HS512, "secret-key")
            .compact();
    }
    
    private void storeUserInRedis(String login, String token) {
        String userKey = "user:" + login;
        String tokenKey = "token:" + token;
        
        redisTemplate.opsForValue().set(userKey, getUserFromDatabase(login), 24, TimeUnit.HOURS);
        redisTemplate.opsForValue().set(tokenKey, login, 1, TimeUnit.HOURS);
    }
    
    private User getUserFromDatabase(String login) {
        return userRepository.findByLogin(login);
    }
}


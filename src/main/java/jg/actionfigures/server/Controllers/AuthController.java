package jg.actionfigures.server.Controllers;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jg.actionfigures.server.API.UserRepository;
import jg.actionfigures.server.DTO.UserLoginRequest;
import jg.actionfigures.server.DTO.UserRegistrationRequest;
import jg.actionfigures.server.Models.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String JWT_SECRET = "$2a$10$Gl76HlY4KBi/9cCB4RP5suTnPN9yCvlwVCccljYp21RAMgKo2mkJ.";
    
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

        newUser.setType("CUSTOMER");
        
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
            System.out.println(userLoginRequest.getLogin());
            return ResponseEntity.badRequest().body("Invalid login");
        }
        
        // Check if password matches
        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid password");
        }
        
        // Generate JWT token
        String token = generateToken(user);
        
        // Store user and token in Redis
        storeUserInRedis(user.getLogin(), token);
        
        return ResponseEntity.ok(token);
    }
    

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        String authToken = getTokenFromRequest(request);
        if (authToken != null) {
            redisTemplate.delete(authToken);
            redisTemplate.delete(extractUsername(authToken));
            return ResponseEntity.ok("User logged out successfully");
        }
        return(ResponseEntity.badRequest().build());

    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        return ResponseEntity.ok(users);
    }
    
    // Helper method to extract token from request header

    private String getTokenFromRequest(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        String authToken = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authToken = authorizationHeader.replace("Bearer ", "");
        }
        return authToken;
    }


    private String generateToken(User user) {
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
    
    private void storeUserInRedis(String login, String token) {
        String userKey = login;
        String tokenKey = token;
        
        redisTemplate.opsForValue().set(userKey, getUserFromDatabase(login), 24, TimeUnit.HOURS);
        redisTemplate.opsForValue().set(tokenKey, login, 1, TimeUnit.HOURS);
    }
    
    private User getUserFromDatabase(String login) {
        return userRepository.findByLogin(login);
    }

    private String extractUsername(String token) {
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET.getBytes()).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}


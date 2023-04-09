package jg.actionfigures.server.Controllers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jg.actionfigures.server.API.UserRepository;
import jg.actionfigures.server.DTO.RefreshTokenRequest;
import jg.actionfigures.server.DTO.RefreshTokenResponse;
import jg.actionfigures.server.DTO.UserLoginRequest;
import jg.actionfigures.server.DTO.UserRegistrationRequest;
import jg.actionfigures.server.Models.User;
import jg.actionfigures.server.Utils.JwtUtils;
import jg.actionfigures.server.Utils.TokenEnum;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private JwtUtils jwtUtils = new JwtUtils();
    
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
            return ResponseEntity.badRequest().body("Пользователь с таким логином уже существует");
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
        String token = jwtUtils.generateToken(newUser, TokenEnum.ACCESS);
        String refreshToken = jwtUtils.generateToken(newUser, TokenEnum.REFRESH);
        // Store user and token in Redis
        storeUserInRedis(newUser.getLogin(), token, refreshToken);
        
        RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse(token, refreshToken);
        return ResponseEntity.ok(refreshTokenResponse);
    }
    

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        // Check if user with given login exists
        User user = userRepository.findByLogin(userLoginRequest.getLogin());
        if (user == null) {
            System.out.println(userLoginRequest.getLogin());
            return ResponseEntity.badRequest().body("Неправильный логин");
        }
        
        // Check if password matches
        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Неправильный пароль");
        }
        
        // Generate JWT token
        String token = jwtUtils.generateToken(user, TokenEnum.ACCESS);
        String refreshToken = jwtUtils.generateToken(user, TokenEnum.REFRESH);
        
        // Store user and token in Redis
        storeUserInRedis(user.getLogin(), token, refreshToken);

        RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse(token, refreshToken);
        return ResponseEntity.ok(refreshTokenResponse);
    }
    

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request1, @RequestBody RefreshTokenRequest request) {
        String authToken = jwtUtils.extractToken(request1);
        String name = request.getUsername();
        String refreshToken = request.getRefreshToken();
        if (authToken != null) {
            redisTemplate.delete(authToken);
            redisTemplate.delete(name);
            redisTemplate.delete(refreshToken);
            return ResponseEntity.ok("Выход выполнен успешно");
        }
        return(ResponseEntity.badRequest().build());

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {

            String refreshToken = refreshTokenRequest.getRefreshToken();
            String username = refreshTokenRequest.getUsername();
            
            // Get the user from the Redis cache
            User user = (User) redisTemplate.opsForValue().get(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ошибка: Пользователь с таким логином не найден");    
            }
            
            // Verify the refresh token
            if (!jwtUtils.validateToken(refreshToken, TokenEnum.REFRESH) && redisTemplate.opsForValue().get(refreshToken) != null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("refresh");
            }
            
            // Generate a new access token
            String accessToken = jwtUtils.generateToken(user, TokenEnum.ACCESS);
            String newRefreshToken = jwtUtils.generateToken(user, TokenEnum.REFRESH);
            // Store the new access token in the Redis cache
            redisTemplate.delete(refreshToken);
            storeUserInRedis(user.getLogin(), accessToken, newRefreshToken);
            
            // Return the new access token
            RefreshTokenResponse response = new RefreshTokenResponse(accessToken, newRefreshToken);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    private void storeUserInRedis(String login, String accessToken, String refreshToken) {
        String userKey = login;
        String accessTokenKey = accessToken;
        String refreshTokenKey = refreshToken;
    
        redisTemplate.opsForValue().set(userKey, userRepository.findByLogin(login), 365, TimeUnit.DAYS);
        redisTemplate.opsForValue().set(accessTokenKey, login, 1, TimeUnit.DAYS);
        redisTemplate.opsForValue().set(refreshTokenKey, login, 365, TimeUnit.DAYS);
    }

}


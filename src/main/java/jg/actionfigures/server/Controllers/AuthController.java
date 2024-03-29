package jg.actionfigures.server.Controllers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jg.actionfigures.server.API.UserRepository;
import jg.actionfigures.server.AuthModule.DTO.RefreshTokenRequest;
import jg.actionfigures.server.AuthModule.DTO.RefreshTokenResponse;
import jg.actionfigures.server.AuthModule.DTO.UserLoginRequest;
import jg.actionfigures.server.AuthModule.DTO.UserRegistrationRequest;
import jg.actionfigures.server.AuthModule.Utils.JwtUtils;
import jg.actionfigures.server.AuthModule.Utils.TokenEnum;
import jg.actionfigures.server.Models.PostgerSql.User;

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
        
        // Проверить, существуют ли пользоватли с таким же логином
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
        
        // Сохранение пользователя в постгрес
        userRepository.save(newUser);
        
        // Создание JWT токена
        String token = jwtUtils.generateToken(newUser, TokenEnum.ACCESS);
        String refreshToken = jwtUtils.generateToken(newUser, TokenEnum.REFRESH);
        // Сохранение токенов в редис
        storeUserInRedis(newUser.getLogin(), token, refreshToken);
        
        RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse(token, refreshToken);
        return ResponseEntity.ok(refreshTokenResponse);
    }
    

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        
        // Проверить валидность логина
        User user = userRepository.findByLogin(userLoginRequest.getLogin());
        if (user == null) {
            System.out.println(userLoginRequest.getLogin());
            return ResponseEntity.badRequest().body("Неправильный логин");
        }
        
        // Проверить совпадение паролей
        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Неправильный пароль");
        }


        if(redisTemplate.opsForValue().get(user.getLogin() + "_access_token") == null) {
        
            // Геренирование JWT токена
            String token = jwtUtils.generateToken(user, TokenEnum.ACCESS);
            String refreshToken = jwtUtils.generateToken(user, TokenEnum.REFRESH);

            // Сохранение токенов в редис
            storeUserInRedis(user.getLogin(), token, refreshToken);

            RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse(token, refreshToken);
            return ResponseEntity.ok(refreshTokenResponse);
        }

        String token = (String) redisTemplate.opsForValue().get(user.getLogin() + "_access_token");
        String refreshToken = (String) redisTemplate.opsForValue().get(user.getLogin() + "_refresh_token");

        RefreshTokenResponse response = new RefreshTokenResponse(token, refreshToken);
        return ResponseEntity.ok(response);

    }
    

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request1) {
        String authToken = jwtUtils.extractToken(request1);
        String login = jwtUtils.extractUsername(authToken, TokenEnum.ACCESS);
        if (authToken != null) {
            redisTemplate.delete(login + "_access_token");
            redisTemplate.delete(login + "_refresh_token");
            return ResponseEntity.ok("Выход выполнен успешно");
        }
        return(ResponseEntity.badRequest().build());

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {

            String refreshToken = refreshTokenRequest.getRefreshToken();
            String username = refreshTokenRequest.getUsername();
            
            // Провекра валидности токена обновления
            if (!jwtUtils.validateToken(refreshToken, TokenEnum.REFRESH) && redisTemplate.opsForValue().get(refreshToken) != null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("refresh");
            }
            
            // Создание новых токенов
            User user = userRepository.findByLogin(username);
            String accessToken = jwtUtils.generateToken(user, TokenEnum.ACCESS);
            String newRefreshToken = jwtUtils.generateToken(user, TokenEnum.REFRESH);
            // Store the new access token in the Redis cache
            redisTemplate.delete(refreshToken);
            storeUserInRedis(username, accessToken, newRefreshToken);
            
            // Генерация ответа
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
        String accessTokenKey = accessToken;
        String refreshTokenKey = refreshToken;
    
        redisTemplate.opsForValue().set(login + "_access_token", accessTokenKey, 1, TimeUnit.HOURS);
        redisTemplate.opsForValue().set(login + "_refresh_token", refreshTokenKey, 365, TimeUnit.DAYS);
    }


}


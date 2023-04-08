package jg.actionfigures.server.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jg.actionfigures.server.Auth.CookieUtil;
import jg.actionfigures.server.Auth.JwtUtil;

@Controller
public class LoginController {
    private static final String jwtTokenCookieName = "JWT-TOKEN";
    private static final String signingKey = "signingKey";
    private static final Map<String, String> credentials = new HashMap<>();

    @Autowired
    private HttpServletResponse resp;

    @Autowired
    private HttpServletRequest req = (HttpServletRequest) ((Object) RequestContextHolder.getRequestAttributes()); 



    public LoginController() {
        credentials.put("hellokoding", "hellokoding");
        credentials.put("hellosso", "hellosso");
    }

    // @RequestMapping("/")
    // public String home(){
    //     return "redirect:/login";
    // }

    @RequestMapping("/login")
    public String loginGet(){
        return "login.html";
    }

    @PostMapping("/auth-page")
    public String loginPut(HttpServletResponse httpServletResponse, String username, String password){
        if (username == null || !credentials.containsKey(username) || !credentials.get(username).equals(password)){
            return "login.html";
        }

        String token = JwtUtil.generateToken(signingKey, username);
        CookieUtil.create(httpServletResponse, jwtTokenCookieName, token, false, -1, "localhost");

        return "protected-resource.html";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        resp = httpServletResponse;
        return "protected-resource.html";
    }

    @PostMapping("/logout-clear-cookies")
    public String logoutFull() {
        JwtUtil.invalidateRelatedTokens((HttpServletRequest) ((Object) RequestContextHolder.getRequestAttributes()));
        CookieUtil.clear(resp, jwtTokenCookieName);
        return "login.html";
    }

}
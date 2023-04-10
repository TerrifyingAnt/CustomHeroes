package jg.actionfigures.server.Config;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jg.actionfigures.server.Utils.JwtUtils;
import jg.actionfigures.server.Utils.TokenEnum;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private JwtUtils jwtUtils = new JwtUtils();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtils.extractToken(request);
        
        if (token != null && jwtUtils.validateToken(token, TokenEnum.ACCESS) && redisTemplate.hasKey(jwtUtils.extractUsername(token, TokenEnum.ACCESS) + "_access_token")) {
            String username = jwtUtils.extractUsername(token, TokenEnum.ACCESS);
            List<String> roles = jwtUtils.extractRoles(token);

            List<GrantedAuthority> authorities = roles.stream()
            .map(role -> new SimpleGrantedAuthority(role))
            .collect(Collectors.toList());
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + authorities.get(0).getAuthority());
            authorities = Collections.singletonList(authority);

            System.out.println(authorities.get(0) + " " + username);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

}

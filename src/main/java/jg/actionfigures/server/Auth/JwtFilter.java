package jg.actionfigures.server.Auth;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter extends OncePerRequestFilter {
    private static final String jwtTokenCookieName = "JWT-TOKEN";
    private static final String signingKey = "signingKey";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
                String username = JwtUtil.parseToken((HttpServlet) httpServletRequest, jwtTokenCookieName, signingKey);
                if(username == null){
                    String authService = this.getFilterConfig().getInitParameter("services.auth");
                    httpServletResponse.sendRedirect(authService + "?redirect=" + httpServletRequest.getRequestURL());
                } else{
                    httpServletRequest.setAttribute("username", username);
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                }
    }
}
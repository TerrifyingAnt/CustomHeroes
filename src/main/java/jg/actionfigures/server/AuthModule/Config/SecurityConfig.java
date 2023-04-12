package jg.actionfigures.server.AuthModule.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.Filter;


@Configuration
@ComponentScan(basePackages = "jg.actionfigures.server.Config")
public class SecurityConfig implements WebSecurityCustomizer {
    
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public void customize(WebSecurity web) {
        web
        .ignoring()
        .requestMatchers("/resources/**")
        .requestMatchers("/publics/**");
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
        http.addFilterBefore(jwtAuthenticationFilter, (Class<? extends Filter>) UsernamePasswordAuthenticationFilter.class);
        
        return http.authorizeHttpRequests()
            .requestMatchers("/api/auth/register").permitAll()
            .requestMatchers("/api/auth/login").permitAll()
            .requestMatchers("/api/auth/logout").hasAnyRole("ADMIN", "CUSTOMER", "CRAFTER")
            .requestMatchers("/api/auth/refresh").permitAll()
            .requestMatchers("/test/xd").hasAnyRole("CUSTOMER")
            .requestMatchers("/api/auth/users").hasAnyRole("ADMIN")
            .requestMatchers("/test/messages").hasAnyRole("ADMIN", "CUSTOMER", "CRAFTER")
            .requestMatchers("/test/update").hasAnyRole("ADMIN", "CUSTOMER", "CRAFTER")
            .and().csrf().disable()
            .formLogin().disable()
            .logout().disable()
            .httpBasic().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().build();
    }
}

package lab2hw;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // dezactivează CSRF pt POST fără token
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // permite toate request-urile
                );
        return http.build();
    }
}

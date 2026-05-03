package com.billing.billingsystem;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login", "/h2-console/**").permitAll()
            .requestMatchers("/products", "/products/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/", true)
            .failureUrl("/login?error")
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .permitAll()
        )
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2-console/**")
        )
        .headers(headers -> headers
            .frameOptions(frame -> frame.sameOrigin())
        );
    return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var encoder = passwordEncoder();
        var user = User.builder()
            .username("admin")
            .password(encoder.encode("admin123"))
            .roles("ADMIN")           // ← changed from USER to ADMIN
            .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
package com.freeadddictionary.dict.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/", "/about", "/privacy", "/terms", "/css/**", "/js/**", "/images/**")
                    .permitAll()
                    .requestMatchers("/user/signup", "/user/register")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/dictionary/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/report/**")
                    .permitAll()
                    .requestMatchers("/admin/**")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated())
        .formLogin(login -> login.loginPage("/login").defaultSuccessUrl("/").permitAll())
        .logout(logout -> logout.logoutSuccessUrl("/").permitAll());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}

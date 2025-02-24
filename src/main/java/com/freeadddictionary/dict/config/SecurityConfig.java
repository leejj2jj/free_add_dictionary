package com.freeadddictionary.dict.config;

import com.freeadddictionary.dict.service.CustomUserDetailsService;
import java.util.Arrays;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final DataSource dataSource;
  private final CustomUserDetailsService userDetailsService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // CSRF 설정
        .csrf(
            csrf ->
                csrf.ignoringRequestMatchers("/api/**").ignoringRequestMatchers("/h2-console/**"))

        // CORS 설정
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))

        // Session 관리
        .sessionManagement(
            session ->
                session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(false))

        // h2-console 프레임 허용
        .headers(
            headers ->
                headers.addHeaderWriter(
                    new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))

        // URL 기반 권한 설정
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/",
                        "/about",
                        "/privacy",
                        "/terms",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/fragments/**",
                        "/layout/**",
                        "/user/login",
                        "/user/signup",
                        "/api/user/signup",
                        "/h2-console/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/dictionary")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/dictionary/form/**")
                    .authenticated()
                    .requestMatchers(HttpMethod.GET, "/dictionary/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/inquiry/**")
                    .permitAll()
                    .requestMatchers("/admin/**")
                    .hasRole("ADMIN")
                    .requestMatchers("/api/dictionary/**")
                    .authenticated()
                    .anyRequest()
                    .authenticated())

        // 로그인 설정
        .formLogin(
            login ->
                login
                    .loginPage("/user/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .loginProcessingUrl("/user/login-process")
                    .defaultSuccessUrl("/")
                    .failureUrl("/user/login?error=true")
                    .permitAll())

        // 로그아웃 설정
        .logout(
            logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .permitAll())

        // Remember Me 설정
        .rememberMe(
            remember ->
                remember
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(86400)
                    .userDetailsService(userDetailsService))
        .exceptionHandling(
            exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(
                    (request, response, authException) -> response.sendRedirect("/user/login")));

    return http.build();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:8888"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
    tokenRepository.setDataSource(dataSource);
    tokenRepository.setCreateTableOnStartup(true);
    return tokenRepository;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}

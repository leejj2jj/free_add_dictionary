package com.freeadddictionary.dict.config.security;

import com.freeadddictionary.dict.service.CustomUserDetailsService;
import com.freeadddictionary.dict.service.LoginAttemptService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
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
  private final LoginAttemptService loginAttemptService;

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
                headers
                    .addHeaderWriter(
                        new XFrameOptionsHeaderWriter(
                            XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                    .contentSecurityPolicy(
                        "script-src 'self' https://cdn.jsdelivr.net; object-src 'none'; base-uri"
                            + " 'self'")
                    .and()
                    .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN)
                    .and()
                    .permissionsPolicy(
                        permissions ->
                            permissions.policy("camera=(), microphone=(), geolocation=()")))

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
                        "/user/password-reset",
                        "/user/password-reset-confirm",
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
                    .failureHandler(customAuthenticationFailureHandler())
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
                    .key("uniqueAndSecret")
                    .tokenValiditySeconds(60 * 60 * 24 * 14)
                    .userDetailsService(userDetailsService)
                    .tokenRepository(persistentTokenRepository()))
        .exceptionHandling(
            exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(
                    (request, response, authException) -> response.sendRedirect("/user/login")));

    return http.build();
  }

  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
    tokenRepository.setDataSource(dataSource);
    tokenRepository.setCreateTableOnStartup(true); // 최초 실행 시에만 true로 설정
    return tokenRepository;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
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
  public AuthenticationFailureHandler customAuthenticationFailureHandler() {
    return new SimpleUrlAuthenticationFailureHandler() {
      @Override
      public void onAuthenticationFailure(
          HttpServletRequest request,
          HttpServletResponse response,
          AuthenticationException exception)
          throws IOException, ServletException {
        String ip = loginAttemptService.getClientIP(request);
        loginAttemptService.loginFailed(ip);

        String errorMessage = "이메일 또는 비밀번호가 올바르지 않습니다.";

        if (loginAttemptService.isBlocked(ip)) {
          errorMessage = "로그인 시도 횟수를 초과했습니다. 잠시 후 다시 시도해 주세요.";
        } else if (exception instanceof LockedException) {
          errorMessage = "계정이 잠겼습니다. 관리자에게 문의하세요.";
        } else if (exception instanceof DisabledException) {
          errorMessage = "이메일 인증이 필요합니다.";
        }

        setDefaultFailureUrl(
            "/user/login?error=true&message=" + URLEncoder.encode(errorMessage, "UTF-8"));
        super.onAuthenticationFailure(request, response, exception);
      }
    };
  }
}

package com.gift.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import lombok.extern.slf4j.Slf4j;

/**
 * セキュリティ関連の設定クラス.
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/favicon.ico", "/error")
        .permitAll().anyRequest().authenticated())

        .formLogin(form -> form.loginPage("/login")
            // ⭕️ ログイン成功時のログ
            .successHandler((request, response, authentication) -> {
              String username = authentication.getName();
              log.info("【SECURITY】ユーザー「{}」がログインに成功しました。IP: {}", username, request.getRemoteAddr());
              response.sendRedirect("/gift/main");
            })
            // ⭕️ ログイン失敗時のログ
            .failureHandler((request, response, exception) -> {
              String username = request.getParameter("username");
              log.warn("【SECURITY WARNING】ユーザー「{}」のログインに失敗しました。理由: {} IP: {}", username,
                  exception.getMessage(), request.getRemoteAddr());
              response.sendRedirect("/login?error");
            }).permitAll())

        // 💡 ログアウト処理のログ
        .logout(logout -> logout.logoutUrl("/logout")
            .logoutSuccessHandler((request, response, authentication) -> {
              if (authentication != null) {
                log.info("【SECURITY】ユーザー「{}」がログアウトしました。", authentication.getName());
              }
              response.sendRedirect("/login?logout");
            }).invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll());

    return http.build();
  }
}

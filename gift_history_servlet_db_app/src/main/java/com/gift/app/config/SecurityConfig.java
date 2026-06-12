package com.gift.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * セキュリティ関連の設定クラス.
 */
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
        .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/gift/main", true)
            .failureUrl("/login?error").permitAll())
        .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout")
            .invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll());

    return http.build();
  }
}

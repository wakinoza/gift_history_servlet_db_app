package com.gift.app.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.gift.app.entity.User;
import com.gift.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Validated
public class LoginController {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * ログイン画面の表示
   */
  @GetMapping("/login")
  public String showLoginPage() {
    return "login";
  }

  /**
   * ログイン認証処理
   */
  @PostMapping("/login")
  public String login(@RequestParam("username") @NotBlank @Size(max = 50) String username,
      @RequestParam("password") @NotBlank @Size(max = 100) String password, HttpSession session) {

    Optional<User> userOpt = userRepository.findByName(username);

    if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {

      session.invalidate();

      return "redirect:/main";
    }

    return "redirect:/login?error";
  }
}

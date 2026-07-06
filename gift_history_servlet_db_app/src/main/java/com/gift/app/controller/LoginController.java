package com.gift.app.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class LoginController {

  /**
   * ログイン画面の表示を行う
   */
  @GetMapping("/login")
  public String showLoginPage() {
    log.info("ログイン画面がリクエストされました。");
    return "login";
  }
}


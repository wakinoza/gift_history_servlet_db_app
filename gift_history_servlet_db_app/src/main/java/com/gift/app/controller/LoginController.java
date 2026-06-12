package com.gift.app.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {

  /**
   * ログイン画面の表示を行う
   */
  @GetMapping("/login")
  public String showLoginPage() {
    return "login";
  }
}


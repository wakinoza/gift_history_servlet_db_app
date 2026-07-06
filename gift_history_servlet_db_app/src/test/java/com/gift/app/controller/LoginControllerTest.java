package com.gift.app.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoginControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("未ログイン状態でも、ログイン画面（/login）に正常にアクセスできること")
  void showLoginPage_Success() throws Exception {
    mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("login"));
  }
}

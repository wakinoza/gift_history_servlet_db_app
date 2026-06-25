package com.gift.app.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.gift.app.entity.GiftItem;
import com.gift.app.repository.GiftItemRepository;

@WebMvcTest(GiftItemController.class)
@AutoConfigureMockMvc(addFilters = false)
class GiftItemControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private GiftItemRepository giftItemRepository;

  // ====================================================================
  // 1. 画面表示系のテスト
  // ====================================================================

  @Test
  @DisplayName("GET /gift/main : 正常に一覧画面が表示され、モデルにリストが格納されること")
  void showMainPage_Success() throws Exception {
    when(giftItemRepository.findAll()).thenReturn(new ArrayList<>());

    mockMvc.perform(get("/gift/main")).andExpect(status().isOk())
        .andExpect(view().name("gift/main")).andExpect(model().attributeExists("giftItemList"));
  }

  @Test
  @DisplayName("GET /gift/new : 正常に新規登録画面が表示されること")
  void showNewGiftPage_Success() throws Exception {
    mockMvc.perform(get("/gift/new")).andExpect(status().isOk()).andExpect(view().name("gift/new"))
        .andExpect(model().attributeExists("giftItem"));
  }

  // ====================================================================
  // 2. 新規登録（POST /gift/new）の網羅テスト
  // ====================================================================

  @Test
  @DisplayName("POST /gift/new (ルート①) : 全項目が正しく入力されている場合、保存してリダイレクトすること")
  void createNewGift_Success_AllFilled() throws Exception {
    mockMvc
        .perform(post("/gift/new").param("what", "メロン").param("who", "佐藤さん").param("why", "お中元")
            .param("howMuch", "5000円").param("whenis", "2026-06-23").param("needReturn", "必要")
            .param("hasGaveReturn", "未返礼"))
        .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/gift/main"));

    verify(giftItemRepository).save(any(GiftItem.class));
  }

  @Test
  @DisplayName("POST /gift/new (ルート②) : 一部空欄がある場合、「未回答」に自動補完されて保存されること")
  void createNewGift_Success_WithDefaultValues() throws Exception {
    mockMvc
        .perform(post("/gift/new").param("what", "").param("who", "").param("why", "お祝い")
            .param("howMuch", ""))
        .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/gift/main"));

    verify(giftItemRepository).save(any(GiftItem.class));
  }

  @Test
  @DisplayName("POST /gift/new (ルート③) : すべての項目が未入力の場合、エラー属性を伴って画面に差し戻されること")
  void createNewGift_Fail_AllEmpty() throws Exception {
    mockMvc
        .perform(post("/gift/new").param("what", "").param("who", "").param("why", "")
            .param("howMuch", "").param("whenis", ""))
        .andExpect(status().isOk()).andExpect(view().name("gift/new"))
        .andExpect(model().attribute("allEmptyError", true));

    verify(giftItemRepository, never()).save(any(GiftItem.class));
  }

  @Test
  @DisplayName("POST /gift/new (ルート④) : 文字数オーバー等のバリデーションエラーがある場合、登録画面へ差し戻されること")
  void createNewGift_Fail_ValidationError() throws Exception {

    String longString = "A".repeat(101);

    mockMvc.perform(post("/gift/new").param("what", longString).param("who", "テスト").param("whenis",
        "2026-06-23")).andExpect(status().isOk()).andExpect(view().name("gift/new"));
  }
}

package com.gift.app.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.ArrayList;
import java.util.Optional;
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

  @Test
  @DisplayName("POST /gift/new : 日付フォーマットの型変換エラーが発生した場合、whenisをnullにして処理を続行すること")
  void createNewGift_DateValidationError_Continue() throws Exception {
    mockMvc
        .perform(post("/gift/new").param("what", "お菓子").param("who", "田中さん").param("whenis",
            "invalid-date-format"))
        .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/gift/main"));

    verify(giftItemRepository).save(any(GiftItem.class));
  }

  @Test
  @DisplayName("POST /gift/new : 各項目が1つだけ入力されているパターンで全未入力チェックの回避と補完処理の裏分岐をすべて網羅する")
  void createNewGift_BranchCoverage_AllCombined() throws Exception {
    mockMvc.perform(post("/gift/new").param("what", "時計").param("who", "").param("why", "")
        .param("howMuch", ""));
    mockMvc.perform(post("/gift/new").param("what", "").param("who", "鈴木さん").param("why", "")
        .param("howMuch", ""));
    mockMvc.perform(post("/gift/new").param("what", "").param("who", "").param("why", "結婚祝")
        .param("howMuch", ""));
    mockMvc.perform(post("/gift/new").param("what", "").param("who", "").param("why", "")
        .param("howMuch", "3000円"));
    mockMvc.perform(post("/gift/new").param("what", "").param("who", "").param("why", "")
        .param("howMuch", "").param("whenis", "2026-06-25"));
  }

  // ====================================================================
  // 3. 詳細画面・ステータス更新・削除のテスト
  // ====================================================================

  @Test
  @DisplayName("GET /gift/detail/{id} : 存在するIDの場合、詳細画面が正常に表示されること")
  void showDetailPage_Success() throws Exception {
    GiftItem item = new GiftItem();
    item.setId(1);
    item.setWhat("お茶");

    when(giftItemRepository.findById(1)).thenReturn(Optional.of(item));

    mockMvc.perform(get("/gift/detail/1")).andExpect(status().isOk())
        .andExpect(view().name("gift/detail"))
        .andExpect(model().attribute("currentGiftItem", item));
  }

  @Test
  @DisplayName("GET /gift/detail/{id} : 存在しないIDの場合、404 NOT FOUND エラーになること")
  void showDetailPage_Fail_NotFound() throws Exception {
    when(giftItemRepository.findById(999)).thenReturn(Optional.empty());

    mockMvc.perform(get("/gift/detail/999")).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("POST /gift/detail/{id}/return : 存在するIDの場合、返礼ステータスが「済」に更新され、一覧へリダイレクトすること")
  void updateToReturned_Success() throws Exception {
    GiftItem item = new GiftItem();
    item.setId(1);
    item.setHasGaveReturn("未返礼");

    when(giftItemRepository.findById(1)).thenReturn(Optional.of(item));

    mockMvc.perform(post("/gift/detail/1/return")).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/gift/main"));

    verify(giftItemRepository).save(item);
  }

  @Test
  @DisplayName("POST /gift/detail/{id}/return : 存在しないIDの場合、IllegalArgumentExceptionが発生すること")
  void updateToReturned_Fail_NotFound() throws Exception {
    when(giftItemRepository.findById(999)).thenReturn(java.util.Optional.empty());

    org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
      mockMvc.perform(post("/gift/detail/999/return"));
    }).hasRootCauseInstanceOf(IllegalArgumentException.class)
        .hasStackTraceContaining("Invalid gift Item Id:999");
  }

  @Test
  @DisplayName("POST /gift/detail/{id}/delete : 正常に削除処理が走り、一覧へリダイレクトすること")
  void deleteGift_Success() throws Exception {

    GiftItem item = new GiftItem();
    item.setId(1);
    when(giftItemRepository.findById(1)).thenReturn(Optional.of(item));

    mockMvc.perform(post("/gift/detail/1/delete")).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/gift/main"));

    verify(giftItemRepository).deleteById(1);
  }

  // ====================================================================
  // 4. 編集画面・編集実行のテスト
  // ====================================================================

  @Test
  @DisplayName("GET /gift/edit/{id} : 存在するIDの場合、回答を空文字に置換して編集画面を表示すること")
  void showEditGiftPage_Success() throws Exception {
    GiftItem item = new GiftItem();
    item.setId(1);
    item.setWhat("果物");
    item.setWho("佐藤さん");
    item.setWhy("お中元");
    item.setHowMuch("5000円");

    when(giftItemRepository.findById(1)).thenReturn(java.util.Optional.of(item));

    mockMvc.perform(get("/gift/edit/1")).andExpect(status().isOk())
        .andExpect(view().name("gift/new")).andExpect(model().attributeExists("giftItem"));
  }

  @Test
  @DisplayName("GET /gift/edit/{id} : 存在するIDの場合、すべての『未回答』フィールドを空文字に置換して編集画面を表示すること")
  void showEditGiftPage_Success_AllUnanswered() throws Exception {
    GiftItem unansweredItem = new GiftItem();
    unansweredItem.setId(1);
    unansweredItem.setWhat("未回答");
    unansweredItem.setWho("未回答");
    unansweredItem.setWhy("未回答");
    unansweredItem.setHowMuch("未回答");

    when(giftItemRepository.findById(1)).thenReturn(java.util.Optional.of(unansweredItem));

    mockMvc.perform(get("/gift/edit/1")).andExpect(status().isOk())
        .andExpect(view().name("gift/new")).andExpect(model().attributeExists("giftItem"));
  }

  @Test
  @DisplayName("GET /gift/edit/{id} : 存在しないIDの編集画面を表示しようとした場合、IllegalArgumentExceptionが発生すること")
  void showEditGiftPage_Fail_NotFound() throws Exception {
    when(giftItemRepository.findById(999)).thenReturn(java.util.Optional.empty());

    org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
      mockMvc.perform(get("/gift/edit/999"));
    }).hasRootCauseInstanceOf(IllegalArgumentException.class)
        .hasStackTraceContaining("Invalid gift Item Id:999");
  }

  @Test
  @DisplayName("POST /gift/edit/{id} : 正常に入力されている場合、編集内容が上書き保存されて詳細画面へリダイレクトすること")
  void updateGift_Success() throws Exception {
    GiftItem existingItem = new GiftItem();
    existingItem.setId(1);
    when(giftItemRepository.findById(1)).thenReturn(java.util.Optional.of(existingItem));

    mockMvc
        .perform(post("/gift/edit/1").param("what", "高級メロン").param("who", "鈴木さん").param("why", "お礼")
            .param("howMuch", "10000円").param("hasGaveReturn", "済"))
        .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/gift/detail/1"));

    verify(giftItemRepository).save(any(GiftItem.class));
  }

  @Test
  @DisplayName("POST /gift/edit/{id} : 編集時にすべての項目を空にした場合、エラー属性を伴って画面に差し戻されること")
  void updateGift_Fail_AllEmpty() throws Exception {
    GiftItem existingItem = new GiftItem();
    existingItem.setId(1);
    when(giftItemRepository.findById(1)).thenReturn(java.util.Optional.of(existingItem));

    mockMvc
        .perform(post("/gift/edit/1").param("what", "").param("who", "").param("why", "")
            .param("howMuch", ""))
        .andExpect(status().isOk()).andExpect(view().name("gift/new"))
        .andExpect(model().attribute("allEmptyError", true));
  }

  @Test
  @DisplayName("POST /gift/edit/{id} : 編集時に文字数バリデーションエラーがある場合、入力画面へ差し戻されること")
  void updateGift_Fail_ValidationError() throws Exception {
    GiftItem existingItem = new GiftItem();
    existingItem.setId(1);
    when(giftItemRepository.findById(1)).thenReturn(java.util.Optional.of(existingItem));

    String longString = "A".repeat(101);

    mockMvc.perform(post("/gift/edit/1").param("what", longString).param("who", "テスト"))
        .andExpect(status().isOk()).andExpect(view().name("gift/new"));
  }

  @Test
  @DisplayName("POST /gift/edit/{id} : 編集時に日付フォーマットエラーがある場合、whenisをnullにして処理を続行すること")
  void updateGift_DateValidationError_Continue() throws Exception {
    GiftItem existingItem = new GiftItem();
    existingItem.setId(1);
    when(giftItemRepository.findById(1)).thenReturn(java.util.Optional.of(existingItem));

    mockMvc
        .perform(post("/gift/edit/1").param("what", "お菓子").param("who", "鈴木さん").param("whenis",
            "invalid-date-format"))
        .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/gift/detail/1"));

    verify(giftItemRepository).save(any(GiftItem.class));
  }

  @Test
  @DisplayName("POST /gift/edit/{id} : 各項目が1つだけ入力されているパターンで全未入力チェックの回避と補完処理の裏分岐をすべて網羅する")
  void updateGift_BranchCoverage_AllCombined() throws Exception {
    GiftItem existingItem = new GiftItem();
    existingItem.setId(1);
    when(giftItemRepository.findById(1)).thenReturn(java.util.Optional.of(existingItem));

    mockMvc.perform(post("/gift/edit/1").param("what", "お肉").param("who", "").param("why", "")
        .param("howMuch", ""));
    mockMvc.perform(post("/gift/edit/1").param("what", "").param("who", "鈴木さん").param("why", "")
        .param("howMuch", ""));
    mockMvc.perform(post("/gift/edit/1").param("what", "").param("who", "").param("why", "結婚祝")
        .param("howMuch", ""));
    mockMvc.perform(post("/gift/edit/1").param("what", "").param("who", "").param("why", "")
        .param("howMuch", "3000円"));
    mockMvc.perform(post("/gift/edit/1").param("what", "").param("who", "").param("why", "")
        .param("howMuch", "").param("hasGaveReturn", "").param("whenis", "2026-06-25"));
    mockMvc.perform(post("/gift/edit/1").param("what", "お菓子").param("needReturn", "不要")
        .param("hasGaveReturn", ""));
  }

  @Test
  @DisplayName("POST /gift/edit/{id} : 存在しないIDの編集実行の場合、IllegalArgumentExceptionが発生すること")
  void updateGift_Fail_NotFound() throws Exception {
    when(giftItemRepository.findById(999)).thenReturn(java.util.Optional.empty());

    org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
      mockMvc.perform(post("/gift/edit/999"));
    }).hasRootCauseInstanceOf(IllegalArgumentException.class)
        .hasStackTraceContaining("Invalid gift Item Id:999");
  }
}

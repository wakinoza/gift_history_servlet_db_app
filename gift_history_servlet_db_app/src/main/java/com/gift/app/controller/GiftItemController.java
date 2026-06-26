package com.gift.app.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.gift.app.entity.GiftItem;
import com.gift.app.repository.GiftItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/gift")
@RequiredArgsConstructor
public class GiftItemController {

  @Autowired
  private org.springframework.validation.SmartValidator validator;
  private final GiftItemRepository giftItemRepository;

  /**
   * 頂き物簡易一覧画面の表示
   */
  @GetMapping("/main")
  public String showMainPage(Model model) {
    List<GiftItem> giftItemList = giftItemRepository.findAll();

    log.info("【GIFT INFO】一覧画面がリクエストされました。取得件数: {}件", giftItemList.size());

    model.addAttribute("giftItemList", giftItemList);
    return "gift/main";
  }

  /**
   * 頂き物新規登録画面の表示
   */
  @GetMapping("/new")
  public String showNewGiftPage(Model model) {
    model.addAttribute("giftItem", new GiftItem());
    return "gift/new";
  }

  /**
   * 頂き物新規登録処理の実行
   */
  @PostMapping("/new")
  public String createNewGift(@Valid @ModelAttribute("giftItem") GiftItem giftItem,
      BindingResult bindingResult, Model model) {

    if (bindingResult.hasFieldErrors("whenis")) {
      log.info("【GIFT INFO】日付フィールドの型変換エラーを無視して続行します。");
      giftItem.setWhenis(null);
    }

    // すべての項目が未入力（白紙）であるかのチェック
    if (isAllFieldsEmpty(giftItem)) {
      log.warn("【GIFT WARNING】すべての項目が未入力のため、処理を中断しました。");
      model.addAttribute("allEmptyError", true);
      return "gift/new";
    }

    // 個別バリデーションエラー（文字数オーバーなど）があれば画面に戻る
    if (bindingResult.hasErrors() && !bindingResult.hasFieldErrors("whenis")) {

      log.warn("【GIFT WARNING】バリデーションエラーが発生したため、入力画面へ差し戻します。エラー数: {}個",
          bindingResult.getErrorCount());
      return "gift/new";
    }

    fillDefaultValues(giftItem);

    giftItemRepository.save(giftItem);

    log.info("【GIFT INFO】新しい頂き物の記録を登録しました。ID: {}, 品物: {}, 贈り主: {}", giftItem.getId(),
        giftItem.getWhat(), giftItem.getWho());

    return "redirect:/gift/main";
  }

  /**
   * 頂き物詳細画面の表示
   */
  @GetMapping("/detail/{id}")
  public String showDetailPage(@PathVariable("id") int id, Model model) {
    log.info("【GIFT INFO】詳細画面がリクエストされました。対象ID: {}", id);

    GiftItem currentGiftItem = giftItemRepository.findById(id)
        .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
            org.springframework.http.HttpStatus.NOT_FOUND, "GiftItem Not Found"));

    model.addAttribute("currentGiftItem", currentGiftItem);
    return "gift/detail";
  }

  /**
   * 返礼済みステータスの更新処理
   */
  @PostMapping("/detail/{id}/return")
  public String updateToReturned(@PathVariable("id") int id) {
    GiftItem giftItem = giftItemRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid gift Item Id:" + id));

    giftItem.setHasGaveReturn("済");
    giftItemRepository.save(giftItem);

    log.info("【GIFT INFO】頂き物記録(ID: {}) の返礼ステータスを「済」に更新しました。", id);

    return "redirect:/gift/main";
  }

  /**
   * 頂き物の削除処理
   */
  @PostMapping("/detail/{id}/delete")
  public String deleteGift(@PathVariable("id") int id) {
    giftItemRepository.deleteById(id);

    log.warn("【GIFT WARNING】頂き物記録(ID: {}) がシステムから削除されました。", id);

    return "redirect:/gift/main";
  }

  /**
   * 頂き物編集画面の表示
   */
  @GetMapping("/edit/{id}")
  public String showEditGiftPage(@PathVariable("id") int id, Model model) {
    log.info("【GIFT INFO】編集画面がリクエストされました。対象ID: {}", id);

    GiftItem giftItem = giftItemRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid gift Item Id:" + id));

    if ("未回答".equals(giftItem.getWhat()))
      giftItem.setWhat("");
    if ("未回答".equals(giftItem.getWho()))
      giftItem.setWho("");
    if ("未回答".equals(giftItem.getWhy()))
      giftItem.setWhy("");
    if ("未回答".equals(giftItem.getHowMuch()))
      giftItem.setHowMuch("");

    model.addAttribute("giftItem", giftItem);
    return "gift/new";
  }

  /**
   * 頂き物編集処理の実行
   */
  @PostMapping("/edit/{id}")
  public String updateGift(@PathVariable("id") int id,
      @ModelAttribute("giftItem") GiftItem formForm, BindingResult bindingResult, Model model) {

    GiftItem giftItem = giftItemRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid gift Item Id:" + id));

    if (isAllFieldsEmpty(formForm)) {
      log.warn("【GIFT WARNING】すべての項目が未入力のため、処理を中断しました。");
      model.addAttribute("allEmptyError", true);
      return "gift/new";
    }

    validator.validate(formForm, bindingResult);

    if (bindingResult.hasFieldErrors("whenis")) {
      formForm.setWhenis(null);
    }

    if (bindingResult.hasErrors() && !bindingResult.hasFieldErrors("whenis")) {
      log.warn("【GIFT WARNING】編集処理中にバリデーションエラーが発生しました。対象ID: {}, エラー数: {}個", id,
          bindingResult.getErrorCount());
      return "gift/new";
    }

    giftItem.setWhat(formForm.getWhat());
    giftItem.setWho(formForm.getWho());
    giftItem.setWhy(formForm.getWhy());
    giftItem.setHowMuch(formForm.getHowMuch());
    giftItem.setWhenis(formForm.getWhenis());
    giftItem.setHasGaveReturn(formForm.getHasGaveReturn());


    if (giftItem.getHasGaveReturn().isBlank()) {
      giftItem.setHasGaveReturn("未返礼");
    }

    fillDefaultValues(giftItem);
    giftItemRepository.save(giftItem);

    return "redirect:/gift/detail/" + id;
  }

  /**
   * 全ての入力項目が空（またはnull）であるかを判定します。
   */
  private boolean isAllFieldsEmpty(GiftItem item) {
    if (!java.util.Objects.toString(item.getWhat(), "").isBlank())
      return false;
    if (!java.util.Objects.toString(item.getWho(), "").isBlank())
      return false;
    if (!java.util.Objects.toString(item.getWhy(), "").isBlank())
      return false;
    if (!java.util.Objects.toString(item.getHowMuch(), "").isBlank())
      return false;
    if (item.getWhenis() != null)
      return false;

    return true;
  }

  /**
   * 未入力の項目（nullまたは空文字）にデフォルト値「未回答」をセットします。
   */
  private void fillDefaultValues(GiftItem item) {

    String what = java.util.Objects.toString(item.getWhat(), "");
    item.setWhat(what.isBlank() ? "未回答" : what);

    String who = java.util.Objects.toString(item.getWho(), "");
    item.setWho(who.isBlank() ? "未回答" : who);

    String why = java.util.Objects.toString(item.getWhy(), "");
    item.setWhy(why.isBlank() ? "未回答" : why);

    String howMuch = java.util.Objects.toString(item.getHowMuch(), "");
    item.setHowMuch(howMuch.isBlank() ? "未回答" : howMuch);
  }
}

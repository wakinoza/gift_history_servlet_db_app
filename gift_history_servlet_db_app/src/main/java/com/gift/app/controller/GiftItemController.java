package com.gift.app.controller;

import jakarta.validation.Valid;
import java.util.List;
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
    if ((giftItem.getWhat() == null || giftItem.getWhat().isBlank())
        && (giftItem.getWho() == null || giftItem.getWho().isBlank())
        && (giftItem.getWhy() == null || giftItem.getWhy().isBlank())
        && (giftItem.getHowMuch() == null || giftItem.getHowMuch().isBlank())
        && giftItem.getWhenis() == null) {

      log.warn("【GIFT WARNING】すべての項目が未入力のため、新規登録処理を中断しました。");
      model.addAttribute("allEmptyError", true);
      return "gift/new";
    }

    // 個別バリデーションエラー（文字数オーバーなど）があれば画面に戻る
    if (bindingResult.hasErrors() && !bindingResult.hasFieldErrors("whenis")) {

      log.warn("【GIFT WARNING】バリデーションエラーが発生したため、入力画面へ差し戻します。エラー数: {}個",
          bindingResult.getErrorCount());
      return "gift/new";
    }

    if (giftItem.getWhat() == null || giftItem.getWhat().isBlank())
      giftItem.setWhat("未回答");
    if (giftItem.getWho() == null || giftItem.getWho().isBlank())
      giftItem.setWho("未回答");
    if (giftItem.getWhy() == null || giftItem.getWhy().isBlank())
      giftItem.setWhy("未回答");
    if (giftItem.getHowMuch() == null || giftItem.getHowMuch().isBlank())
      giftItem.setHowMuch("未回答");

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
      @Valid @ModelAttribute("giftItem") GiftItem giftItem, BindingResult bindingResult,
      Model model) {

    if ((giftItem.getWhat() == null || giftItem.getWhat().isBlank())
        && (giftItem.getWho() == null || giftItem.getWho().isBlank())
        && (giftItem.getWhy() == null || giftItem.getWhy().isBlank())
        && (giftItem.getHowMuch() == null || giftItem.getHowMuch().isBlank())
        && giftItem.getWhenis() == null) {

      log.warn("【GIFT WARNING】すべての項目が未入力のため、編集（上書き）処理を中断しました。対象ID: {}", id);
      model.addAttribute("allEmptyError", true);
      return "gift/new";
    }

    if (bindingResult.hasFieldErrors("whenis")) {
      giftItem.setWhenis(null);
    }
    if (bindingResult.hasErrors() && !bindingResult.hasFieldErrors("whenis")) {
      log.warn("【GIFT WARNING】編集処理中にバリデーションエラーが発生しました。対象ID: {}, エラー数: {}個", id,
          bindingResult.getErrorCount());
      return "gift/new";
    }

    giftItem.setId(id);

    if (giftItem.getWhat() == null || giftItem.getWhat().isBlank())
      giftItem.setWhat("未回答");
    if (giftItem.getWho() == null || giftItem.getWho().isBlank())
      giftItem.setWho("未回答");
    if (giftItem.getWhy() == null || giftItem.getWhy().isBlank())
      giftItem.setWhy("未回答");
    if (giftItem.getHowMuch() == null || giftItem.getHowMuch().isBlank())
      giftItem.setHowMuch("未回答");
    if (giftItem.getHasGaveReturn() == null || giftItem.getHasGaveReturn().isBlank())
      giftItem.setHasGaveReturn("未返礼");

    giftItemRepository.save(giftItem);

    log.info("【GIFT INFO】頂き物記録(ID: {}) の情報が上書き更新されました。更新後の品物: {}, 贈り主: {}", id, giftItem.getWhat(),
        giftItem.getWho());

    return "redirect:/gift/detail/" + id;
  }
}

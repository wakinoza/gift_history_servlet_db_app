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

    // 型変換エラー（日付の空文字エラーなど）を、全未入力チェックの前に一旦クリアする
    // 日付の変換エラー（typeMismatch）だけが発生している場合は、必須項目ではないのでエラーを無視して処理を続行させru
    if (bindingResult.hasFieldErrors("whenis")) {
      giftItem.setWhenis(null);
    }

    // すべての項目が未入力（白紙）であるかのチェック
    if ((giftItem.getWhat() == null || giftItem.getWhat().isBlank())
        && (giftItem.getWho() == null || giftItem.getWho().isBlank())
        && (giftItem.getWhy() == null || giftItem.getWhy().isBlank())
        && (giftItem.getHowMuch() == null || giftItem.getHowMuch().isBlank())
        && giftItem.getWhenis() == null) {


      model.addAttribute("allEmptyError", true);
      return "gift/new";
    }

    // 個別バリデーションエラー（文字数オーバーなど）があれば画面に戻る
    if (bindingResult.hasErrors() && !bindingResult.hasFieldErrors("whenis")) {
      return "gift/new";
    }

    // もし一部だけ入力されていて空の欄があれば「未回答」を補完
    if (giftItem.getWhat() == null || giftItem.getWhat().isBlank())
      giftItem.setWhat("未回答");
    if (giftItem.getWho() == null || giftItem.getWho().isBlank())
      giftItem.setWho("未回答");
    if (giftItem.getWhy() == null || giftItem.getWhy().isBlank())
      giftItem.setWhy("未回答");
    if (giftItem.getHowMuch() == null || giftItem.getHowMuch().isBlank())
      giftItem.setHowMuch("未回答");

    giftItemRepository.save(giftItem);

    return "redirect:/gift/main";
  }

  /**
   * 頂き物詳細画面の表示
   */
  @GetMapping("/detail/{id}")
  public String showDetailPage(@PathVariable("id") int id, Model model) {
    GiftItem currentGiftItem = giftItemRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid gift Item Id:" + id));

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

    return "redirect:/gift/main";
  }

  /**
   * 頂き物の削除処理
   */
  @PostMapping("/detail/{id}/delete")
  public String deleteGift(@PathVariable("id") int id) {
    giftItemRepository.deleteById(id);

    return "redirect:/gift/main";
  }

  /**
   * 頂き物編集画面の表示
   */
  @GetMapping("/edit/{id}")
  public String showEditGiftPage(@PathVariable("id") int id, Model model) {
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

      model.addAttribute("allEmptyError", true);
      return "gift/new";
    }

    if (bindingResult.hasFieldErrors("whenis")) {
      giftItem.setWhenis(null);
    }
    if (bindingResult.hasErrors() && !bindingResult.hasFieldErrors("whenis")) {
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

    return "redirect:/gift/detail/" + id;
  }
}

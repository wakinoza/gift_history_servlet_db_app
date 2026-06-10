package com.gift.app.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  public String showNewGiftPage() {
    return "gift/new";
  }

  /**
   * 頂き物新規登録処理の実行
   */
  @PostMapping("/new")
  public String createNewGift(@RequestParam("what") String what,
      @RequestParam("whenis") LocalDate whenis, @RequestParam("who") String who,
      @RequestParam("why") String why, @RequestParam("howMuch") String howMuch,
      @RequestParam("needReturn") String needReturn) {

    GiftItem newItem = new GiftItem();

    if (what != null && !what.isBlank())
      newItem.setWhat(what);
    if (whenis != null)
      newItem.setWhenis(whenis);
    if (who != null && !who.isBlank())
      newItem.setWho(who);
    if (why != null && !why.isBlank())
      newItem.setWhy(why);
    if (howMuch != null && !howMuch.isBlank())
      newItem.setHowMuch(howMuch);
    if (needReturn != null && !needReturn.isBlank())
      newItem.setNeedReturn(needReturn);

    giftItemRepository.save(newItem);

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
}

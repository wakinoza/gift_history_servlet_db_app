package com.gift.app.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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


}

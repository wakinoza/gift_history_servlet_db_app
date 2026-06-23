package com.gift.app.repository;

import static org.assertj.core.api.Assertions.*;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.gift.app.entity.GiftItem;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GiftItemRepositoryTest {

  @Autowired
  private GiftItemRepository giftItemRepository;

  @Test
  @DisplayName("頂き物情報を保存し、正しくIDが採番されて取得できること")
  void saveAndFindTest() {
    GiftItem item = new GiftItem();
    item.setWhat("高級チョコレート");
    item.setWho("鈴木さん");
    item.setWhy("お土産");
    item.setHowMuch("3,000円");
    item.setWhenis(LocalDate.of(2026, 6, 01));
    item.setNeedReturn("不要");
    item.setHasGaveReturn("未返礼");

    GiftItem savedItem = giftItemRepository.save(item);

    assertThat(savedItem).isNotNull();
    assertThat(savedItem.getId()).isPositive();
    assertThat(savedItem.getWhat()).isEqualTo("高級チョコレート");

    Optional<GiftItem> foundItemOpt = giftItemRepository.findById(savedItem.getId());

    assertThat(foundItemOpt).isPresent();
    assertThat(foundItemOpt.get().getWhat()).isEqualTo("高級チョコレート");
    assertThat(foundItemOpt.get().getWho()).isEqualTo("鈴木さん");
  }
}

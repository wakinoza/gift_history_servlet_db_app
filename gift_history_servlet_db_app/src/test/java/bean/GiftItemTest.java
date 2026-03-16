package bean;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GiftItemTest {

  @Test
  @DisplayName("引数なしコンストラクタで生成した際、返礼ステータスの初期値が『未返礼』であること")
  void testDefaultConstructor() {
    GiftItem item = new GiftItem();

    assertThat(item.getHasGaveReturn()).isEqualTo("未返礼");
    assertThat(item.getWhat()).isNull();
  }

  @Test
  @DisplayName("主要項目コンストラクタでセットした値が正しく保持されること")
  void testMainConstructor() {
    GiftItem item = new GiftItem("メロン", "2026-03-16", "親戚", "内祝い", "5000", "必要");

    assertThat(item)
        .extracting(GiftItem::getWhat, GiftItem::getWhenis, GiftItem::getWho, GiftItem::getWhy,
            GiftItem::getHowMuch, GiftItem::getNeedReturn)
        .containsExactly("メロン", "2026-03-16", "親戚", "内祝い", "5000", "必要");

    assertThat(item.getHasGaveReturn()).isEqualTo("未返礼");
  }

  @Test
  @DisplayName("全てのsetter/getterが正しく動作すること")
  void testAllSettersAndGetters() {
    GiftItem item = new GiftItem();


    item.setId("001");
    item.setWhat("お菓子");
    item.setWhenis("2026-01-01");
    item.setWho("友人");
    item.setWhy("誕生日");
    item.setHowMuch("3000");
    item.setNeedReturn("不要");
    item.setHasGaveReturn("返礼済み");

    assertThat(item).returns("001", GiftItem::getId).returns("お菓子", GiftItem::getWhat)
        .returns("2026-01-01", GiftItem::getWhenis).returns("友人", GiftItem::getWho)
        .returns("誕生日", GiftItem::getWhy).returns("3000", GiftItem::getHowMuch)
        .returns("不要", GiftItem::getNeedReturn).returns("返礼済み", GiftItem::getHasGaveReturn);
  }
}

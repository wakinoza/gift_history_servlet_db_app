package model;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import dao.GiftItemDAO;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import bean.GiftItem;

class GiftItemLogicTest {

  private GiftItemLogic logic;

  @Mock
  private GiftItemDAO dao;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    logic = new GiftItemLogic(dao);
  }

  /**
   * インスタンス作成と入力値のバリデーションに関するテスト
   */
  @Nested
  @DisplayName("createNewGiftItemメソッドのテスト")
  class CreateNewGiftItemTest {

    @Test
    @DisplayName("正常系：全項目が正しく入力され、日付が変換されること")
    void testCreateSuccess() {
      GiftItem result =
          logic.createNewGiftItem("カタログギフト", "2026-03-16", "Alice", "結婚祝い", "5000", "必要");

      assertThat(result).isNotNull();
      assertThat(result.getWhat()).isEqualTo("カタログギフト");
      assertThat(result.getWhenis()).isEqualTo("2026年03月16日");
    }

    @ParameterizedTest
    @MethodSource("provideBlankVariations")
    @DisplayName("正常系：未入力（null/空文字/空白）の場合、『未回答』へ置換されること")
    void testDefaultValueReplacement(String val, String date) {
      // 全引数に「空白バリエーション」を渡して、三項演算子と短絡評価を網羅
      GiftItem result = logic.createNewGiftItem(val, date, val, val, val, val);

      assertThat(result).isNotNull();
      if (date == null || date.isBlank()) {
        assertThat(result.getWhenis()).isEqualTo("未回答");
      }
      assertThat(result.getWhat()).isEqualTo("未回答");
      assertThat(result.getWho()).isEqualTo("未回答");
    }

    static Stream<Arguments> provideBlankVariations() {
      return Stream.of(Arguments.of(null, null), Arguments.of("", ""), Arguments.of(" ", " "));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2026/03/16", "invalid"})
    @DisplayName("異常系：日付形式が不正な場合、nullを返すこと")
    void testInvalidDate(String invalidDate) {
      GiftItem result = logic.createNewGiftItem("チョコ", invalidDate, "Bob", "理由", "1000", "不要");
      assertThat(result).isNull();
    }

    @Nested
    @DisplayName("境界値・文字数制限のテスト")
    class LengthValidationTest {

      @Test
      @DisplayName("isExceeding: 1000文字ちょうどの時、正常に作成されること")
      void testBoundaryLength() {
        String boundaryStr = "a".repeat(1000);
        GiftItem result =
            logic.createNewGiftItem(boundaryStr, "2026-03-16", "Alice", "理由", "1000", "不要");
        assertThat(result).isNotNull();
      }

      @ParameterizedTest
      @MethodSource("provideOverLengthStrings")
      @DisplayName("isExceeding: 1001文字以上の項目がある場合、nullを返すこと")
      void testOverLength(String what, String who, String why, String howMuch) {
        GiftItem result = logic.createNewGiftItem(what, "2026-03-16", who, why, howMuch, "不要");
        assertThat(result).isNull();
      }

      static Stream<Arguments> provideOverLengthStrings() {
        String over = "a".repeat(1001);
        return Stream.of(Arguments.of(over, "Alice", "理由", "1000"),
            Arguments.of("物", over, "理由", "1000"), Arguments.of("物", "Alice", over, "1000"),
            Arguments.of("物", "Alice", "理由", over));
      }
    }
  }

  /**
   * IDのバリデーション（isInvalidId）が関わるメソッドのテスト
   */
  @Nested
  @DisplayName("IDバリデーションを伴うメソッドのテスト")
  class IdValidationTest {

    @Test
    @DisplayName("正常系：数値IDの場合、正しくDAOが呼ばれること")
    void testValidId() {
      GiftItem mockItem = new GiftItem();
      when(dao.select("1")).thenReturn(mockItem);
      when(dao.delete("1")).thenReturn(true);

      assertThat(logic.findGiftItem("1")).isEqualTo(mockItem);
      assertThat(logic.remove("1")).isTrue();

      verify(dao).select("1");
      verify(dao).delete("1");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", " ", ""})
    @DisplayName("異常系：数値以外のIDの場合、null/falseを返し、DAOを呼ばないこと")
    void testInvalidIdFormat(String id) {
      assertThat(logic.findGiftItem(id)).isNull();
      assertThat(logic.remove(id)).isFalse();
      verify(dao, never()).select(anyString());
    }

    @Test
    @DisplayName("異常系：IDがnullの場合にガードされること（短絡評価の網羅）")
    void testIdIsNull() {
      assertThat(logic.findGiftItem(null)).isNull();
      assertThat(logic.remove(null)).isFalse();
    }
  }

  /**
   * ロジックを持たない単純なDAO呼び出しのテスト
   */
  @Nested
  @DisplayName("その他のDAO連携メソッドのテスト")
  class OtherDaoMethodsTest {

    @Test
    @DisplayName("getAllGiftItem：全件取得ができること")
    void testGetAll() {
      List<GiftItem> list = Collections.singletonList(new GiftItem());
      when(dao.selectAll()).thenReturn(list);
      assertThat(logic.getAllGiftItem()).hasSize(1);
    }

    @Test
    @DisplayName("add：追加ができること")
    void testAdd() {
      GiftItem item = new GiftItem();
      when(dao.insert(item)).thenReturn(true);
      assertThat(logic.add(item)).isTrue();
    }

    @Test
    @DisplayName("returned：ステータス更新ができること")
    void testReturned() {
      when(dao.updateReturned("10")).thenReturn(true);
      assertThat(logic.returned("10")).isTrue();
    }
  }
}

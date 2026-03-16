package util;

import static org.assertj.core.api.Assertions.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CsrfUtilTest {

  @Test
  @DisplayName("トークンが適切な形式で生成されること")
  void testGenerateToken() {
    String token1 = CsrfUtil.generateToken();
    String token2 = CsrfUtil.generateToken();

    assertThat(token1).isNotBlank();
    assertThat(token2).isNotBlank();
    assertThat(token1.length()).isGreaterThanOrEqualTo(43);
    assertThat(token1).isNotEqualTo(token2);
  }

  @Test
  @DisplayName("コンストラクタのプライベート化とカバレッジ網羅")
  void testConstructorIsPrivate() throws Exception {
    Constructor<CsrfUtil> constructor = CsrfUtil.class.getDeclaredConstructor();
    constructor.setAccessible(true);

    try {
      constructor.newInstance();
      fail("AssertionError expected");
    } catch (InvocationTargetException e) {
      assertThat(e.getTargetException()).isInstanceOf(AssertionError.class);
    }
  }
}

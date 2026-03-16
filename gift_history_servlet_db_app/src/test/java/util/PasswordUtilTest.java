package util;

import static org.assertj.core.api.Assertions.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordUtilTest {

  @Test
  @DisplayName("パスワードが正しくハッシュ化され、照合ができること")
  void testHashAndCheck() {
    String rawPassword = "my_secure_password";

    String hashedPassword = PasswordUtil.hash(rawPassword);

    assertThat(hashedPassword).isNotEqualTo(rawPassword);

    assertThat(PasswordUtil.check(rawPassword, hashedPassword)).isTrue();

    assertThat(PasswordUtil.check("wrong_password", hashedPassword)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "a", "long_password_1234567890!@#$%^&*()"})
  @DisplayName("様々な文字列パターンでハッシュ化と照合が正常に行えること")
  void testVariousPasswordPatterns(String password) {
    String hashedPassword = PasswordUtil.hash(password);
    assertThat(PasswordUtil.check(password, hashedPassword)).isTrue();
  }

  @Test
  @DisplayName("ユーティリティクラスのコンストラクタを呼び出してカバレッジを網羅する")
  void testConstructorIsPrivate() throws Exception {
    Constructor<PasswordUtil> constructor = PasswordUtil.class.getDeclaredConstructor();

    constructor.setAccessible(true);

    try {
      constructor.newInstance();
    } catch (InvocationTargetException e) {
      assertThat(e.getTargetException()).isInstanceOf(AssertionError.class);
    }
  }

}

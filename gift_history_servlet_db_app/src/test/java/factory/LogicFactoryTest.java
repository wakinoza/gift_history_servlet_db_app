package factory;

import static org.assertj.core.api.Assertions.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import model.GiftItemLogic;
import model.LoginLogic;

class LogicFactoryTest {

  @Test
  @DisplayName("createLoginLogic: LoginLogicのインスタンスが正しく生成されること")
  void testCreateLoginLogic() {
    LoginLogic logic = LogicFactory.createLoginLogic();

    assertThat(logic).isNotNull();
    assertThat(logic).isInstanceOf(LoginLogic.class);
  }

  @Test
  @DisplayName("createGiftItemLogic: GiftItemLogicのインスタンスが正しく生成されること")
  void testCreateGiftItemLogic() {
    GiftItemLogic logic = LogicFactory.createGiftItemLogic();

    assertThat(logic).isNotNull();
    assertThat(logic).isInstanceOf(GiftItemLogic.class);
  }

  @Test
  @DisplayName("コンストラクタのプライベート化とカバレッジ網羅")
  void testConstructorIsPrivate() throws Exception {
    Constructor<LogicFactory> constructor = LogicFactory.class.getDeclaredConstructor();
    constructor.setAccessible(true);

    try {
      constructor.newInstance();
      fail("AssertionError expected");
    } catch (InvocationTargetException e) {
      assertThat(e.getTargetException()).isInstanceOf(AssertionError.class);
    }
  }
}

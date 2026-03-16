package bean;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  @DisplayName("コンストラクタでセットした値がgetterで取得できること")
  void testConstructorAndGetters() {
    User user = new User(1, "alice", "password123");


    assertThat(user.getId()).isEqualTo(1);

    assertThat(user).extracting(User::getName, User::getPass).containsExactly("alice",
        "password123");
  }

  @Test
  @DisplayName("setterで値が正しく更新されること")
  void testSetters() {
    User user = new User();

    user.setId(100);
    user.setName("alice");
    user.setPass("new-pass");

    assertThat(user.getId()).isEqualTo(100);
    assertThat(user.getName()).isEqualTo("alice");
    assertThat(user.getPass()).isEqualTo("new-pass");
  }
}

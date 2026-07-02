package com.gift.app.repository;

import static org.assertj.core.api.Assertions.*;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import com.gift.app.entity.User;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("ユーザー名で検索したとき、該当するユーザー情報が正しく取得できること")
  void findByNameTest() {
    User user = new User();
    user.setName("testUser");
    user.setPassword("$2a$12$xyz...");
    userRepository.save(user);

    Optional<User> foundUserOpt = userRepository.findByName("testUser");

    assertThat(foundUserOpt).isPresent();
    assertThat(foundUserOpt.get().getName()).isEqualTo("testUser");
  }
}

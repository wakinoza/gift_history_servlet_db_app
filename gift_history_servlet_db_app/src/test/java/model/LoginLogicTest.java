package model;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import dao.UserDAO;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import bean.User;

class LoginLogicTest {

  private LoginLogic loginLogic;

  @Mock
  private UserDAO userDao;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    loginLogic = new LoginLogic(userDao);
  }

  @Test
  @DisplayName("正常系：バリデーションを通過し、DAOからユーザーが返されること")
  void testExecute_Success() {
    User mockUser = new User(1, "alice", "hashed_pass");
    when(userDao.select("alice", "password")).thenReturn(mockUser);

    User result = loginLogic.execute("alice", "password");

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("alice");
    verify(userDao, times(1)).select("alice", "password");
  }

  @ParameterizedTest
  @MethodSource("provideInvalidInputs")
  @DisplayName("バリデーション異常系：MethodSourceによる柔軟なテスト")
  void testExecute_WithMethodSource(String name, String pass) {
    User result = loginLogic.execute(name, pass);
    assertThat(result).isNull();
    verify(userDao, never()).select(anyString(), anyString());
  }

  static Stream<Arguments> provideInvalidInputs() {
    return Stream.of(Arguments.of(null, "password"), Arguments.of(" ", "password"),
        Arguments.of("a".repeat(51), "password"), Arguments.of("alice", "p".repeat(101)));
  }

  @ParameterizedTest
  @MethodSource("provideBlankInputs")
  @DisplayName("境界値テスト：名前やパスワードが未入力・空白の場合にnullを返すこと")
  void testExecute_BlankInput(String name, String pass) {
    // Exercise
    User result = loginLogic.execute(name, pass);

    // Verify
    assertThat(result).as("入力値 [%s, %s] のときnullであるべき", name, pass).isNull();
    verify(userDao, never()).select(anyString(), anyString());
  }

  static Stream<Arguments> provideBlankInputs() {
    return Stream.of(Arguments.of(null, "password"), Arguments.of("", "password"),
        Arguments.of(" ", "password"), Arguments.of("alice", null), Arguments.of("alice", ""),
        Arguments.of("alice", "　"));
  }
}

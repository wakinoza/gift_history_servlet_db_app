package dao;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.spi.InitialContextFactory;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import bean.User;
import util.PasswordUtil;

class UserDAOTest {

  private UserDAO dao;
  private static DataSource mockDs;

  @Mock
  private Connection mockCon;
  @Mock
  private PreparedStatement mockSt;
  @Mock
  private ResultSet mockRs;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    dao = new UserDAO();
    mockDs = mock(DataSource.class);

    Field field = DAO.class.getDeclaredField("ds");
    field.setAccessible(true);
    field.set(null, null);

    System.setProperty(Context.INITIAL_CONTEXT_FACTORY, MockFactory.class.getName());

    when(mockDs.getConnection()).thenReturn(mockCon);
    when(mockCon.prepareStatement(anyString())).thenReturn(mockSt);
    when(mockSt.executeQuery()).thenReturn(mockRs);
  }

  @Nested
  @DisplayName("selectメソッドのテスト")
  class SelectTest {

    @Test
    @DisplayName("正常系：ユーザー名とパスワードが一致する場合、Userインスタンスを返すこと")
    void testSelectSuccess() throws Exception {
      String rawPassword = "password123";
      String hashedPassword = PasswordUtil.hash(rawPassword);

      when(mockRs.next()).thenReturn(true);
      when(mockRs.getString("password")).thenReturn(hashedPassword);
      when(mockRs.getInt("id")).thenReturn(1);
      when(mockRs.getString("name")).thenReturn("Alice");

      User result = dao.select("Alice", rawPassword);

      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(1);
      assertThat(result.getName()).isEqualTo("Alice");
      verify(mockSt).setString(1, "Alice");
    }

    @Test
    @DisplayName("異常系：パスワードが一致しない場合、nullを返すこと")
    void testSelectWrongPassword() throws Exception {
      String correctHash = PasswordUtil.hash("correct_pass");

      when(mockRs.next()).thenReturn(true);
      when(mockRs.getString("password")).thenReturn(correctHash);

      User result = dao.select("Alice", "wrong_pass");

      assertThat(result).isNull();
    }

    @Test
    @DisplayName("異常系：ユーザーが存在しない場合、nullを返すこと")
    void testSelectUserNotFound() throws Exception {
      when(mockRs.next()).thenReturn(false);

      User result = dao.select("Unknown", "any_pass");

      assertThat(result).isNull();
    }

    @Test
    @DisplayName("異常系：DB接続エラー（例外）が発生した場合、nullを返すこと")
    void testSelectDatabaseError() throws Exception {
      when(mockDs.getConnection()).thenThrow(new RuntimeException("DB Error"));

      User result = dao.select("Alice", "pass");

      assertThat(result).isNull();
    }
  }

  public static class MockFactory implements InitialContextFactory {
    @Override
    public Context getInitialContext(Hashtable<?, ?> env) {
      Context context = mock(Context.class);
      try {
        when(context.lookup(anyString())).thenReturn(mockDs);
      } catch (Exception e) {
      }
      return context;
    }
  }
}

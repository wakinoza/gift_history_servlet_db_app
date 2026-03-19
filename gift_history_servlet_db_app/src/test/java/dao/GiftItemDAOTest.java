package dao;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import bean.GiftItem;

@ExtendWith(MockitoExtension.class)
public class GiftItemDAOTest {

  private GiftItemDAO dao;

  @Mock
  private DataSource mockDs;
  @Mock
  private Connection mockCon;
  @Mock
  private PreparedStatement mockPs;
  @Mock
  private ResultSet mockRs;

  @BeforeEach
  void setUp() throws Exception {
    dao = new GiftItemDAO();
    DAO.ds = mockDs;
  }

  @Nested
  @DisplayName("insertメソッドのテスト")
  class InsertTest {
    @Test
    @DisplayName("正常に1件挿入できること")
    void testInsertSuccess() throws Exception {
      when(mockDs.getConnection()).thenReturn(mockCon);
      when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
      when(mockPs.executeUpdate()).thenReturn(1);

      GiftItem item = new GiftItem();
      boolean result = dao.insert(item);

      assertThat(result).isTrue();
      verify(mockPs).executeUpdate();
    }

    @Test
    @DisplayName("例外発生時にfalseを返すこと(網羅用)")
    void testInsertFailure() throws Exception {
      when(mockDs.getConnection()).thenThrow(new SQLException("DB Error"));
      boolean result = dao.insert(new GiftItem());
      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("挿入行数が0の場合にfalseを返すこと(網羅用)")
    void testInsertZeroRows() throws Exception {
      when(mockDs.getConnection()).thenReturn(mockCon);
      when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
      when(mockPs.executeUpdate()).thenReturn(0);

      boolean result = dao.insert(new GiftItem());
      assertThat(result).isFalse();
    }
  }

  @Nested
  @DisplayName("updateReturnedメソッドのテスト")
  class UpdateTest {
    @Test
    @DisplayName("正常に更新できること")
    void testUpdateSuccess() throws Exception {
      when(mockDs.getConnection()).thenReturn(mockCon);
      when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
      when(mockPs.executeUpdate()).thenReturn(1);

      boolean result = dao.updateReturned("1");
      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("例外発生時にfalseを返すこと")
    void testUpdateFailure() throws Exception {
      when(mockDs.getConnection()).thenThrow(new SQLException("Update Error"));
      boolean result = dao.updateReturned("1");
      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("更新対象がなく0行の場合にfalseを返すこと(網羅用)")
    void testUpdateZeroRows() throws Exception {
      when(mockDs.getConnection()).thenReturn(mockCon);
      when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
      when(mockPs.executeUpdate()).thenReturn(0);

      boolean result = dao.updateReturned("999");
      assertThat(result).isFalse();
    }
  }

  @Nested
  @DisplayName("selectメソッドのテスト")
  class SelectTest {
    @Test
    @DisplayName("ID指定でデータが取得できること")
    void testSelectFound() throws Exception {
      when(mockDs.getConnection()).thenReturn(mockCon);
      when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
      when(mockPs.executeQuery()).thenReturn(mockRs);
      when(mockRs.next()).thenReturn(true);
      when(mockRs.getString("id")).thenReturn("1");

      GiftItem result = dao.select("1");

      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo("1");
    }

    @Test
    @DisplayName("データが存在しない場合にnullを返すこと")
    void testSelectNotFound() throws Exception {
      when(mockDs.getConnection()).thenReturn(mockCon);
      when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
      when(mockPs.executeQuery()).thenReturn(mockRs);
      when(mockRs.next()).thenReturn(false);

      GiftItem result = dao.select("999");
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("例外発生時にnullを返すこと(網羅用)")
    void testSelectFailure() throws Exception {
      when(mockDs.getConnection()).thenReturn(mockCon);
      when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
      when(mockPs.executeQuery()).thenThrow(new SQLException("Select Error"));

      GiftItem result = dao.select("1");
      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("selectAllメソッドのテスト")
  class SelectAllTest {
    @Test
    @DisplayName("全件取得できること")
    void testSelectAllSuccess() throws Exception {
      when(mockDs.getConnection()).thenReturn(mockCon);
      when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
      when(mockPs.executeQuery()).thenReturn(mockRs);
      when(mockRs.next()).thenReturn(true, true, false);

      List<GiftItem> list = dao.selectAll();

      assertThat(list).hasSize(2);
    }

    @Test
    @DisplayName("例外発生時に空のリストを返すこと(網羅用)")
    void testSelectAllFailure() throws Exception {
      when(mockDs.getConnection()).thenThrow(new SQLException("All Error"));
      List<GiftItem> list = dao.selectAll();
      assertThat(list).isEmpty();
    }
  }

  @Nested
  @DisplayName("deleteメソッドのテスト")
  class DeleteTest {
    @Test
    @DisplayName("正常に削除できること")
    void testDeleteSuccess() throws Exception {
      when(mockDs.getConnection()).thenReturn(mockCon);
      when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
      when(mockPs.executeUpdate()).thenReturn(1);

      boolean result = dao.delete("1");
      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("例外発生時にfalseを返すこと")
    void testDeleteFailure() throws Exception {
      when(mockDs.getConnection()).thenThrow(new SQLException("Delete Error"));
      boolean result = dao.delete("1");
      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("削除対象がなく0行の場合にfalseを返すこと(網羅用)")
    void testDeleteZeroRows() throws Exception {
      when(mockDs.getConnection()).thenReturn(mockCon);
      when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
      when(mockPs.executeUpdate()).thenReturn(0);

      boolean result = dao.delete("999");
      assertThat(result).isFalse();
    }
  }
}

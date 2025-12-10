package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import bean.GiftItem;

public class GiftItemDAO extends DAO {

  /**
   * . テーブルにGiftItemインスタンスの情報を挿入するメソッド
   *
   * @param GiftItem テーブルに挿入するGiftItemインスタンス
   * @return 挿入操作が完了したがどうかを示す真偽値
   */
  public boolean insert(GiftItem giftItem) {
    try (Connection con = getConnection()) {
      String sql =
          "INSERT INTO GiftItems (what, when, who, why, howMuch, needReturn, hasGaveReturn) VALUES (?, ?, ?, ?, ?, ?, ?)";
      PreparedStatement ps = con.prepareStatement(sql);

      ps.setString(1, giftItem.getWhat());
      ps.setString(2, giftItem.getWhen());
      ps.setString(3, giftItem.getWho());
      ps.setString(4, giftItem.getWhy());
      ps.setString(5, giftItem.getHowMuch());
      ps.setString(6, giftItem.getNeedReturn());
      ps.setString(7, giftItem.getHasGaveReturn());

      int result = ps.executeUpdate();
      if (result != 1) {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  /**
   * . 指定されたTodoItemインスタンスの進捗情報を変更するメソッド
   *
   * @param id 進捗情報を更新するTodoItemインスタンスのID
   * @return 変更操作が完了したがどうかを示す真偽値
   */
  public boolean updateProgress(String id) {
    try (Connection con = getConnection()) {
      String sql = "SELECT progress FROM todoItems WHERE id = ?";
      PreparedStatement ps = con.prepareStatement(sql);

      ps.setString(1, id);

      String nextProgress;

      try (ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
          String result = rs.getString("progress");
          if (result.equals("未実施")) {
            nextProgress = "実施中";
          } else if (result.equals("実施中")) {
            nextProgress = "完了済";
          } else {
            return delete(id);
          }

        } else {
          return false;
        }
      }

      String sql2 = "UPDATE todoItems SET progress = ? WHERE id = ?";
      PreparedStatement ps2 = con.prepareStatement(sql2);

      ps2.setString(1, nextProgress);
      ps2.setString(2, id);

      int result2 = ps2.executeUpdate();

      if (result2 != 1) {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  /**
   * . テーブルの全情報をListに変換するメソッド
   *
   * @return テーブルの全情報を格納したＬｉｓｔ
   */
  public List<TodoItem> selectAll() {
    List<TodoItem> todoItemList = new ArrayList<>();

    try (Connection con = getConnection()) {
      String sql = "SELECT * FROM todoItems";
      PreparedStatement ps = con.prepareStatement(sql);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          TodoItem todoItem = new TodoItem();
          todoItem.setId(rs.getString("id"));
          todoItem.setText(rs.getString("text"));
          todoItem.setProgress(rs.getString("progress"));
          todoItemList.add(todoItem);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return todoItemList;
  }

  /**
   * . 指定されたTodoItemインスタンスの情報を削除するメソッド
   *
   * @param id 削除するTodoItemインスタンスのID
   * @return 削除操作が完了したがどうかを示す真偽値
   */
  public boolean delete(String id) {
    try (Connection con = getConnection()) {
      String sql = "DELETE FROM todoItems WHERE id = ?";
      PreparedStatement ps = con.prepareStatement(sql);

      ps.setString(1, id);

      int result = ps.executeUpdate();
      if (result != 1) {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

}

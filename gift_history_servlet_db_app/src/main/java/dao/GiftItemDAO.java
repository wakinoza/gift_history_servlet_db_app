package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import bean.GiftItem;

/**
 * GiftItemsテーブル操作をつかさどるクラス
 */
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
          "INSERT INTO giftItems (what, whenis, who, why, howMuch, needReturn, hasGaveReturn) VALUES (?, ?, ?, ?, ?, ?, ?)";
      PreparedStatement ps = con.prepareStatement(sql);

      ps.setString(1, giftItem.getWhat());
      ps.setString(2, giftItem.getWhenis());
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
   * . 指定されたgiftItemインスタンスの進捗情報を変更するメソッド
   *
   * @param id 進捗情報を更新するgiftItemインスタンスのID
   * @return 変更操作が完了したがどうかを示す真偽値
   */
  public boolean updateReturned(String id) {
    try (Connection con = getConnection()) {

      String sql = "UPDATE giftItems SET hasGaveReturn = ? WHERE id = ?";
      PreparedStatement ps = con.prepareStatement(sql);

      ps.setString(1, "返礼済み");
      ps.setString(2, id);

      int result2 = ps.executeUpdate();

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
  public GiftItem select(String id) {

    try (Connection con = getConnection()) {
      String sql = "SELECT * FROM giftItems WHERE id = ?";
      PreparedStatement ps = con.prepareStatement(sql);

      ps.setString(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          GiftItem giftItem = new GiftItem();
          giftItem.setId(rs.getString("id"));
          giftItem.setWhat(rs.getString("what"));
          giftItem.setWhenis(rs.getString("whenis"));
          giftItem.setWho(rs.getString("who"));
          giftItem.setWhy(rs.getString("why"));
          giftItem.setHowMuch(rs.getString("howMuch"));
          giftItem.setNeedReturn(rs.getString("needReturn"));
          giftItem.setHasGaveReturn(rs.getString("hasGaveReturn"));

          return giftItem;
        } else {

        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * . テーブルの全情報をListに変換するメソッド
   *
   * @return テーブルの全情報を格納したＬｉｓｔ
   */
  public List<GiftItem> selectAll() {
    List<GiftItem> giftItemList = new ArrayList<>();

    try (Connection con = getConnection()) {
      String sql = "SELECT * FROM giftItems";
      PreparedStatement ps = con.prepareStatement(sql);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          GiftItem giftItem = new GiftItem();
          giftItem.setId(rs.getString("id"));
          giftItem.setWhat(rs.getString("what"));
          giftItem.setWhenis(rs.getString("whenis"));
          giftItem.setWho(rs.getString("who"));
          giftItem.setWhy(rs.getString("why"));
          giftItem.setHowMuch(rs.getString("howMuch"));
          giftItem.setNeedReturn(rs.getString("needReturn"));
          giftItem.setHasGaveReturn(rs.getString("hasGaveReturn"));

          giftItemList.add(giftItem);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return giftItemList;
  }

  /**
   * . 指定されたgiftItemインスタンスの情報を削除するメソッド
   *
   * @param id 削除するgiftItemインスタンスのID
   * @return 削除操作が完了したがどうかを示す真偽値
   */
  public boolean delete(String id) {
    try (Connection con = getConnection()) {
      String sql = "DELETE FROM giftItems WHERE id = ?";
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

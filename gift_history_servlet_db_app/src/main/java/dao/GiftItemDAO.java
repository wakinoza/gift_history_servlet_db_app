package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import bean.GiftItem;


/**
 * GiftItemsテーブル操作をつかさどるクラス.
 */
public class GiftItemDAO extends DAO {

  /**
   * . テーブルにGiftItemインスタンスの情報を挿入するメソッド
   *
   * @param giftItem テーブルに挿入するGiftItemインスタンス
   * @return 挿入操作が完了したがどうかを示す真偽値
   */
  public boolean insert(GiftItem giftItem) {
    boolean status = false;
    String sql =
        "INSERT INTO giftItems (what, whenis, who, why, howMuch, needReturn, hasGaveReturn) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

      ps.setString(1, giftItem.getWhat());
      ps.setString(2, giftItem.getWhenis());
      ps.setString(3, giftItem.getWho());
      ps.setString(4, giftItem.getWhy());
      ps.setString(5, giftItem.getHowMuch());
      ps.setString(6, giftItem.getNeedReturn());
      ps.setString(7, giftItem.getHasGaveReturn());

      int result = ps.executeUpdate();
      if (result == 1) {
        status = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return status;
  }

  /**
   * . 指定されたgiftItemインスタンスの進捗情報を変更するメソッド
   *
   * @param id 進捗情報を更新するgiftItemインスタンスのID
   * @return 変更操作が完了したがどうかを示す真偽値
   */
  public boolean updateReturned(String id) {
    boolean status = false;
    String sql = "UPDATE giftItems SET hasGaveReturn = ? WHERE id = ?";
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

      ps.setString(1, "返礼済み");
      ps.setString(2, id);

      int result2 = ps.executeUpdate();

      if (result2 == 1) {
        status = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return status;
  }

  /**
   * . テーブルの全情報をListに変換するメソッド
   *
   * @return テーブルの全情報を格納したＬｉｓｔ
   */
  public GiftItem select(String id) {
    GiftItem giftItem = null;
    String sql = "SELECT * FROM giftItems WHERE id = ?";
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setString(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          giftItem = new GiftItem();
          giftItem.setId(rs.getString("id"));
          giftItem.setWhat(rs.getString("what"));
          giftItem.setWhenis(rs.getString("whenis"));
          giftItem.setWho(rs.getString("who"));
          giftItem.setWhy(rs.getString("why"));
          giftItem.setHowMuch(rs.getString("howMuch"));
          giftItem.setNeedReturn(rs.getString("needReturn"));
          giftItem.setHasGaveReturn(rs.getString("hasGaveReturn"));

        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return giftItem;
  }

  /**
   * . テーブルの全情報をListに変換するメソッド
   *
   * @return テーブルの全情報を格納したＬｉｓｔ
   */
  public List<GiftItem> selectAll() {
    List<GiftItem> giftItemList = new ArrayList<>();
    String sql = "SELECT * FROM giftItems";
    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

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
        return giftItemList;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return giftItemList;
    }

  }

  /**
   * . 指定されたgiftItemインスタンスの情報を削除するメソッド
   *
   * @param id 削除するgiftItemインスタンスのID
   * @return 削除操作が完了したがどうかを示す真偽値
   */
  public boolean delete(String id) {
    boolean status = false;
    String sql = "DELETE FROM giftItems WHERE id = ?";

    try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

      ps.setString(1, id);
      int result = ps.executeUpdate();
      if (result == 1) {
        status = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
      status = false;
    }
    return status;
  }

}

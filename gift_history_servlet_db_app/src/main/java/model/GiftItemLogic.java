package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import bean.GiftItem;
import dao.GiftItemDAO;

/**
 * GiteItemの処理を司るビジネスロジッククラス.
 */
public class GiftItemLogic {

  /**
   * GiftItemインスタンスを作成するメソッド
   *
   * @param what 何を頂いたか
   * @param whenis いつ頂いたか
   * @param who だれから頂いたか
   * @param why 頂いた理由
   * @param howMuch 予想価格
   * @param needReturn 返礼が必要かどうか
   * @return 新しいGiftItemクラス
   */
  public GiftItem createNewGiftItem(String what, String whenis, String who, String why,
      String howMuch, String needReturn) {

    if (what == null || what.length() == 0) {
      what = "未回答";
    }

    if (whenis == null || whenis.length() == 0) {
      whenis = "未回答";
    } else {
      DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate localDateWhen = LocalDate.parse(whenis, formatter2);
      DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
      whenis = formatter3.format(localDateWhen);
    }

    if (who == null || who.length() == 0) {
      who = "未回答";
    }

    if (why == null || why.length() == 0) {
      why = "未回答";
    }

    if (howMuch == null || howMuch.length() == 0) {
      howMuch = "未回答";
    }

    if (needReturn == null || needReturn.length() == 0) {
      needReturn = "未回答";
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    LocalDateTime now = LocalDateTime.now();
    formatter.format(now);

    GiftItem giftItem = new GiftItem(what, whenis, who, why, howMuch, needReturn);

    return giftItem;
  }

  /**
   * 指定のidを持つGiftItemインスタンスを検索するメソッド.
   *
   * @param id 検索したいGiftItemインスタンスのid
   * @return 検索で取得したGiftItemインスタンス
   */
  public GiftItem getGiftItem(String id) {
    GiftItemDAO giftItemDao = new GiftItemDAO();
    return giftItemDao.select(id);
  }

  /**
   * テーブルのすべてのGiftIteをListに抽出するメソッド
   * 
   * @return GiftItemのList
   */
  public List<GiftItem> getAllGiftItem() {
    GiftItemDAO giftItemDao = new GiftItemDAO();
    return giftItemDao.selectAll();
  }


  /**
   * GiftItemインスタンスをgiftoItemListに追加するメソッド.
   *
   * @param giftItem GiftItemインスタンス
   * @return 追加操作が完了したがどうかを示す真偽値
   */
  public boolean add(GiftItem giftItem) {
    GiftItemDAO giftItemDao = new GiftItemDAO();
    return giftItemDao.insert(giftItem);
  }

  /**
   * 指定のidを持つGiftItemインスタンスのhasGaveReturnフィールドを変更するメソッド.
   *
   * @param id 検索したいGiftItemインスタンスのid
   * @return 変更操作が完了したがどうかを示す真偽値
   */
  public boolean returned(String id) {
    GiftItemDAO giftItemDao = new GiftItemDAO();
    return giftItemDao.updateReturned(id);
  }

  /**
   * 指定のidを持つGiftItemインスタンスを削除するメソッド.
   *
   * @param id 検索したいGiftItemインスタンスのid
   * @return 削除操作が完了したがどうかを示す真偽値
   */
  public boolean remove(String id) {
    GiftItemDAO giftItemDao = new GiftItemDAO();
    return giftItemDao.delete(id);
  }
}


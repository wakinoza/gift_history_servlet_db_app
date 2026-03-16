package model;

import dao.GiftItemDAO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import bean.GiftItem;



/**
 * GiftItemインスタンスの処理をつかさどるクラス.
 */
public class GiftItemLogic {

  /**
   * 入力テキストの最大長
   */
  private final int MAX_TEXT_LENGTH = 1000;

  /**
   * GiftIteDAOのインスタンス
   */
  private final GiftItemDAO giftItemDao;

  /**
   * .コンストラクタ
   *
   * @param giftItemDao
   */
  public GiftItemLogic(GiftItemDAO giftItemDao) {
    this.giftItemDao = giftItemDao;
  }

  /**
   * GiftItemインスタンスを作成するメソッド.
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

    what = (what == null || what.isBlank()) ? "未回答" : what;
    who = (who == null || who.isBlank()) ? "未回答" : who;
    why = (why == null || why.isBlank()) ? "未回答" : why;
    howMuch = (howMuch == null || howMuch.isBlank()) ? "未回答" : howMuch;
    needReturn = (needReturn == null || needReturn.isBlank()) ? "未回答" : needReturn;

    if (whenis == null || whenis.isBlank()) {
      whenis = "未回答";
    } else {
      if (!isValidDate(whenis)) {
        return null;
      }
      LocalDate localDateWhen = LocalDate.parse(whenis, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      whenis = DateTimeFormatter.ofPattern("yyyy年MM月dd日").format(localDateWhen);
    }

    if (!isValidGiftInput(what, who, why, howMuch)) {
      return null;
    }
    return new GiftItem(what, whenis, who, why, howMuch, needReturn);
  }

  /**
   * 入力値のバリデーションを行うメソッド
   * 
   * @param what 何を
   * @param whenis いつ
   * @param who だれから
   * @param why なぜ
   * @param howMuch いくらくらい
   * @return バリデーションの結果
   */
  private boolean isValidGiftInput(String what, String who, String why, String howMuch) {

    if (isExceeding(what) || isExceeding(who) || isExceeding(why) || isExceeding(howMuch)) {
      return false;
    }

    return true;
  }

  /**
   * 日付形式のみをチェックする専門メソッド
   */
  private boolean isValidDate(String whenis) {
    try {
      LocalDate.parse(whenis, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  /**
   * 文字列のバリデーションを行うメソッド
   * 
   * @param s 文字列
   * @return バリデーション結果
   */
  private boolean isExceeding(String s) {
    return s != null && s.length() > MAX_TEXT_LENGTH;
  }

  /**
   * . IDをチェックするメソッド
   * 
   * @param id ID
   * @return バリデーション結果
   */
  private boolean isInvalidId(String id) {
    return id == null || !id.matches("^[0-9]+$");
  }

  /**
   * 指定のidを持つGiftItemインスタンスを検索するメソッド.
   *
   * @param id 検索したいGiftItemインスタンスのid
   * @return 検索で取得したGiftItemインスタンス
   */
  public GiftItem findGiftItem(String id) {
    if (isInvalidId(id)) {
      return null;
    }
    return giftItemDao.select(id);
  }

  /**
   * テーブルのすべてのGiftIteをListに抽出するメソッド.
   * 
   * @return GiftItemのList
   */
  public List<GiftItem> getAllGiftItem() {
    return giftItemDao.selectAll();
  }


  /**
   * GiftItemインスタンスをgiftoItemListに追加するメソッド.
   *
   * @param giftItem GiftItemインスタンス
   * @return 追加操作が完了したがどうかを示す真偽値
   */
  public boolean add(GiftItem giftItem) {
    return giftItemDao.insert(giftItem);
  }

  /**
   * 指定のidを持つGiftItemインスタンスのhasGaveReturnフィールドを変更するメソッド.
   *
   * @param id 検索したいGiftItemインスタンスのid
   * @return 変更操作が完了したがどうかを示す真偽値
   */
  public boolean returned(String id) {
    return giftItemDao.updateReturned(id);
  }

  /**
   * 指定のidを持つGiftItemインスタンスを削除するメソッド.
   *
   * @param id 検索したいGiftItemインスタンスのid
   * @return 削除操作が完了したがどうかを示す真偽値
   */
  public boolean remove(String id) {
    if (isInvalidId(id)) {
      return false;
    }
    return giftItemDao.delete(id);
  }
}


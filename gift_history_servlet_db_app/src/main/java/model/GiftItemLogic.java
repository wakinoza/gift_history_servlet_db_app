package model;

import java.util.List;
import bean.GiftItem;
import dao.GiftItemDAO;

/**
 * GiteItemの処理を司るビジネスロジッククラス.
 */
public class GiftItemLogic {

  /**
   * 指定のidを持つGiftItemインスタンスを検索するメソッド.
   *
   * @param id 検索したいGiftItemインスタンスのid
   * @param giftItemList GiftItemのList
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
   * @param giftItemList GiftItemのList
   */
  public void add(GiftItem giftItem, List<GiftItem> giftItemList) {
    giftItemList.add(0, giftItem);
  }

  /**
   * 指定のidを持つGiftItemインスタンスのhasGaveReturnフィールドを変更するメソッド.
   *
   * @param id 検索したいGiftItemインスタンスのid
   * @param giftItemList GiftItemのList
   */
  public void returned(String id, List<GiftItem> giftItemList) {
    GiftItem currentGiftItem = getGiftItem(id, giftItemList);
    currentGiftItem.setHasGaveReturn("返礼済み");
  }

  /**
   * 指定のidを持つGiftItemインスタンスを削除するメソッド.
   *
   * @param id 検索したいGiftItemインスタンスのid
   * @param giftItemList GiftItemのList
   */
  public void remove(String id, List<GiftItem> giftItemList) {
    GiftItem currentGiftItem = getGiftItem(id, giftItemList);
    giftItemList.remove(giftItemList.indexOf(currentGiftItem));
  }
}


package model;

import java.util.List;
import bean.GiftItem;

/**
 * Giteitemの処理を司るビジネスロジッククラス.
 */
public class GiftItemLogic {

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
   * 指定のidを持つGiftItemインスタンスを検索するメソッド.
   *
   * @param id 検索したいGiftItemインスタンスのid
   * @param giftItemList GiftItemのList
   */
  public GiftItem getGiftItem(String id, List<GiftItem> giftItemList) {
    for (GiftItem giftItem : giftItemList) {
      if (giftItem.getId().equals(id)) {
        return giftItem;
      }
    }
    throw new java.util.NoSuchElementException("指定されたID (" + id + ") のGiftItemが見つかりません。");
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


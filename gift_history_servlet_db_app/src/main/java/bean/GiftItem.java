package bean;

import java.io.Serializable;

/**
 * 頂き物の情報を保持するクラス.
 */
public class GiftItem implements Serializable {
  private String id;
  private String what;
  private String when;
  private String who;
  private String why;
  private String howMuch;
  private String needReturn;
  private String hasGaveReturn = "未返礼";

  /**
   * コンストラクタ.
   */
  public GiftItem() {}

  /**
   * コンストラクタ.
   *
   * @param id ID
   * @param what 頂いたもの
   * @param when 頂いた日
   * @param who 贈り主
   * @param why 贈られた理由
   * @param howMuch いくらぐらい
   * @param needReturn 返礼が必要か
   */
  public GiftItem(String id, String what, String when, String who, String why, String howMuch,
      String needReturn) {
    this.id = id;
    this.what = what;
    this.when = when;
    this.who = who;
    this.why = why;
    this.howMuch = howMuch;
    this.needReturn = needReturn;
  }

  /**
   * getterメソッド.
   */
  public String getId() {
    return this.id;
  }

  public String getWhat() {
    return this.what;
  }

  public String getWhen() {
    return this.when;
  }

  public String getWho() {
    return this.who;
  }

  public String getWhy() {
    return this.why;
  }

  public String getHowMuch() {
    return this.howMuch;
  }

  public String getNeedReturn() {
    return this.needReturn;
  }

  public String getHasGaveReturn() {
    return this.hasGaveReturn;
  }


  /**
   * setterメソッド.
   */
  public void setId(String id) {
    this.id = id;
  }

  public void setWhat(String what) {
    this.what = what;
  }

  public void setWhen(String when) {
    this.when = when;
  }

  public void setWho(String who) {
    this.who = who;
  }

  public void setWhy(String why) {
    this.why = why;
  }

  public void setHowMuch(String howMuch) {
    this.howMuch = howMuch;
  }

  public void setNeedReturn(String needReturn) {
    this.needReturn = needReturn;
  }

  public void setHasGaveReturn(String hasGaveReturn) {
    this.hasGaveReturn = hasGaveReturn;
  }

}

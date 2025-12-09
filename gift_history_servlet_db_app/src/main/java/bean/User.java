package bean;

import java.io.Serializable;

/**
 * ユーザー情報を保持するクラス.
 */
public class User implements Serializable {
  /** ユーザーID. */
  private String id;

  /** ユーザー名. */
  private String name;

  /** パスワード. */
  private String pass;

  /**
   * コンストラクタ.
   */
  public User() {}

  /**
   * コンストラクタ.
   *
   * @param name ユーザー名
   * @param pass パスワード
   */
  public User(String name, String pass) {
    this.name = name;
    this.pass = pass;
  }

  /**
   * getterメソッド.
   */
  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getPass() {
    return this.pass;
  }

  /**
   * setterメソッド
   */
  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPass(String pass) {
    this.pass = pass;
  }

}



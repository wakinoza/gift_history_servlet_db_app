package model;

import bean.User;

/**
 * ログイン時の認証を行うクラス.
 */
public class LoginLogic {

  /**
   * ログイン時の認証を行うメソッド.
   *
   * @param user Userクラスのインスタンス
   * @return 認証の可否
   */
  public boolean execute(User user) {
    if (user.getPass().equals("1234")) {
      return true;
    }
    return false;
  }
}


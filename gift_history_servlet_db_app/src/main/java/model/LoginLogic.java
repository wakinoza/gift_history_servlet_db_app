package model;

import dao.UserDAO;

/**
 * ログイン時の認証を行うクラス.
 */
public class LoginLogic {

  /**
   * ログイン時の認証を行うメソッド.
   *
   * @param name ユーザー名
   * @param pass パスワード
   * @return 認証されたUserインスタンス
   */
  public User execute(String name, String pass) {
    UserDAO userDao = new UserDAO();
    return userDao.select(name, pass);
  }
}


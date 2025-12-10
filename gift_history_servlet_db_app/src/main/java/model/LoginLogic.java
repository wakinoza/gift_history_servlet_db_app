package model;

import bean.User;
import dao.UserDAO;

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
  public User execute(String name, String pass) {
    UserDAO userDao = new UserDAO();
    return userDao.select(name, pass);
  }
}


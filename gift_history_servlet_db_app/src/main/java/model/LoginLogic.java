package model;

import dao.UserDAO;
import bean.User;

/**
 * ログイン時の認証を行うクラス.
 */
public class LoginLogic {
  /** ユーザ名の最大サイズ */
  final int MAX_NAME_LENGTH = 50;

  /** パスワードの最大サイズ */
  final int MAX_PASS_LENGTH = 100;

  /** UserDAOインスタンス */
  private final UserDAO userDao;

  /**
   * . コンストラクタ
   *
   * @param userDao UserDAOのインスタンス
   */
  public LoginLogic(UserDAO userDao) {
    this.userDao = userDao;
  }

  /**
   * ログイン時の認証を行うメソッド.
   *
   * @param name ユーザー名
   * @param pass パスワード
   * @return 認証されたUserインスタンス
   */
  public User execute(String name, String pass) {


    if (!isValidInput(name, pass)) {
      return null;
    }
    return userDao.select(name, pass);
  }

  /**
   * 入力値のバリデーションを行うメソッド.
   * 
   * @param name ユーザー名
   * @param pass パスワード
   * @return バリデーション結果
   */
  private boolean isValidInput(String name, String pass) {
    if (name == null || name.isBlank() || pass == null || pass.isBlank()) {
      return false;
    }

    if (name.length() > MAX_NAME_LENGTH || pass.length() > MAX_PASS_LENGTH) {
      return false;
    }

    return true;
  }
}


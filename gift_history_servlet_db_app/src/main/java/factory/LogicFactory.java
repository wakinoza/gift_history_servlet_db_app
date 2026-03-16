package factory;

import dao.UserDAO;
import model.LoginLogic;

/**
 * 各ロジッククラスのインスタンスを生成するファクトリクラス.
 */
public class LogicFactory {

  /**
   * LoginLogicのインスタンスを生成します.
   *
   * @return UserDAOを注入済みのLoginLogic
   */
  public static LoginLogic createLoginLogic() {
    // ここでDAOを生成してLogicに渡す
    return new LoginLogic(new UserDAO());
  }

  // 他のロジック（GiftLogicなど）が必要になったらここに追加していく
}

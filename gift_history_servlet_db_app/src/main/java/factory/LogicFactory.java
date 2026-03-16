package factory;

import dao.GiftItemDAO;
import dao.UserDAO;
import model.GiftItemLogic;
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
    return new LoginLogic(new UserDAO());
  }

  /**
   * GiftItemDAOのインスタンスを生成する
   *
   * @return GiftItemDAOを注入済みのGiftItemLogic
   */
  public static GiftItemLogic createGiftItemLogic() {
    return new GiftItemLogic(new GiftItemDAO());
  }
}

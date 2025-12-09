package servlet;

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import bean.User;
import model.LoginLogic;


/**
 * ログイン処理するサーブレットクラス.
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * . @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String name = request.getParameter("name");
    String pass = request.getParameter("pass");
    User user = new User(name, pass);
    LoginLogic loginLogic = new LoginLogic();
    boolean isLogin = loginLogic.execute(user);

    if (isLogin) {
      HttpSession session = request.getSession();
      session.setAttribute("loginUser", user);
    }
    RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/loginResult.jsp");
    dispatcher.forward(request, response);
  }
}



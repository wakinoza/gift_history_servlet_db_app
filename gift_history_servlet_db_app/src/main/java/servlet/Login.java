package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.LoginLogic;
import bean.User;
import util.CsrfUtil;


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
    LoginLogic loginLogic = new LoginLogic();
    User loginUser = null;
    loginUser = loginLogic.execute(name, pass);

    if (loginUser != null) {
      HttpSession oldSession = request.getSession(false);
      if (oldSession != null) {
        oldSession.invalidate();
      }
      HttpSession newSession = request.getSession(true);
      newSession.setAttribute("loginUser", loginUser);

      String csrfToken = CsrfUtil.generateToken();
      newSession.setAttribute("csrfToken", csrfToken);

      String sessionId = newSession.getId();
      String contextPath = request.getContextPath();
      String cookieHeader = String.format("JSESSIONID=%s; Path=%s; HttpOnly; SameSite=Lax",
          sessionId, (contextPath.isEmpty() ? "/" : contextPath));
      response.setHeader("Set-Cookie", cookieHeader);
    }
    request.getRequestDispatcher("WEB-INF/jsp/loginResult.jsp").forward(request, response);
  }
}



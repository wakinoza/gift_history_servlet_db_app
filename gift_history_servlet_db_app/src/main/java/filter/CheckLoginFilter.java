package filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.User;

/**
 * ログイン状態かを確認するフィルタークラス.
 */
public class CheckLoginFilter extends HttpFilter {

  /**
   * doFilterメソッド.
   */
  @Override
  public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpSession session = request.getSession(false);
    User loginUser = (session != null) ? (User) session.getAttribute("loginUser") : null;

    if (loginUser == null) {
      response.sendRedirect("index.jsp");
      return;
    }

    chain.doFilter(request, response);
  }

}

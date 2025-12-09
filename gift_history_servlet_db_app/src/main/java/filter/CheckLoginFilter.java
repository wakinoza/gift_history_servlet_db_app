package filter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import bean.User;

/**
 * ログイン状態かを確認するフィルタークラス.
 */
@WebFilter("/Main, /NewGift, /ViewGifts")
public class CheckLoginFilter extends HttpFilter {

  /**
   * doFilterメソッド.
   */
  @Override
  public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpSession session = request.getSession();
    User loginUser = (User) session.getAttribute("loginUser");

    if (loginUser == null) {
      response.sendRedirect("index.jsp");

    }

    chain.doFilter(request, response);
  }

}

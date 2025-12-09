package filter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * エンコーディングを行うフィルタークラス.
 */
@WebFilter("/*")
public class SetEncodingFilter extends HttpFilter {

  /**
   * doFilterメソッド.
   */
  @Override
  public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html, charset=UTF-8");
    chain.doFilter(request, response);
  }

}

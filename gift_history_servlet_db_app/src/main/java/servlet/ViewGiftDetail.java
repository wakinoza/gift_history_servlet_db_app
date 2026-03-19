package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.GiftItemLogic;
import factory.LogicFactory;

/**
 * 頂き物の詳細情報を参照のサーブレット.
 */
@WebServlet("/ViewGiftDetail")
public class ViewGiftDetail extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(false);
    if (session != null) {
      String csrfToken = (String) session.getAttribute("csrfToken");
      request.setAttribute("csrfToken", csrfToken);
    }

    request.getRequestDispatcher("WEB-INF/jsp/viewGiftDetail.jsp").forward(request, response);

  }


  /**
   * doPostメソッド.
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(false);
    String sessionToken = (session != null) ? (String) session.getAttribute("csrfToken") : null;
    String requestToken = request.getParameter("csrfToken");

    if (sessionToken == null || !sessionToken.equals(requestToken)) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN, "不正なリクエストを検知しました。");
      return;
    }

    String id = request.getParameter("id");
    String action = request.getParameter("action");
    GiftItemLogic giftItemLogic = LogicFactory.createGiftItemLogic();

    String redirectPath = null;
    String errorMsg = null;

    try {
      boolean result = false;

      if ("returned".equals(action)) {
        result = giftItemLogic.returned(id);
      } else if ("remove".equals(action)) {
        result = giftItemLogic.remove(id);
      }
      if (result) {
        redirectPath = "Main";
      } else {
        errorMsg = "指定した処理が実行できませんでした";
      }

    } catch (Exception e) {
      errorMsg = "処理中にエラーが発生しました：" + e.getMessage();
      e.printStackTrace();
    }

    if (redirectPath != null) {
      response.sendRedirect(redirectPath);
    } else {
      request.setAttribute("errorMsg", errorMsg);
      request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
    }

  }

}

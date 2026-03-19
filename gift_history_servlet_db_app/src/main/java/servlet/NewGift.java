package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.GiftItemLogic;
import bean.GiftItem;
import factory.LogicFactory;


/**
 * 頂き物の情報の新規登録を行うサーブレットクラス.
 */
@WebServlet("/NewGift")
public class NewGift extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * doGetメソッド.
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(false);
    if (session != null) {
      String csrfToken = (String) session.getAttribute("csrfToken");
      request.setAttribute("csrfToken", csrfToken);
    }

    request.getRequestDispatcher("WEB-INF/jsp/newGift.jsp").forward(request, response);
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
      response.sendError(HttpServletResponse.SC_FORBIDDEN, "不正なリクエストです。");
      return;
    }

    String what = request.getParameter("what");
    String whenis = request.getParameter("whenis");
    String who = request.getParameter("who");
    String why = request.getParameter("why");
    String howMuch = request.getParameter("howMuch");
    String needReturn = request.getParameter("needReturn");

    GiftItemLogic giftItemLogic = LogicFactory.createGiftItemLogic();
    String redirectPath = null;
    String forwardPath = null;
    String errorMsg = null;

    try {
      GiftItem newGiftItem =
          giftItemLogic.createNewGiftItem(what, whenis, who, why, howMuch, needReturn);

      if (newGiftItem == null) {
        errorMsg = "入力内容が正しくありません。";
        forwardPath = "/WEB-INF/jsp/newGift.jsp";
      } else {
        boolean result = giftItemLogic.add(newGiftItem);
        if (result) {
          redirectPath = "Main";
        } else {
          errorMsg = "いただきものが追加できませんでした";
          forwardPath = "/WEB-INF/jsp/error.jsp";
        }
      }
    } catch (Exception e) {
      errorMsg = "処理中にエラーが発生しました：" + e.getMessage();
      e.printStackTrace();
      forwardPath = "/WEB-INF/jsp/error.jsp";
    }

    if (redirectPath != null) {
      response.sendRedirect(redirectPath);
    } else {
      request.setAttribute("errorMsg", errorMsg);
      request.getRequestDispatcher(forwardPath).forward(request, response);
    }

  }

}

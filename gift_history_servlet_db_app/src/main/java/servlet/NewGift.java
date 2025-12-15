package servlet;

import java.io.IOException;
import java.util.NoSuchElementException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import bean.GiftItem;
import model.GiftItemLogic;

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

    request.getRequestDispatcher("WEB-INF/jsp/newGift.jsp").forward(request, response);
  }

  /**
   * doPostメソッド.
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String what = request.getParameter("what");
    String whenis = request.getParameter("when");
    String who = request.getParameter("who");
    String why = request.getParameter("why");
    String howMuch = request.getParameter("howMuch");
    String needReturn = request.getParameter("needReturn");

    GiftItemLogic giftItemLogic = new GiftItemLogic();
    try {
      GiftItem newGiftItem =
          giftItemLogic.createNewGiftItem(what, whenis, who, why, howMuch, needReturn);
      boolean result = giftItemLogic.add(newGiftItem);

      if (result) {
        response.sendRedirect("Main");

      } else {
        String errorMsg = "いただきものが追加できませんでした";
        request.setAttribute("errorMsg", errorMsg);
        request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
      }

    } catch (NoSuchElementException e) {
      String errorMsg = "処理中にエラーが発生しました：" + e.getMessage();
      request.setAttribute("errorMsg", errorMsg);
      e.printStackTrace();
      request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
    }

  }

}

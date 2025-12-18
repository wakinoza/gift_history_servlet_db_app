package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;
import model.GiftItemLogic;

/**
 * 頂き物の詳細情報を参照のサーブレット.
 */
@WebServlet("/ViewGiftDetail")
public class ViewGiftDetail extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    request.getRequestDispatcher("WEB-INF/jsp/viewGiftDetail.jsp").forward(request, response);

  }


  /**
   * doPostメソッド.
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {


    String id = request.getParameter("id");
    String action = request.getParameter("action");
    GiftItemLogic giftItemLogic = new GiftItemLogic();

    try {
      boolean result;
      if ("returned".equals(action)) {
        result = giftItemLogic.returned(id);
      } else {
        result = giftItemLogic.remove(id);
      }

      if (result) {
        response.sendRedirect("Main");
      } else {
        String errorMsg = " 指定した処理が実行できませんでした";
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

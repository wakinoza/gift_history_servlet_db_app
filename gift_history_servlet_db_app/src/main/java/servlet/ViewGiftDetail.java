package servlet;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import bean.GiftItem;
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
  @SuppressWarnings("unchecked")
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    ServletContext application = this.getServletContext();
    List<GiftItem> giftItemList = (List<GiftItem>) application.getAttribute("giftItemList");
    if (giftItemList == null) {
      giftItemList = new java.util.ArrayList<>();
      application.setAttribute("giftItemList", giftItemList);
    }

    String id = request.getParameter("id");
    String action = request.getParameter("action");
    GiftItemLogic giftItemLogic = new GiftItemLogic();

    try {
      if ("returned".equals(action)) {
        giftItemLogic.returned(id, giftItemList);
      } else if ("remove".equals(action)) {
        giftItemLogic.remove(id, giftItemList);
      }
      application.setAttribute("giftItemList", giftItemList);
      response.sendRedirect("Main");

    } catch (NoSuchElementException e) {
      String errorMsg = "処理中にエラーが発生しました：" + e.getMessage();
      request.setAttribute("errorMsg", errorMsg);
      e.printStackTrace();
      request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
    }

  }

}

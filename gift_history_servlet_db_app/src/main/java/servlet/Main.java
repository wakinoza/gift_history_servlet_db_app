package servlet;

import java.io.IOException;
import java.util.ArrayList;
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
 * メイン画面の処理を行うサーブレットクラス.
 */
@WebServlet("/Main")
public class Main extends HttpServlet {
  private static final long serialVersionUID = 1L;


  /**
   * . @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  @SuppressWarnings("unchecked")
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    ServletContext application = this.getServletContext();
    List<GiftItem> giftItemList = (List<GiftItem>) application.getAttribute("giftItemList");

    if (giftItemList == null) {
      giftItemList = new ArrayList<>();
      application.setAttribute("giftItemList", giftItemList);
    }

    request.getRequestDispatcher("WEB-INF/jsp/main.jsp").forward(request, response);

  }

  /**
   * . @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  @SuppressWarnings("unchecked")
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    ServletContext application = this.getServletContext();
    List<GiftItem> giftItemList = (List<GiftItem>) application.getAttribute("giftItemList");

    String id = request.getParameter("id");

    GiftItemLogic giftItemLogic = new GiftItemLogic();
    GiftItem currentGiftItem;

    try {
      currentGiftItem = giftItemLogic.getGiftItem(id, giftItemList);
      application.setAttribute("currentGiftItem", currentGiftItem);
      response.sendRedirect("ViewGiftDetail");

    } catch (NoSuchElementException e) {
      String errorMsg = "処理中にエラーが発生しました：" + e.getMessage();
      request.setAttribute("errorMsg", errorMsg);
      e.printStackTrace();
      request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
    }
  }



}

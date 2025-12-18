package servlet;


import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import model.GiftItem;
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
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    GiftItemLogic giftItemLogic = new GiftItemLogic();
    List<GiftItem> giftItemList = giftItemLogic.getAllGiftItem();

    if (giftItemList == null) {
      giftItemList = new ArrayList<>();
    }
    request.setAttribute("giftItemList", giftItemList);
    request.getRequestDispatcher("WEB-INF/jsp/main.jsp").forward(request, response);

  }

  /**
   * . @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {


    String id = request.getParameter("id");

    GiftItemLogic giftItemLogic = new GiftItemLogic();
    GiftItem currentGiftItem;

    try {
      currentGiftItem = giftItemLogic.findGiftItem(id);

      if (currentGiftItem != null) {
        ServletContext application = this.getServletContext();
        application.setAttribute("currentGiftItem", currentGiftItem);
        response.sendRedirect("ViewGiftDetail");
      } else {
        String errorMsg = "指定したいただきもの情報がみつかりませんでした";
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

package servlet;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.GiftItemLogic;
import bean.GiftItem;
import factory.LogicFactory;


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
    GiftItemLogic giftItemLogic = LogicFactory.createGiftItemLogic();
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

    GiftItemLogic giftItemLogic = LogicFactory.createGiftItemLogic();
    String errorMsg = null;
    String forwardPath = "/WEB-INF/jsp/error.jsp";

    try {
      GiftItem currentGiftItem = giftItemLogic.findGiftItem(id);

      if (currentGiftItem != null) {
        HttpSession session = request.getSession();
        session.setAttribute("currentGiftItem", currentGiftItem);
        response.sendRedirect("ViewGiftDetail");
        return;

      } else {
        errorMsg = "指定したいただきもの情報がみつかりませんでした";
      }

    } catch (Exception e) {
      errorMsg = "処理中にエラーが発生しました：" + e.getMessage();
      e.printStackTrace();
    }

    if (errorMsg != null) {
      request.setAttribute("errorMsg", errorMsg);
      request.getRequestDispatcher(forwardPath).forward(request, response);
    }
  }

}

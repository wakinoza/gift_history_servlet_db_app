package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import jakarta.servlet.ServletContext;
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
  @SuppressWarnings("unchecked")
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    ServletContext application = this.getServletContext();
    List<GiftItem> giftItemList = (List<GiftItem>) application.getAttribute("giftItemList");

    if (giftItemList == null) {
      giftItemList = new java.util.ArrayList<>();
      application.setAttribute("giftItemList", giftItemList);
    }

    String what = request.getParameter("what");
    if (what == null || what.length() == 0) {
      what = "未回答";
    }

    String when = request.getParameter("when");
    if (when == null || when.length() == 0) {
      when = "未回答";
    } else {
      DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate localDateWhen = LocalDate.parse(when, formatter2);
      DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
      when = formatter3.format(localDateWhen);
    }

    String who = request.getParameter("who");
    if (who == null || who.length() == 0) {
      who = "未回答";
    }

    String why = request.getParameter("why");
    if (why == null || why.length() == 0) {
      why = "未回答";
    }

    String howMuch = request.getParameter("howMuch");
    if (howMuch == null || howMuch.length() == 0) {
      howMuch = "未回答";
    }

    String needReturn = request.getParameter("needReturn");
    if (needReturn == null || needReturn.length() == 0) {
      needReturn = "未回答";
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    LocalDateTime now = LocalDateTime.now();
    String id = formatter.format(now);

    GiftItem gitfItem = new GiftItem(id, what, when, who, why, howMuch, needReturn);

    GiftItemLogic giftItemLogic = new GiftItemLogic();
    giftItemLogic.add(gitfItem, giftItemList);

    application.setAttribute("giftItemList", giftItemList);
    response.sendRedirect("Main");
  }

}

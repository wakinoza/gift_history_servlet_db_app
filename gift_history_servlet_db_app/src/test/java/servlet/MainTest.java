package servlet;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import model.GiftItemLogic;
import bean.GiftItem;
import factory.LogicFactory;

@ExtendWith(MockitoExtension.class)
class MainTest {

  private Main mainServlet;

  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private HttpSession session;
  @Mock
  private RequestDispatcher dispatcher;
  @Mock
  private GiftItemLogic mockLogic;
  @Mock
  private GiftItem mockItem;

  @BeforeEach
  void setUp() {
    mainServlet = new Main();
  }

  @Nested
  @DisplayName("doGetメソッドのテスト")
  class DoGetTest {
    @Test
    @DisplayName("一覧取得：データが存在する場合、requestにセットしてmain.jspへ遷移すること")
    void testDoGetSuccess() throws ServletException, IOException {
      try (MockedStatic<LogicFactory> factoryMock = mockStatic(LogicFactory.class)) {
        List<GiftItem> list = new ArrayList<>();
        list.add(mockItem);

        factoryMock.when(LogicFactory::createGiftItemLogic).thenReturn(mockLogic);
        when(mockLogic.getAllGiftItem()).thenReturn(list);
        when(request.getRequestDispatcher("WEB-INF/jsp/main.jsp")).thenReturn(dispatcher);

        mainServlet.doGet(request, response);

        verify(request).setAttribute("giftItemList", list);
        verify(dispatcher).forward(request, response);
      }
    }

    @Test
    @DisplayName("一覧取得：データがnullの場合、空のリストをセットすること(網羅用)")
    void testDoGetNullList() throws ServletException, IOException {
      try (MockedStatic<LogicFactory> factoryMock = mockStatic(LogicFactory.class)) {
        factoryMock.when(LogicFactory::createGiftItemLogic).thenReturn(mockLogic);
        when(mockLogic.getAllGiftItem()).thenReturn(null); // nullを返す
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        mainServlet.doGet(request, response);

        verify(request).setAttribute(eq("giftItemList"), any(List.class));
      }
    }
  }

  @Nested
  @DisplayName("doPostメソッドのテスト")
  class DoPostTest {
    @Test
    @DisplayName("詳細表示：指定IDが見つかった場合、sessionに保存してリダイレクトすること")
    void testDoPostSuccess() throws ServletException, IOException {
      try (MockedStatic<LogicFactory> factoryMock = mockStatic(LogicFactory.class)) {
        when(request.getParameter("id")).thenReturn("1");
        factoryMock.when(LogicFactory::createGiftItemLogic).thenReturn(mockLogic);
        when(mockLogic.findGiftItem("1")).thenReturn(mockItem);

        when(request.getSession()).thenReturn(session);

        mainServlet.doPost(request, response);

        verify(session).setAttribute("currentGiftItem", mockItem);
        verify(response).sendRedirect("ViewGiftDetail");
      }
    }

    @Test
    @DisplayName("詳細表示：指定IDが見つからない場合、エラー画面へフォワードすること")
    void testDoPostNotFound() throws ServletException, IOException {
      try (MockedStatic<LogicFactory> factoryMock = mockStatic(LogicFactory.class)) {
        when(request.getParameter("id")).thenReturn("999");
        factoryMock.when(LogicFactory::createGiftItemLogic).thenReturn(mockLogic);
        when(mockLogic.findGiftItem("999")).thenReturn(null);

        when(request.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(dispatcher);

        mainServlet.doPost(request, response);

        verify(request).setAttribute(eq("errorMsg"), contains("みつかりませんでした"));
        verify(dispatcher).forward(request, response);
      }
    }

    @Test
    @DisplayName("例外発生：Logicで例外が起きた場合、エラー画面へ遷移すること(網羅用)")
    void testDoPostException() throws ServletException, IOException {
      try (MockedStatic<LogicFactory> factoryMock = mockStatic(LogicFactory.class)) {
        when(request.getParameter("id")).thenReturn("1");
        factoryMock.when(LogicFactory::createGiftItemLogic).thenReturn(mockLogic);
        when(mockLogic.findGiftItem("1")).thenThrow(new NoSuchElementException("Test Error"));

        when(request.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(dispatcher);

        mainServlet.doPost(request, response);

        verify(request).setAttribute(eq("errorMsg"), contains("処理中にエラーが発生しました"));
        verify(dispatcher).forward(request, response);
      }
    }
  }
}

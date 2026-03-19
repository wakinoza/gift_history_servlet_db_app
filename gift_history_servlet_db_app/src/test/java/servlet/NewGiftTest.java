package servlet;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
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
class NewGiftTest {

  private NewGift newGiftServlet;

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
    newGiftServlet = new NewGift();
  }

  @Nested
  @DisplayName("doGetメソッドのテスト")
  class DoGetTest {
    @Test
    @DisplayName("doGet: セッションがある場合、トークンをリクエストスコープにコピーすること")
    void testDoGetWithSession() throws ServletException, IOException {
      when(request.getSession(false)).thenReturn(session);
      when(session.getAttribute("csrfToken")).thenReturn("test-token");
      when(request.getRequestDispatcher("WEB-INF/jsp/newGift.jsp")).thenReturn(dispatcher);

      newGiftServlet.doGet(request, response);

      verify(request).setAttribute("csrfToken", "test-token");
      verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet: セッションがない場合、そのままJSPへフォワードすること(網羅用)")
    void testDoGetWithoutSession() throws ServletException, IOException {
      when(request.getSession(false)).thenReturn(null);
      when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

      newGiftServlet.doGet(request, response);

      verify(request, never()).setAttribute(eq("csrfToken"), any());
      verify(dispatcher).forward(request, response);
    }
  }

  @Nested
  @DisplayName("doPostメソッドのテスト")
  class DoPostTest {

    @Test
    @DisplayName("CSRFエラー: トークンが一致しない場合、403エラーを返すこと")
    void testDoPostCsrfError() throws ServletException, IOException {
      when(request.getSession(false)).thenReturn(session);
      when(session.getAttribute("csrfToken")).thenReturn("valid-token");
      when(request.getParameter("csrfToken")).thenReturn("invalid-token");

      newGiftServlet.doPost(request, response);

      verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "不正なリクエストです。");
      verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    @DisplayName("登録成功: 全てのバリデーションを通過し、Mainへリダイレクトすること")
    void testDoPostSuccess() throws ServletException, IOException {
      try (MockedStatic<LogicFactory> factoryMock = mockStatic(LogicFactory.class)) {
        setupCsrfSuccess();

        factoryMock.when(LogicFactory::createGiftItemLogic).thenReturn(mockLogic);
        when(mockLogic.createNewGiftItem(any(), any(), any(), any(), any(), any()))
            .thenReturn(mockItem);
        when(mockLogic.add(mockItem)).thenReturn(true);

        newGiftServlet.doPost(request, response);

        verify(response).sendRedirect("Main");
      }
    }

    @Test
    @DisplayName("入力エラー: createNewGiftItemがnullを返す場合、入力画面に戻ること")
    void testDoPostInputError() throws ServletException, IOException {
      try (MockedStatic<LogicFactory> factoryMock = mockStatic(LogicFactory.class)) {
        setupCsrfSuccess();
        factoryMock.when(LogicFactory::createGiftItemLogic).thenReturn(mockLogic);
        when(mockLogic.createNewGiftItem(any(), any(), any(), any(), any(), any()))
            .thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/jsp/newGift.jsp")).thenReturn(dispatcher);

        newGiftServlet.doPost(request, response);

        verify(request).setAttribute("errorMsg", "入力内容が正しくありません。");
        verify(dispatcher).forward(request, response);
      }
    }

    @Test
    @DisplayName("DB登録エラー: addメソッドがfalseを返す場合、エラー画面へ行くこと")
    void testDoPostDbError() throws ServletException, IOException {
      try (MockedStatic<LogicFactory> factoryMock = mockStatic(LogicFactory.class)) {
        setupCsrfSuccess();
        factoryMock.when(LogicFactory::createGiftItemLogic).thenReturn(mockLogic);
        when(mockLogic.createNewGiftItem(any(), any(), any(), any(), any(), any()))
            .thenReturn(mockItem);

        when(mockLogic.add(mockItem)).thenReturn(false);
        when(request.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(dispatcher);

        newGiftServlet.doPost(request, response);

        verify(request).setAttribute("errorMsg", "いただきものが追加できませんでした");
        verify(dispatcher).forward(request, response);
      }
    }

    @Test
    @DisplayName("例外発生: Logicで例外が起きた場合、エラー画面へ遷移すること(網羅用)")
    void testDoPostException() throws ServletException, IOException {
      try (MockedStatic<LogicFactory> factoryMock = mockStatic(LogicFactory.class)) {
        setupCsrfSuccess();
        factoryMock.when(LogicFactory::createGiftItemLogic).thenReturn(mockLogic);
        when(mockLogic.createNewGiftItem(any(), any(), any(), any(), any(), any()))
            .thenThrow(new RuntimeException("Fatal Error"));
        when(request.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(dispatcher);

        newGiftServlet.doPost(request, response);

        verify(request).setAttribute(eq("errorMsg"), contains("処理中にエラーが発生しました"));
        verify(dispatcher).forward(request, response);
      }
    }

    @Test
    @DisplayName("網羅用：セッションが完全にnullの場合にCSRFエラーになること")
    void testDoPostSessionNull() throws ServletException, IOException {
      when(request.getSession(false)).thenReturn(null);
      when(request.getParameter("csrfToken")).thenReturn("any-token");

      newGiftServlet.doPost(request, response);

      verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "不正なリクエストです。");
    }

    @Test
    @DisplayName("網羅用：セッションはあるがトークンが保存されていない場合にCSRFエラーになること")
    void testDoPostSessionTokenNull() throws ServletException, IOException {

      when(request.getSession(false)).thenReturn(session);
      when(session.getAttribute("csrfToken")).thenReturn(null);
      when(request.getParameter("csrfToken")).thenReturn("any-token");

      newGiftServlet.doPost(request, response);

      verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "不正なリクエストです。");
    }


    private void setupCsrfSuccess() {
      when(request.getSession(false)).thenReturn(session);
      when(session.getAttribute("csrfToken")).thenReturn("token");
      when(request.getParameter("csrfToken")).thenReturn("token");
    }
  }
}

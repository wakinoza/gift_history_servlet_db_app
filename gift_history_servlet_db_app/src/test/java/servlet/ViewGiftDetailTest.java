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
import factory.LogicFactory;

@ExtendWith(MockitoExtension.class)
class ViewGiftDetailTest {

  private ViewGiftDetail viewGiftDetailServlet;

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

  @BeforeEach
  void setUp() {
    viewGiftDetailServlet = new ViewGiftDetail();
  }

  @Nested
  @DisplayName("doGetメソッドのテスト")
  class DoGetTest {
    @Test
    @DisplayName("doGet: セッションがある場合、トークンをリクエストスコープにコピーすること")
    void testDoGetWithSession() throws ServletException, IOException {
      when(request.getSession(false)).thenReturn(session);
      when(session.getAttribute("csrfToken")).thenReturn("test-token");
      when(request.getRequestDispatcher("WEB-INF/jsp/viewGiftDetail.jsp")).thenReturn(dispatcher);

      viewGiftDetailServlet.doGet(request, response);

      verify(request).setAttribute("csrfToken", "test-token");
      verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet: セッションがない場合、そのままJSPへフォワードすること(網羅用)")
    void testDoGetWithoutSession() throws ServletException, IOException {
      when(request.getSession(false)).thenReturn(null);
      when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

      viewGiftDetailServlet.doGet(request, response);

      verify(request, never()).setAttribute(eq("csrfToken"), any());
      verify(dispatcher).forward(request, response);
    }
  }

  @Nested
  @DisplayName("doPostメソッドのテスト")
  class DoPostTest {

    @Test
    @DisplayName("CSRFエラー: トークンが一致しない場合、403エラー")
    void testDoPostCsrfError() throws ServletException, IOException {
      when(request.getSession(false)).thenReturn(session);
      when(session.getAttribute("csrfToken")).thenReturn("valid");
      when(request.getParameter("csrfToken")).thenReturn("invalid");

      viewGiftDetailServlet.doPost(request, response);

      verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), contains("不正なリクエスト"));

    }

    @Test
    @DisplayName("返礼完了: action=returned のとき、returnedメソッドが呼ばれ成功すること")
    void testDoPostReturnedSuccess() throws ServletException, IOException {
      try (MockedStatic<LogicFactory> factoryMock = mockStatic(LogicFactory.class)) {
        setupCsrfSuccess();
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("action")).thenReturn("returned");

        factoryMock.when(LogicFactory::createGiftItemLogic).thenReturn(mockLogic);
        when(mockLogic.returned("1")).thenReturn(true);

        viewGiftDetailServlet.doPost(request, response);

        verify(mockLogic).returned("1");
        verify(response).sendRedirect("Main");
      }
    }

    @Test
    @DisplayName("削除: action=remove のとき、removeメソッドが呼ばれ成功すること")
    void testDoPostRemoveSuccess() throws ServletException, IOException {
      try (MockedStatic<LogicFactory> factoryMock = mockStatic(LogicFactory.class)) {
        setupCsrfSuccess();
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("action")).thenReturn("remove");

        factoryMock.when(LogicFactory::createGiftItemLogic).thenReturn(mockLogic);
        when(mockLogic.remove("1")).thenReturn(true);

        viewGiftDetailServlet.doPost(request, response);

        verify(mockLogic).remove("1");
        verify(response).sendRedirect("Main");
      }
    }

    @Test
    @DisplayName("処理失敗: actionが不正、またはLogicがfalseを返す場合、エラー画面へ")
    void testDoPostActionFailure() throws ServletException, IOException {
      try (MockedStatic<LogicFactory> factoryMock = mockStatic(LogicFactory.class)) {
        setupCsrfSuccess();
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("action")).thenReturn("invalid_action");

        factoryMock.when(LogicFactory::createGiftItemLogic).thenReturn(mockLogic);
        when(request.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(dispatcher);

        viewGiftDetailServlet.doPost(request, response);

        verify(request).setAttribute(eq("errorMsg"), contains("実行できませんでした"));
        verify(dispatcher).forward(request, response);
      }
    }

    @Test
    @DisplayName("例外発生: Logicで例外が起きた場合、エラー画面へ遷移すること(網羅用)")
    void testDoPostException() throws ServletException, IOException {
      try (MockedStatic<LogicFactory> factoryMock = mockStatic(LogicFactory.class)) {
        setupCsrfSuccess();
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("action")).thenReturn("remove");

        factoryMock.when(LogicFactory::createGiftItemLogic).thenReturn(mockLogic);
        when(mockLogic.remove(any())).thenThrow(new RuntimeException("DB Error"));

        when(request.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(dispatcher);

        viewGiftDetailServlet.doPost(request, response);

        verify(request).setAttribute(eq("errorMsg"), contains("処理中にエラーが発生しました"));
        verify(dispatcher).forward(request, response);
      }
    }

    @Test
    @DisplayName("網羅用：セッションが完全にnullの場合にCSRFエラーになること")
    void testDoPostSessionNull() throws ServletException, IOException {
      when(request.getSession(false)).thenReturn(null);
      when(request.getParameter("csrfToken")).thenReturn("any-token");

      viewGiftDetailServlet.doPost(request, response);

      verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "不正なリクエストを検知しました。");
    }

    @Test
    @DisplayName("網羅用：セッションはあるがトークンが保存されていない場合にCSRFエラーになること")
    void testDoPostSessionTokenNull() throws ServletException, IOException {

      when(request.getSession(false)).thenReturn(session);
      when(session.getAttribute("csrfToken")).thenReturn(null);
      when(request.getParameter("csrfToken")).thenReturn("any-token");

      viewGiftDetailServlet.doPost(request, response);

      verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "不正なリクエストを検知しました。");
    }

    private void setupCsrfSuccess() {
      when(request.getSession(false)).thenReturn(session);
      when(session.getAttribute("csrfToken")).thenReturn("token");
      when(request.getParameter("csrfToken")).thenReturn("token");
    }
  }
}

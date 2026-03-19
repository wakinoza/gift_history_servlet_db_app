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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import model.LoginLogic;
import bean.User;
import factory.LogicFactory;
import util.CsrfUtil;

@ExtendWith(MockitoExtension.class)
class LoginTest {

  private Login loginServlet;

  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private HttpSession oldSession;
  @Mock
  private HttpSession newSession;
  @Mock
  private RequestDispatcher dispatcher;
  @Mock
  private LoginLogic mockLoginLogic;
  @Mock
  private User mockUser;

  @BeforeEach
  void setUp() {
    loginServlet = new Login();
  }

  @Test
  @DisplayName("ログイン成功：セッション更新とクッキー設定が行われること")
  void testDoPostSuccess() throws ServletException, IOException {
    try (MockedStatic<LogicFactory> factoryMock = mockStatic(LogicFactory.class);
        MockedStatic<CsrfUtil> csrfMock = mockStatic(CsrfUtil.class)) {

      when(request.getParameter("name")).thenReturn("azusa");
      when(request.getParameter("pass")).thenReturn("password");
      factoryMock.when(LogicFactory::createLoginLogic).thenReturn(mockLoginLogic);
      when(mockLoginLogic.execute("azusa", "password")).thenReturn(mockUser);

      when(request.getSession(false)).thenReturn(oldSession);
      when(request.getSession(true)).thenReturn(newSession);
      when(newSession.getId()).thenReturn("new-session-id");
      when(request.getContextPath()).thenReturn("/giftapp");

      csrfMock.when(CsrfUtil::generateToken).thenReturn("test-token");

      when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

      loginServlet.doPost(request, response);

      verify(oldSession).invalidate();
      verify(newSession).setAttribute("loginUser", mockUser);
      verify(newSession).setAttribute("csrfToken", "test-token");
      verify(response).setHeader(eq("Set-Cookie"), contains("SameSite=Lax"));
      verify(dispatcher).forward(request, response);
    }
  }

  @Test
  @DisplayName("ログイン失敗：セッション処理をスキップして結果画面へ行くこと")
  void testDoPostFailure() throws ServletException, IOException {
    try (MockedStatic<LogicFactory> factoryMock = mockStatic(LogicFactory.class)) {

      when(request.getParameter(anyString())).thenReturn("wrong-user");
      factoryMock.when(LogicFactory::createLoginLogic).thenReturn(mockLoginLogic);
      when(mockLoginLogic.execute(anyString(), anyString())).thenReturn(null);

      when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

      loginServlet.doPost(request, response);

      verify(request, never()).getSession(anyBoolean());
      verify(dispatcher).forward(request, response);
    }
  }

  @Test
  @DisplayName("網羅用：既存セッションがない場合にエラーにならないこと")
  void testDoPostOldSessionNull() throws ServletException, IOException {
    try (MockedStatic<LogicFactory> factoryMock = mockStatic(LogicFactory.class);
        MockedStatic<CsrfUtil> csrfMock = mockStatic(CsrfUtil.class)) {

      factoryMock.when(LogicFactory::createLoginLogic).thenReturn(mockLoginLogic);
      when(mockLoginLogic.execute(any(), any())).thenReturn(mockUser);

      when(request.getSession(false)).thenReturn(null);
      when(request.getSession(true)).thenReturn(newSession);
      when(newSession.getId()).thenReturn("id");
      when(request.getContextPath()).thenReturn("/path");
      when(request.getRequestDispatcher(any())).thenReturn(dispatcher);

      loginServlet.doPost(request, response);

      verify(oldSession, never()).invalidate();
    }
  }

  @Test
  @DisplayName("網羅用：ContextPathが空の場合に/が設定されること")
  void testDoPostContextPathEmpty() throws ServletException, IOException {
    try (MockedStatic<LogicFactory> factoryMock = mockStatic(LogicFactory.class);
        MockedStatic<CsrfUtil> csrfMock = mockStatic(CsrfUtil.class)) {

      factoryMock.when(LogicFactory::createLoginLogic).thenReturn(mockLoginLogic);
      when(mockLoginLogic.execute(any(), any())).thenReturn(mockUser);

      when(request.getSession(anyBoolean())).thenReturn(newSession);
      when(newSession.getId()).thenReturn("id");

      when(request.getContextPath()).thenReturn("");

      when(request.getRequestDispatcher(any())).thenReturn(dispatcher);

      loginServlet.doPost(request, response);

      verify(response).setHeader(eq("Set-Cookie"), contains("Path=/;"));
    }
  }

}

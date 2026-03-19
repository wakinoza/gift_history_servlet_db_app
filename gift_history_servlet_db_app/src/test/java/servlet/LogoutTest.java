package servlet;

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
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LogoutTest {

  private Logout logoutServlet;

  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private HttpSession session;
  @Mock
  private RequestDispatcher dispatcher;

  @BeforeEach
  void setUp() {
    logoutServlet = new Logout();
  }

  @Test
  @DisplayName("ログアウト：セッションが破棄され、ログアウト画面へ遷移すること")
  void testDoGetSuccess() throws ServletException, IOException {

    when(request.getSession()).thenReturn(session);
    when(request.getRequestDispatcher("WEB-INF/jsp/logout.jsp")).thenReturn(dispatcher);

    logoutServlet.doGet(request, response);

    verify(session, times(1)).invalidate();
    verify(dispatcher).forward(request, response);
  }
}

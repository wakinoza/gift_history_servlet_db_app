package filter;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import bean.User;

class CheckLoginFilterTest {

  private CheckLoginFilter filter;

  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private FilterChain chain;
  @Mock
  private HttpSession session;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    filter = new CheckLoginFilter();
  }

  @Nested
  @DisplayName("除外対象パスのテスト")
  class ExcludePathTest {

    @ParameterizedTest
    @ValueSource(strings = {"/index.jsp", "/Login", "/css/style.css"})
    @DisplayName("ログイン不要なパスの場合、そのまま次の処理へ進むこと")
    void testExcludePaths(String path) throws IOException, ServletException {
      when(request.getServletPath()).thenReturn(path);

      filter.doFilter(request, response, chain);

      verify(chain).doFilter(request, response);
      verify(request, never()).getSession(anyBoolean());
    }
  }

  @Nested
  @DisplayName("ログイン状態の判定テスト")
  class LoginStatusTest {

    @BeforeEach
    void setUpPath() {
      when(request.getServletPath()).thenReturn("/Main");
    }

    @Test
    @DisplayName("異常系：セッション自体が存在しない場合、index.jspへリダイレクトされること")
    void testNoSession() throws IOException, ServletException {
      when(request.getSession(false)).thenReturn(null);

      filter.doFilter(request, response, chain);

      verify(response).sendRedirect("index.jsp");
      verify(chain, never()).doFilter(any(), any());
    }

    @Test
    @DisplayName("異常系：セッションはあるがユーザー情報がない場合、リダイレクトされること")
    void testNoUserInSession() throws IOException, ServletException {
      when(request.getSession(false)).thenReturn(session);
      when(session.getAttribute("loginUser")).thenReturn(null);

      filter.doFilter(request, response, chain);

      verify(response).sendRedirect("index.jsp");
      verify(chain, never()).doFilter(any(), any());
    }

    @Test
    @DisplayName("正常系：ログイン済みの場合、次の処理へ進むこと")
    void testLoggedIn() throws IOException, ServletException {
      when(request.getSession(false)).thenReturn(session);
      when(session.getAttribute("loginUser")).thenReturn(new User());

      filter.doFilter(request, response, chain);

      verify(chain).doFilter(request, response);
      verify(response, never()).sendRedirect(anyString());
    }
  }
}

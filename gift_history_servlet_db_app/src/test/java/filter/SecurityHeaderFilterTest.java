package filter;

import static org.mockito.Mockito.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SecurityHeaderFilterTest {

  private SecurityHeaderFilter filter;

  @Mock
  private ServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private FilterChain chain;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    filter = new SecurityHeaderFilter();
  }

  @Test
  @DisplayName("doFilter: すべてのセキュリティヘッダーが正しく設定され、チェーンが継続されること")
  void testDoFilter() throws IOException, ServletException {
    filter.doFilter(request, response, chain);

    verify(response).setContentType("text/html; charset=UTF-8");

    verify(response).setHeader("Content-Security-Policy",
        "default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline';");
    verify(response).setHeader("X-Frame-Options", "DENY");
    verify(response).setHeader("X-Content-Type-Options", "nosniff");
    verify(response).setHeader("X-XSS-Protection", "1; mode=block");

    verify(chain).doFilter(request, response);
  }
}

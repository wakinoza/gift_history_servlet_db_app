package filter;

import static org.mockito.Mockito.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SetEncodingFilterTest {

  private SetEncodingFilter filter;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private FilterChain chain;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    filter = new SetEncodingFilter();
  }

  @Test
  @DisplayName("doFilter: レスポンスにUTF-8エンコーディングが設定され、次のフィルタが呼ばれること")
  void testDoFilter() throws IOException, ServletException {
    filter.doFilter(request, response, chain);

    verify(response).setCharacterEncoding("UTF-8");
    verify(response).setContentType("text/html, charset=UTF-8");
    verify(chain).doFilter(request, response);
  }
}

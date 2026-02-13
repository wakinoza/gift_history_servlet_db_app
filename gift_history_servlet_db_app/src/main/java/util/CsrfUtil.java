package util;

import java.security.SecureRandom;
import java.util.Base64;

public class CsrfUtil {
  /**
   * CSRF対策用のランダムなトークンを生成する
   */
  public static String generateToken() {
    byte[] tokenBytes = new byte[32];
    new SecureRandom().nextBytes(tokenBytes);
    return Base64.getEncoder().encodeToString(tokenBytes);
  }
}

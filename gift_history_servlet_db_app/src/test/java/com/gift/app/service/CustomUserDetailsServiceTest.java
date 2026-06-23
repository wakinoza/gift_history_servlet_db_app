package com.gift.app.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.gift.app.entity.User;
import com.gift.app.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CustomUserDetailsService customUserDetailsService;

  @Test
  @DisplayName("正常系：DBにユーザーが存在する場合、UserDetailsが正しく生成されること")
  void loadUserByUsername_Success() {
    User mockUser = new User();
    mockUser.setName("Alice");
    mockUser.setPassword("$2a$12$securepasswordhash");

    when(userRepository.findByName("Alice")).thenReturn(Optional.of(mockUser));

    UserDetails userDetails = customUserDetailsService.loadUserByUsername("Alice");

    assertThat(userDetails).isNotNull();
    assertThat(userDetails.getUsername()).isEqualTo("Alice");
    assertThat(userDetails.getPassword()).isEqualTo("$2a$12$securepasswordhash");
    assertThat(userDetails.getAuthorities()).extracting("authority").containsExactly("ROLE_USER");
  }

  @Test
  @DisplayName("異常系：DBにユーザーが存在しない場合、UsernameNotFoundExceptionがスローされること")
  void loadUserByUsername_UserNotFound_ThrowsException() {
    when(userRepository.findByName("unknownUser")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("unknownUser"))
        .isInstanceOf(UsernameNotFoundException.class)
        .hasMessageContaining("ユーザーが見つかりません: unknownUser");
  }
}

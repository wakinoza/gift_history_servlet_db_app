package com.gift.app.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.gift.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    com.gift.app.entity.User dbUser = userRepository.findByName(username)
        .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));

    return org.springframework.security.core.userdetails.User.withUsername(dbUser.getName())
        .password(dbUser.getPassword()).roles("USER").build();
  }
}

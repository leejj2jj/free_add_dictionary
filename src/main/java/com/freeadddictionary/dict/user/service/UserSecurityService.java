package com.freeadddictionary.dict.user.service;

import com.freeadddictionary.dict.user.domain.DictUser;
import com.freeadddictionary.dict.user.domain.UserRole;
import com.freeadddictionary.dict.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Optional<DictUser> _dictUser = userRepository.findByEmail(username);
    if (_dictUser.isEmpty()) {
      throw new UsernameNotFoundException("User not found with email: " + username);
    }
    DictUser dictUser = _dictUser.get();
    List<GrantedAuthority> authorities = new ArrayList<>();
    if ("admin".equals(username)) {
      authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
    } else {
      authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
    }
    return new User(dictUser.getEmail(), dictUser.getPassword(), authorities);
  }
}

package com.freeadddictionary.dict.repository;

import com.freeadddictionary.dict.domain.Role;
import com.freeadddictionary.dict.domain.User;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

  Page<User> searchUsers(String keyword, Role role, Boolean active, Pageable pageable);

  Page<User> findRecentlyInactiveUsers(LocalDateTime lastLoginBefore, Pageable pageable);
}

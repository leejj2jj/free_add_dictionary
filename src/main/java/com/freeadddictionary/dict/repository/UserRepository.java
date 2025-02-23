package com.freeadddictionary.dict.repository;

import com.freeadddictionary.dict.domain.User;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);

  @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startTime")
  long countTodayNewUsers(LocalDateTime startTime);

  @Query("SELECT u FROM User u WHERE u.role = 'ROLE_ADMIN'")
  Page<User> findAllAdmins(Pageable pageable);
}

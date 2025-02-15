package com.freeadddictionary.dict.user.repository;

import com.freeadddictionary.dict.user.domain.DictUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<DictUser, Long> {

  Optional<DictUser> findByEmail(String email);
}

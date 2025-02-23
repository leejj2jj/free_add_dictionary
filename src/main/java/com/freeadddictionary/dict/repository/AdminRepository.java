package com.freeadddictionary.dict.repository;

import com.freeadddictionary.dict.domain.Admin;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

/** Admin 엔티티에 대한 데이터베이스 작업을 처리하는 리포지토리. CRUD 작업과 이메일 기반 검색 기능을 제공합니다. */
public interface AdminRepository extends JpaRepository<Admin, Long> {

  /**
   * 이메일로 관리자를 검색합니다.
   *
   * @param email 검색할 관리자의 이메일
   * @return 검색된 관리자 정보를 담은 Optional
   * @throws IllegalArgumentException email이 null인 경우
   */
  Optional<Admin> findByEmail(@NonNull String email);

  /**
   * 주어진 이메일을 사용하는 관리자가 존재하는지 확인합니다.
   *
   * @param email 존재 여부를 확인할 이메일
   * @return 해당 이메일을 사용하는 관리자가 존재하면 true, 그렇지 않으면 false
   * @throws IllegalArgumentException email이 null인 경우
   */
  boolean existsByEmail(@NonNull String email);
}

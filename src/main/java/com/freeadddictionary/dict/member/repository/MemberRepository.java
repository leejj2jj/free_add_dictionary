package com.freeadddictionary.dict.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.freeadddictionary.dict.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String email);

}

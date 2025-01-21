package com.freeadddictionary.dict.member.service;

import com.freeadddictionary.dict.member.domain.Member;
import com.freeadddictionary.dict.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public Member loadUserByUsername(String email) {
    return memberRepository
        .findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException(email));
  }
}

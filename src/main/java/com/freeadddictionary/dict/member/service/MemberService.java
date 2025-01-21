package com.freeadddictionary.dict.member.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.freeadddictionary.dict.member.domain.Member;
import com.freeadddictionary.dict.member.dto.request.AddMemberRequest;
import com.freeadddictionary.dict.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  public Long save(AddMemberRequest dto) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    return memberRepository.save(Member.builder().email(dto.getEmail())
        .password(encoder.encode(dto.getPassword())).name(dto.getName()).phone(dto.getPhone())
        .receivingEmail(dto.isReceivingEmail()).registerDate(dto.getRegisterDate()).build())
        .getId();
  }

  public Member findById(Long userId) {
    return memberRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
  }

  public Member findByEmail(String email) {
    return memberRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
  }
}

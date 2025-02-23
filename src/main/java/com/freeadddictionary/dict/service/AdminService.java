package com.freeadddictionary.dict.service;

import com.freeadddictionary.dict.domain.Admin;
import com.freeadddictionary.dict.dto.request.AdminRequest;
import com.freeadddictionary.dict.dto.response.AdminResponse;
import com.freeadddictionary.dict.exception.NotFoundException;
import com.freeadddictionary.dict.repository.AdminRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {
  private final AdminRepository adminRepository;
  private final PasswordEncoder passwordEncoder;

  public AdminResponse createAdmin(AdminRequest request) {
    validateAdminRequest(request);
    if (adminRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
    }
    Admin admin = request.toEntity(passwordEncoder.encode(request.password()));
    adminRepository.save(admin);
    return AdminResponse.from(admin);
  }

  @Cacheable(value = "admins", key = "#id")
  public AdminResponse getAdminById(Long id) {
    Admin admin =
        adminRepository.findById(id).orElseThrow(() -> new NotFoundException("관리자를 찾을 수 없습니다."));
    return AdminResponse.from(admin);
  }

  @Cacheable(value = "admins", key = "'all'")
  public List<AdminResponse> getAllAdmins() {
    return adminRepository.findAll().stream().map(AdminResponse::from).collect(Collectors.toList());
  }

  @CacheEvict(value = "admins", key = "#id")
  public void updateAdmin(Long id, AdminRequest request) {
    validateAdminRequest(request);
    Admin admin =
        adminRepository.findById(id).orElseThrow(() -> new NotFoundException("관리자를 찾을 수 없습니다."));
    admin.updateAdminInfo(request.nickname(), passwordEncoder.encode(request.password()));
    adminRepository.save(admin);
  }

  @CacheEvict(value = "admins", allEntries = true)
  public void deleteAdmin(Long id) {
    Admin admin =
        adminRepository.findById(id).orElseThrow(() -> new NotFoundException("관리자를 찾을 수 없습니다."));
    adminRepository.delete(admin);
  }

  private void validateAdminRequest(AdminRequest request) {
    if (!StringUtils.hasText(request.email())) {
      throw new IllegalArgumentException("이메일은 필수 입력값입니다.");
    }
    if (!request.email().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
      throw new IllegalArgumentException("유효한 이메일 주소를 입력하세요.");
    }
    if (!StringUtils.hasText(request.password()) || request.password().length() < 10) {
      throw new IllegalArgumentException("비밀번호는 최소 10자 이상이어야 합니다.");
    }
    if (!StringUtils.hasText(request.nickname())) {
      throw new IllegalArgumentException("닉네임은 필수 입력값입니다.");
    }
  }
}

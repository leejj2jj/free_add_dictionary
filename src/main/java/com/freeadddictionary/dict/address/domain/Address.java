package com.freeadddictionary.dict.address.domain;

import java.util.ArrayList;
import java.util.List;

import com.freeadddictionary.dict.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "addresses")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

  @Id
  @Column(nullable = false)
  private String postcode;

  @Column(nullable = false, length = 100)
  private String address;

  @Column(columnDefinition = "TEXT")
  private String detailedAddress;

  @OneToMany(mappedBy = "address")
  private List<User> users = new ArrayList<>();

  @Builder
  public Address(String postcode, String address, String detailedAddress) {
    this.postcode = postcode;
    this.address = address;
    this.detailedAddress = detailedAddress;
  }

}

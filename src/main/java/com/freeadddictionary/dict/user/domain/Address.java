package com.freeadddictionary.dict.user.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.freeadddictionary.dict.shared.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Address extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "address_id")
  private Long id;

  private String postcode;

  private String address;

  private String detailedAddress;

  @Builder
  public Address(String postcode, String address, String detailedAddress) {
    this.postcode = postcode;
    this.address = address;
    this.detailedAddress = detailedAddress;
  }
}

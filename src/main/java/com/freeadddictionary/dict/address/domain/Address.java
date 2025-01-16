package com.freeadddictionary.dict.address.domain;

import static lombok.AccessLevel.PROTECTED;

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
public class Address {

  @Id
  @GeneratedValue
  @Column(name = "address_id")
  private Long id;

  @Column(nullable = false)
  private String postcode;

  @Column(nullable = false, length = 100)
  private String address;

  @Column(columnDefinition = "TEXT")
  private String detailedAddress;

  @Builder
  public Address(String postcode, String address, String detailedAddress) {
    this.postcode = postcode;
    this.address = address;
    this.detailedAddress = detailedAddress;
  }

}

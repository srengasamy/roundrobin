package com.roundrobin.vault.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "address")
public class Address {
  @Id
  private String id;
  private String street;
  private String city;
  private String state;
  private String country;
  private String zipCode;
}

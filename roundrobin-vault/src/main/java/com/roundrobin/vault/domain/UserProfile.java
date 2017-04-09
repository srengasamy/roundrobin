package com.roundrobin.vault.domain;

import com.roundrobin.vault.enums.SexType;

import org.apache.tomcat.jni.Address;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
@Document(collection = "user_profile")
public class UserProfile {
  @Id
  private String id;
  private String userId;
  private String firstName;
  private String lastName;
  private String email;
  private String mobileNumber;
  private Boolean vendor;
  private LocalDate dob;
  private String homeNumber;
  private SexType sex;
  private Integer flags;
  private Boolean active;
  private DateTime created;
  private Long version;
  private GeoJsonPoint location;

  private LocalTime startWindow;
  private LocalTime endWindow;
  private int overtime;

  private Address address;

  @DBRef
  private List<CreditCard> creditCards = new ArrayList<>();
  @DBRef
  private List<BankAccount> bankAccounts = new ArrayList<>();
  @DBRef
  private List<Skill> skills = new ArrayList<>();

}

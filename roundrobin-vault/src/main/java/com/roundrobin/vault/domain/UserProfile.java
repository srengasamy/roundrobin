package com.roundrobin.vault.domain;

import com.roundrobin.vault.enums.SexType;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

//TODO : start/end window and overtime
//TODO : averageReview, numberOfReview, punctuality, hospitality
@Data
@Document(collection = "user_profile")
public class UserProfile {
  @Id
  private String id;
  private String firstName;
  private String lastName;
  private String mobileNumber;
  private String homeNumber;
  private Boolean vendor;
  private LocalDate dob;
  private SexType sex;
  private Boolean active;
  private DateTime created;

  @DBRef
  private List<CreditCard> creditCards = new ArrayList<>();

  @DBRef
  private List<BankAccount> bankAccounts = new ArrayList<>();

}

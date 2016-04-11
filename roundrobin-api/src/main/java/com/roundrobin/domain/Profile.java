package com.roundrobin.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.tomcat.jni.Address;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roundrobin.groups.CreateValidator;
import com.roundrobin.groups.Validator;

@Document(collection = "profile")
public class Profile {
  @Id
  private String id;

  @NotBlank(groups = CreateValidator.class)
  private String firstName;

  @NotBlank(groups = CreateValidator.class)
  private String lastName;

  @NotBlank(groups = CreateValidator.class)
  @Email(groups = Validator.class)
  private String email;

  @NotEmpty(groups = CreateValidator.class)
  @Pattern(regexp = "(^$|[0-9]{10})", groups = Validator.class)
  private String mobileNumber;

  @NotNull(groups = CreateValidator.class)
  private Boolean vendor;

  @NotNull(groups = CreateValidator.class)
  private LocalDate dob;

  @Pattern(regexp = "(^$|[0-9]{10})", groups = Validator.class)
  private String homeNumber;

  @NotNull(groups = CreateValidator.class)
  private SexType sex;

  @JsonIgnore
  private int flags;

  @JsonIgnore
  private LocalDateTime created;

  private Address address;
  private Credentials credentials;
  private List<UserAction> actions;
  private List<CreditCard> creditCards;
  private List<BankAccount> bankAccounts;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public SexType getSex() {
    return sex;
  }

  public void setSex(SexType sex) {
    this.sex = sex;
  }

  public int getFlags() {
    return flags;
  }

  public void setFlags(int flags) {
    this.flags = flags;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getHomeNumber() {
    return homeNumber;
  }

  public void setHomeNumber(String homeNumber) {
    this.homeNumber = homeNumber;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDate getDob() {
    return dob;
  }

  public void setDob(LocalDate dob) {
    this.dob = dob;
  }

  public LocalDateTime getCreated() {
    return created;
  }

  public void setCreated(LocalDateTime created) {
    this.created = created;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public List<CreditCard> getCreditCards() {
    return creditCards;
  }

  public void setCreditCards(List<CreditCard> creditCards) {
    this.creditCards = creditCards;
  }

  public List<BankAccount> getBankAccounts() {
    return bankAccounts;
  }

  public void setBankAccounts(List<BankAccount> bankAccounts) {
    this.bankAccounts = bankAccounts;
  }

  public List<UserAction> getActions() {
    return actions;
  }

  public void setActions(List<UserAction> actions) {
    this.actions = actions;
  }

  public Credentials getCredentials() {
    return credentials;
  }

  public void setCredentials(Credentials credentials) {
    this.credentials = credentials;
  }

  public Boolean getVendor() {
    return vendor;
  }

  public void setVendor(Boolean vendor) {
    this.vendor = vendor;
  }

  public static enum SexType {
    MALE, FEMALE, OTHER
  }
}

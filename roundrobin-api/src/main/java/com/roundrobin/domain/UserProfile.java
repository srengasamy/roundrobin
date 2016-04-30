package com.roundrobin.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.jni.Address;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_profile")
public class UserProfile {
  @Id
  private String id;
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

  private Address address;
  @DBRef
  private Credential credential;
  @DBRef
  private List<UserAction> actions = new ArrayList<>();
  @DBRef
  private List<CreditCard> creditCards = new ArrayList<>();
  @DBRef
  private List<BankAccount> bankAccounts = new ArrayList<>();
  @DBRef
  private List<Skill> skills = new ArrayList<>();

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

  public Integer getFlags() {
    return flags;
  }

  public void setFlags(Integer flags) {
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

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
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

  public List<Skill> getSkills() {
    return skills;
  }

  public void setSkills(List<Skill> skills) {
    this.skills = skills;
  }

  public Credential getCredential() {
    return credential;
  }

  public void setCredential(Credential credential) {
    this.credential = credential;
  }

  public Boolean getVendor() {
    return vendor;
  }

  public void setVendor(Boolean vendor) {
    this.vendor = vendor;
  }

  public GeoJsonPoint getLocation() {
    return location;
  }

  public void setLocation(GeoJsonPoint location) {
    this.location = location;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public static enum SexType {
    MALE, FEMALE, OTHER
  }
}

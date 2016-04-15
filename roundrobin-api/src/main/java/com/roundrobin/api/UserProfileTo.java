package com.roundrobin.api;

import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;
import org.joda.time.LocalDate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.roundrobin.domain.UserProfile.SexType;
import com.roundrobin.groups.CreateProfileValidator;
import com.roundrobin.groups.ProfileValidator;
import com.roundrobin.groups.UpdateProfileValidator;
@JsonInclude(Include.NON_ABSENT)
public class UserProfileTo {
  @NotBlank(groups = UpdateProfileValidator.class)
  private String id;

  @UnwrapValidatedValue
  @NotBlank(groups = CreateProfileValidator.class)
  private Optional<String> firstName = Optional.empty();

  @UnwrapValidatedValue
  @NotBlank(groups = CreateProfileValidator.class)
  private Optional<String> lastName = Optional.empty();

  @UnwrapValidatedValue
  @NotBlank(groups = CreateProfileValidator.class)
  @Email(groups = ProfileValidator.class)
  private Optional<String> email = Optional.empty();

  @UnwrapValidatedValue
  @NotEmpty(groups = CreateProfileValidator.class)
  @Pattern(regexp = "(^$|[0-9]{10})", groups = ProfileValidator.class)
  private Optional<String> mobileNumber = Optional.empty();

  @UnwrapValidatedValue
  @NotNull(groups = CreateProfileValidator.class)
  private Optional<Boolean> vendor = Optional.empty();

  @UnwrapValidatedValue
  @NotNull(groups = CreateProfileValidator.class)
  private Optional<LocalDate> dob = Optional.empty();

  @UnwrapValidatedValue
  @Pattern(regexp = "(^$|[0-9]{10})", groups = ProfileValidator.class)
  private Optional<String> homeNumber = Optional.empty();

  @UnwrapValidatedValue
  private Optional<SexType> sex = Optional.empty();

  @UnwrapValidatedValue
  private Optional<GeoJsonPoint> location = Optional.empty();

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Optional<String> getFirstName() {
    return firstName;
  }

  public void setFirstName(Optional<String> firstName) {
    this.firstName = firstName;
  }

  public Optional<String> getLastName() {
    return lastName;
  }

  public void setLastName(Optional<String> lastName) {
    this.lastName = lastName;
  }

  public Optional<String> getEmail() {
    return email;
  }

  public void setEmail(Optional<String> email) {
    this.email = email;
  }

  public Optional<String> getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(Optional<String> mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public Optional<Boolean> getVendor() {
    return vendor;
  }

  public void setVendor(Optional<Boolean> vendor) {
    this.vendor = vendor;
  }

  public Optional<LocalDate> getDob() {
    return dob;
  }

  public void setDob(Optional<LocalDate> dob) {
    this.dob = dob;
  }

  public Optional<String> getHomeNumber() {
    return homeNumber;
  }

  public void setHomeNumber(Optional<String> homeNumber) {
    this.homeNumber = homeNumber;
  }

  public Optional<SexType> getSex() {
    return sex;
  }

  public void setSex(Optional<SexType> sex) {
    this.sex = sex;
  }

  public Optional<GeoJsonPoint> getLocation() {
    return location;
  }

  public void setLocation(Optional<GeoJsonPoint> location) {
    this.location = location;
  }

}

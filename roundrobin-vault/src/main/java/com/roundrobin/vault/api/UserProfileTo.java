package com.roundrobin.vault.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.roundrobin.vault.enums.SexType;
import com.roundrobin.vault.groups.CreateProfileValidator;
import com.roundrobin.vault.groups.ProfileValidator;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;
import org.joda.time.LocalDate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
@JsonInclude(Include.NON_ABSENT)
public class UserProfileTo {

  private String userId;

  @UnwrapValidatedValue
  @JsonProperty("first_name")
  @NotBlank(groups = CreateProfileValidator.class)
  @Length(max = 35, groups = ProfileValidator.class)
  private Optional<String> firstName = Optional.empty();

  @UnwrapValidatedValue
  @JsonProperty("last_name")
  @NotBlank(groups = CreateProfileValidator.class)
  @Length(max = 35, groups = ProfileValidator.class)
  private Optional<String> lastName = Optional.empty();

  @UnwrapValidatedValue
  @NotBlank(groups = CreateProfileValidator.class)
  @Email(groups = ProfileValidator.class)
  private Optional<String> email = Optional.empty();

  @UnwrapValidatedValue
  @JsonProperty("mobile_number")
  @NotEmpty(groups = CreateProfileValidator.class)
  @Pattern(regexp = "(^$|[0-9]{10})", groups = ProfileValidator.class)
  private Optional<String> mobileNumber = Optional.empty();

  @UnwrapValidatedValue
  @NotNull(groups = CreateProfileValidator.class)
  private Optional<Boolean> vendor = Optional.empty();

  @UnwrapValidatedValue
  @JsonProperty("home_number")
  @Pattern(regexp = "(^$|[0-9]{10})", groups = ProfileValidator.class)
  private Optional<String> homeNumber = Optional.empty();

  @UnwrapValidatedValue
  @NotBlank(groups = CreateProfileValidator.class)
  @Length(max = 35, groups = ProfileValidator.class)
  private Optional<String> password = Optional.empty();

  @UnwrapValidatedValue
  private Optional<LocalDate> dob = Optional.empty();

  @UnwrapValidatedValue
  private Optional<SexType> sex = Optional.empty();

  @UnwrapValidatedValue
  private Optional<GeoJsonPoint> location = Optional.empty();

}

package com.roundrobin.domain;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roundrobin.domain.Skill.DeliveryType;
import com.roundrobin.groups.CreateSkillDetailValidator;
import com.roundrobin.groups.CreateSkillValidator;
import com.roundrobin.groups.UpdateSkillDetailValidator;

@Document(collection = "skill_detail")
public class SkillDetail {
  @Id
  @NotBlank(groups = {UpdateSkillDetailValidator.class, CreateSkillValidator.class})
  private String id;

  @UnwrapValidatedValue
  @NotBlank(groups = CreateSkillDetailValidator.class)
  private Optional<String> name = Optional.empty();

  @UnwrapValidatedValue
  @NotNull(groups = CreateSkillDetailValidator.class)
  private Optional<DeliveryType> deliveryType = Optional.empty();

  @Valid
  @DBRef
  @NotNull(groups = CreateSkillDetailValidator.class)
  private SkillGroup skillGroup;

  @UnwrapValidatedValue
  @NotNull(groups = CreateSkillDetailValidator.class)
  private Optional<Boolean> active = Optional.empty();

  @Version
  @JsonIgnore
  private Long version;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Optional<String> getName() {
    return name;
  }

  public void setName(Optional<String> name) {
    this.name = name;
  }

  public Optional<DeliveryType> getDeliveryType() {
    return deliveryType;
  }

  public void setDeliveryType(Optional<DeliveryType> deliveryType) {
    this.deliveryType = deliveryType;
  }

  public void setActive(Optional<Boolean> active) {
    this.active = active;
  }

  public SkillGroup getSkillGroup() {
    return skillGroup;
  }

  public void setSkillGroup(SkillGroup skillGroup) {
    this.skillGroup = skillGroup;
  }

  public Optional<Boolean> getActive() {
    return active;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

}

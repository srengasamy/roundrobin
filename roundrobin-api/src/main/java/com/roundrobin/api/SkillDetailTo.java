package com.roundrobin.api;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.roundrobin.domain.Skill.DeliveryType;
import com.roundrobin.groups.CreateSkillDetailValidator;
import com.roundrobin.groups.UpdateSkillDetailValidator;
@JsonInclude(Include.NON_NULL)
public class SkillDetailTo {
  @NotBlank(groups = UpdateSkillDetailValidator.class)
  private String id;

  @UnwrapValidatedValue
  @NotBlank(groups = CreateSkillDetailValidator.class)
  private Optional<String> name = Optional.empty();

  @UnwrapValidatedValue
  @NotNull(groups = CreateSkillDetailValidator.class)
  private Optional<DeliveryType> deliveryType = Optional.empty();

  @NotBlank(groups = CreateSkillDetailValidator.class)
  private String skillGroupId;

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

  public String getSkillGroupId() {
    return skillGroupId;
  }

  public void setSkillGroupId(String skillGroupId) {
    this.skillGroupId = skillGroupId;
  }


}

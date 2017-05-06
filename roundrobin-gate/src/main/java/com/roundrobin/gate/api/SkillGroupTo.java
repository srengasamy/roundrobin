package com.roundrobin.gate.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.roundrobin.gate.groups.CreateSkillGroupValidator;
import com.roundrobin.gate.groups.SkillGroupValidator;
import com.roundrobin.gate.groups.UpdateSkillGroupValidator;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import java.util.Optional;

import lombok.Data;

@Data
public class SkillGroupTo {
  @NotBlank(groups = UpdateSkillGroupValidator.class)
  private String id;

  @UnwrapValidatedValue
  @JsonProperty("group_name")
  @NotBlank(groups = CreateSkillGroupValidator.class)
  @Length(max = 25, groups = SkillGroupValidator.class)
  private Optional<String> groupName = Optional.empty();

}

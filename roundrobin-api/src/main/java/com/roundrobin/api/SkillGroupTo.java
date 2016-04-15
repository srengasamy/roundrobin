package com.roundrobin.api;

import java.util.Optional;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import com.roundrobin.groups.CreateSkillGroupValidator;
import com.roundrobin.groups.UpdateSkillGroupValidator;

public class SkillGroupTo {
  @NotBlank(groups = UpdateSkillGroupValidator.class)
  private String id;

  @UnwrapValidatedValue
  @NotBlank(groups = CreateSkillGroupValidator.class)
  private Optional<String> groupName = Optional.empty();

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Optional<String> getGroupName() {
    return groupName;
  }

  public void setGroupName(Optional<String> groupName) {
    this.groupName = groupName;
  }


}

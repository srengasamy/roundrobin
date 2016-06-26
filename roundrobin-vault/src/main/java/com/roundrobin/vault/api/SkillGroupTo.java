package com.roundrobin.vault.api;

import java.util.Optional;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import com.roundrobin.vault.groups.CreateSkillGroupValidator;
import com.roundrobin.vault.groups.SkillGroupValidator;
import com.roundrobin.vault.groups.UpdateSkillGroupValidator;

public class SkillGroupTo {
  @NotBlank(groups = UpdateSkillGroupValidator.class)
  private String id;

  @UnwrapValidatedValue
  @NotBlank(groups = CreateSkillGroupValidator.class)
  @Length(max = 25, groups = SkillGroupValidator.class)
  @Pattern(regexp = "^[A-Za-z0-9]*$", groups=SkillGroupValidator.class)
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

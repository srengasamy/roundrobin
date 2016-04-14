package com.roundrobin.domain;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roundrobin.groups.CreateSkillDetailValidator;
import com.roundrobin.groups.CreateSkillGroupValidator;
import com.roundrobin.groups.UpdateSkillGroupValidator;

@Document(collection = "skill_group")
public class SkillGroup {
  @Id
  @NotBlank(groups = {UpdateSkillGroupValidator.class, CreateSkillDetailValidator.class})
  private String id;

  @UnwrapValidatedValue
  @NotBlank(groups = CreateSkillGroupValidator.class)
  private Optional<String> groupName = Optional.empty();

  @UnwrapValidatedValue
  @NotNull(groups = CreateSkillGroupValidator.class)
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

  public Optional<String> getGroupName() {
    return groupName;
  }

  public void setGroupName(Optional<String> groupName) {
    this.groupName = groupName;
  }

  public Optional<Boolean> getActive() {
    return active;
  }

  public void setActive(Optional<Boolean> active) {
    this.active = active;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

}

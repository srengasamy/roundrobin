package com.roundrobin.domain;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roundrobin.groups.CreateUserValidator;
import com.roundrobin.groups.SkillValidator;
import com.roundrobin.validator.SkillsRequired;
import com.roundrobin.validator.SkillsValid;

@Document(collection = "user")
@SkillsRequired(groups = SkillValidator.class)
@SkillsValid(groups = SkillValidator.class)
public class User implements Principal {
  @Id
  private String id;
  private String nickname;
  @JsonIgnore
  private Boolean active;

  @Valid
  @NotNull(groups = CreateUserValidator.class)
  private Profile profile;

  @Valid
  @DBRef
  private List<Skill> skills = new ArrayList<>();

  @Version
  private Long version;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
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

  public Profile getProfile() {
    return profile;
  }

  public void setProfile(Profile profile) {
    this.profile = profile;
  }

  public List<Skill> getSkills() {
    return skills;
  }

  public void setSkills(List<Skill> skills) {
    this.skills = skills;
  }

  @Override
  @JsonIgnore
  public String getName() {
    return nickname;
  }

}

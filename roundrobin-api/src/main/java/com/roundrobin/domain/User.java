package com.roundrobin.domain;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roundrobin.validator.SkillsRequired;

@Document(collection = "user")
@SkillsRequired
public class User implements Principal {
  @Id
  private String id;
  private String nickname;
  @JsonIgnore
  private boolean active;

  @Valid
  private Profile profile;

  @Valid
  private List<Skill> skills;

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

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
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

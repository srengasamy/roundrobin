package com.roundrobin.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.roundrobin.domain.Skill.DeliveryType;

@Document(collection = "skill_detail")
public class SkillDetail {
  @Id
  private String id;
  private String name;
  private DeliveryType deliveryType;
  private SkillGroup skillGroup;
  private boolean active;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DeliveryType getDeliveryType() {
    return deliveryType;
  }

  public void setDeliveryType(DeliveryType deliveryType) {
    this.deliveryType = deliveryType;
  }

  public SkillGroup getSkillGroup() {
    return skillGroup;
  }

  public void setSkillGroup(SkillGroup skillGroup) {
    this.skillGroup = skillGroup;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

}

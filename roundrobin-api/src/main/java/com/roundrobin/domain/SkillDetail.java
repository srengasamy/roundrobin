package com.roundrobin.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.roundrobin.domain.Skill.DeliveryType;

@Document(collection = "skill_detail")
public class SkillDetail {
  @Id
  private String id;

  private String name;

  private DeliveryType deliveryType;

  @DBRef
  private SkillGroup skillGroup;

  private Boolean active;

  @Version
  private Long version;

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

}

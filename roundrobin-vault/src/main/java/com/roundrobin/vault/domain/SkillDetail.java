package com.roundrobin.vault.domain;

import com.roundrobin.vault.enums.DeliveryType;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
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

}

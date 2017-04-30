package com.roundrobin.vault.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "skill_group")
public class SkillGroup {
  @Id
  private String id;
  private String groupName;
  private Boolean active;

}

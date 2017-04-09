package com.roundrobin.auth.domain;

import com.roundrobin.auth.enums.RoleType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
@Document(collection = "user")
public class User {
  @Id
  @Indexed
  private String id;
  @Indexed
  private String username;
  private String password;
  private Boolean verified;

  private List<RoleType> roles = new ArrayList<>();
  private List<UserAction> actions = new ArrayList<>();
  @CreatedDate
  private Date created;

  private Boolean active;

}

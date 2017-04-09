package com.roundrobin.vault.domain;

import com.roundrobin.vault.enums.ComplaintType;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "complaint")
public class Complaint {
  private String id;
  @DBRef
  private UserProfile userProfile;
  @DBRef
  private UserProfile vendorProfile;
  private ComplaintType type;
  @DBRef
  private Job job;
  private String desc;

}

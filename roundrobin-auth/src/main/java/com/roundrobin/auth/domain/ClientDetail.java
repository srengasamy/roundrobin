package com.roundrobin.auth.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import lombok.Data;

@Data
@Document(collection = "client_detail")
public class ClientDetail {
  @Id
  @Indexed
  private String id;
  @Indexed
  private String clientId;
  private Set<String> resourceIds;
  private boolean secretRequired;
  @Indexed
  private String clientSecret;
  private boolean scoped;
  private Set<String> scope;
  private Set<String> authorizedGrantTypes;
  private Set<String> registeredRedirectUri;
  private Collection<String> authorities;
  private Integer accessTokenValiditySeconds;
  private Integer refreshTokenValiditySeconds;
  private boolean autoApprove;
  private Map<String, Object> additionalInformation;
  @CreatedDate
  private Date created;
  private Boolean active;

}

package com.roundrobin.auth.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;

import lombok.Data;

@Data
@Document(collection = "refresh_token")
public class RefreshToken {

  @Indexed
  private String id;
  private String tokenId;
  private DefaultExpiringOAuth2RefreshToken refreshToken;

}

package com.roundrobin.auth.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

import lombok.Data;

@Data
@Document(collection = "access_token")
public class AccessToken {

  @Indexed
  private String id;
  @Indexed
  private String tokenId;
  private DefaultOAuth2AccessToken accessToken;
  private DefaultExpiringOAuth2RefreshToken refreshToken;
  private String authenticationId;
  private String username;
  private String clientId;
  private String authentication;

}

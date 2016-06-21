package com.roundrobin.auth.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;


@Document(collection = "refresh_token")
public class RefreshToken {

  @Indexed
  private String id;
  private String tokenId;
  private DefaultExpiringOAuth2RefreshToken refreshToken;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTokenId() {
    return tokenId;
  }

  public void setTokenId(String tokenId) {
    this.tokenId = tokenId;
  }

  public DefaultExpiringOAuth2RefreshToken getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(DefaultExpiringOAuth2RefreshToken refreshToken) {
    this.refreshToken = refreshToken;
  }

}

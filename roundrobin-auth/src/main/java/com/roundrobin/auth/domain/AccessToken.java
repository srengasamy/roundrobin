package com.roundrobin.auth.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;


@Document(collection = "access_token")
public class AccessToken {

  @Indexed
  private String id;
  @Indexed
  private String tokenId;
  private OAuth2AccessToken oAuth2AccessToken;
  private String authenticationId;
  private String username;
  private String clientId;
  private OAuth2Authentication authentication;
  private String refreshToken;

  public AccessToken() {}

  public AccessToken(final OAuth2AccessToken oAuth2AccessToken, final OAuth2Authentication authentication,
      final String authenticationId) {
    this.tokenId = oAuth2AccessToken.getValue();
    this.oAuth2AccessToken = oAuth2AccessToken;
    this.authenticationId = authenticationId;
    this.username = authentication.getName();
    this.clientId = authentication.getOAuth2Request().getClientId();
    this.authentication = authentication;
    if (oAuth2AccessToken.getRefreshToken() != null) {
      this.refreshToken = oAuth2AccessToken.getRefreshToken().getValue();
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTokenId() {
    return tokenId;
  }

  public OAuth2AccessToken getoAuth2AccessToken() {
    return oAuth2AccessToken;
  }

  public String getAuthenticationId() {
    return authenticationId;
  }

  public String getUsername() {
    return username;
  }

  public String getClientId() {
    return clientId;
  }

  public OAuth2Authentication getAuthentication() {
    return authentication;
  }

  public String getRefreshToken() {
    return refreshToken;
  }
}

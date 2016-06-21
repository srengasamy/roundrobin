package com.roundrobin.auth.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;


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

  public DefaultOAuth2AccessToken getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(DefaultOAuth2AccessToken accessToken) {
    this.accessToken = accessToken;
  }

  public DefaultExpiringOAuth2RefreshToken getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(DefaultExpiringOAuth2RefreshToken refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getAuthenticationId() {
    return authenticationId;
  }

  public void setAuthenticationId(String authenticationId) {
    this.authenticationId = authenticationId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getAuthentication() {
    return authentication;
  }

  public void setAuthentication(String authentication) {
    this.authentication = authentication;
  }

}

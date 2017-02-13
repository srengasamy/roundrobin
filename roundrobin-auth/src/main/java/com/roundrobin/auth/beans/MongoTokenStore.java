package com.roundrobin.auth.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Component;

import com.roundrobin.auth.domain.AccessToken;
import com.roundrobin.auth.domain.RefreshToken;
import com.roundrobin.auth.repository.AccessTokenRepository;
import com.roundrobin.auth.repository.RefreshTokenRepository;

public class MongoTokenStore extends JwtTokenStore {

  private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

  @Autowired
  private MongoTokenConverter tokenConverter;

  @Autowired
  private AccessTokenRepository accessTokenRepo;

  @Autowired
  private RefreshTokenRepository refreshTokenRepo;

  public MongoTokenStore(MongoTokenConverter tokenConverter, AccessTokenRepository accessTokenRepo,
      RefreshTokenRepository refreshTokenRepo) {
    super(tokenConverter);
    this.tokenConverter = tokenConverter;
    this.accessTokenRepo = accessTokenRepo;
    this.refreshTokenRepo = refreshTokenRepo;
  }

  @Override
  public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
    String authenticationId = authenticationKeyGenerator.extractKey(authentication);
    AccessToken token = accessTokenRepo.findByAuthenticationId(authenticationId);
    return token == null ? null : token.getAccessToken();
  }

  @Override
  public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
    AccessToken accessToken = new AccessToken();
    accessToken.setTokenId(token.getValue());
    accessToken.setAccessToken((DefaultOAuth2AccessToken) token);
    accessToken.setRefreshToken((DefaultExpiringOAuth2RefreshToken) token.getRefreshToken());
    accessToken.setAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
    accessToken.setUsername(authentication.getName());
    accessToken.setClientId(authentication.getOAuth2Request().getClientId());
    accessToken.setAuthentication(tokenConverter.encode(token, authentication));
    accessTokenRepo.save(accessToken);
  }

  @Override
  public void storeRefreshToken(OAuth2RefreshToken token, OAuth2Authentication authentication) {
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setTokenId(token.getValue());
    refreshToken.setRefreshToken((DefaultExpiringOAuth2RefreshToken) token);
    refreshTokenRepo.save(refreshToken);
  }

}

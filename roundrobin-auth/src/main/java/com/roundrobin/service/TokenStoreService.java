package com.roundrobin.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import com.roundrobin.domain.AccessToken;
import com.roundrobin.domain.RefreshToken;
import com.roundrobin.repository.AccessTokenRepository;
import com.roundrobin.repository.RefreshTokenRepository;

@Service
public class TokenStoreService implements TokenStore {

  @Autowired
  private AccessTokenRepository accessTokenRepo;

  @Autowired
  private RefreshTokenRepository refreshTokenRepo;

  private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

  @Override
  public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
    return readAuthentication(token.getValue());
  }

  @Override
  public OAuth2Authentication readAuthentication(String tokenId) {
    return accessTokenRepo.findByTokenId(tokenId).getAuthentication();
  }

  @Override
  public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
    AccessToken oAuth2AuthenticationAccessToken =
        new AccessToken(token, authentication, authenticationKeyGenerator.extractKey(authentication));
    accessTokenRepo.save(oAuth2AuthenticationAccessToken);
  }

  @Override
  public OAuth2AccessToken readAccessToken(String tokenValue) {
    AccessToken token = accessTokenRepo.findByTokenId(tokenValue);
    if (token == null) {
      return null; // let spring security handle the invalid token
    }
    OAuth2AccessToken accessToken = token.getoAuth2AccessToken();
    return accessToken;
  }

  @Override
  public void removeAccessToken(OAuth2AccessToken token) {
    AccessToken accessToken = accessTokenRepo.findByTokenId(token.getValue());
    if (accessToken != null) {
      accessTokenRepo.delete(accessToken);
    }
  }

  @Override
  public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
    refreshTokenRepo.save(new RefreshToken(refreshToken, authentication));
  }

  @Override
  public OAuth2RefreshToken readRefreshToken(String tokenValue) {
    return refreshTokenRepo.findByTokenId(tokenValue).getoAuth2RefreshToken();
  }

  @Override
  public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
    return refreshTokenRepo.findByTokenId(token.getValue()).getAuthentication();
  }

  @Override
  public void removeRefreshToken(OAuth2RefreshToken token) {
    refreshTokenRepo.delete(refreshTokenRepo.findByTokenId(token.getValue()));
  }

  @Override
  public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
    accessTokenRepo.delete(accessTokenRepo.findByRefreshToken(refreshToken.getValue()));
  }

  @Override
  public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
    AccessToken token = accessTokenRepo.findByAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
    return token == null ? null : token.getoAuth2AccessToken();
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
    List<AccessToken> tokens = accessTokenRepo.findByClientId(clientId);
    return extractAccessTokens(tokens);
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String username) {
    List<AccessToken> tokens = accessTokenRepo.findByClientIdAndUsername(clientId, username);
    return extractAccessTokens(tokens);
  }

  private Collection<OAuth2AccessToken> extractAccessTokens(List<AccessToken> tokens) {
    List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>();
    for (AccessToken token : tokens) {
      accessTokens.add(token.getoAuth2AccessToken());
    }
    return accessTokens;
  }

}

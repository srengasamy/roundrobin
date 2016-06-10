
package com.roundrobin.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
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

  private final AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

  @Override
  public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
    return readAuthentication(token.getValue());
  }

  @Override
  public OAuth2Authentication readAuthentication(String tokenId) {
    Optional<AccessToken> accessToken = accessTokenRepo.findByTokenId(tokenId);
    return accessToken.isPresent() ? accessToken.get().getAuthentication() : null;
  }

  @Override
  public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
    AccessToken accessToken =
        new AccessToken(token, authentication, authenticationKeyGenerator.extractKey(authentication));
    accessTokenRepo.save(accessToken);
  }

  @Override
  public OAuth2AccessToken readAccessToken(String tokenId) {
    Optional<AccessToken> accessToken = accessTokenRepo.findByTokenId(tokenId);
    if (accessToken.isPresent()) {
      return accessToken.get().getoAuth2AccessToken();
    }
    throw new InvalidTokenException("Token not valid");
  }

  @Override
  public void removeAccessToken(OAuth2AccessToken accessToken) {
    Optional<AccessToken> token = accessTokenRepo.findByTokenId(accessToken.getValue());
    if (token.isPresent()) {
      accessTokenRepo.delete(token.get());
    }
  }

  @Override
  public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
    refreshTokenRepo.save(new RefreshToken(refreshToken, authentication));
  }

  @Override
  public OAuth2RefreshToken readRefreshToken(String accessToken) {
    Optional<RefreshToken> token = refreshTokenRepo.findByTokenId(accessToken);
    if (token.isPresent()) {
      token.get().getoAuth2RefreshToken();
    }
    throw new InvalidTokenException("Token not valid");
  }

  @Override
  public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
    Optional<RefreshToken> refreshToken = refreshTokenRepo.findByTokenId(token.getValue());
    return refreshToken.isPresent() ? refreshToken.get().getAuthentication() : null;
  }

  @Override
  public void removeRefreshToken(OAuth2RefreshToken accessToken) {
    Optional<RefreshToken> refreshToken = refreshTokenRepo.findByTokenId(accessToken.getValue());
    if (refreshToken.isPresent()) {
      refreshTokenRepo.delete(refreshToken.get());
    }
  }

  @Override
  public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
    Optional<AccessToken> token = accessTokenRepo.findByRefreshToken(refreshToken.getValue());
    if (token.isPresent()) {
      accessTokenRepo.delete(token.get());
    }
  }

  @Override
  public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
    String authenticationId = authenticationKeyGenerator.extractKey(authentication);
    if (null == authenticationId) {
      return null;
    }
    Optional<AccessToken> token = accessTokenRepo.findByAuthenticationId(authenticationId);
    return token.isPresent() ? token.get().getoAuth2AccessToken() : null;
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("clientId").is(clientId));
    List<AccessToken> accessTokens = accessTokenRepo.findAllByClientId(clientId);
    return extractAccessTokens(accessTokens);
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String username) {
    List<AccessToken> accessTokens = accessTokenRepo.findAllByClientIdAndUsername(clientId, username);
    return extractAccessTokens(accessTokens);
  }

  private Collection<OAuth2AccessToken> extractAccessTokens(List<AccessToken> tokens) {
    List<OAuth2AccessToken> accessTokens = new ArrayList<>();
    tokens.stream().forEach(token -> {
      accessTokens.add(token.getoAuth2AccessToken());
    });
    return accessTokens;
  }

}

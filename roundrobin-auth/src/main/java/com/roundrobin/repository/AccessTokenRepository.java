package com.roundrobin.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.roundrobin.domain.AccessToken;

@Repository
public interface AccessTokenRepository extends MongoRepository<AccessToken, String> {

  public AccessToken findByTokenId(String tokenId);

  public AccessToken findByRefreshToken(String refreshToken);

  public AccessToken findByAuthenticationId(String authenticationId);

  public List<AccessToken> findByClientIdAndUsername(String clientId, String username);

  public List<AccessToken> findByClientId(String clientId);
}

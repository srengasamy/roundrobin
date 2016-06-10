package com.roundrobin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.roundrobin.domain.AccessToken;

@Repository
public interface AccessTokenRepository extends MongoRepository<AccessToken, String> {
  public Optional<AccessToken> findByTokenId(String tokenId);

  public Optional<AccessToken> findByAuthenticationId(String authenticationId);

  public List<AccessToken> findAllByClientId(String clientId);

  public List<AccessToken> findAllByClientIdAndUsername(String clientId, String username);

  public Optional<AccessToken> findByRefreshToken(String refreshToken);
}

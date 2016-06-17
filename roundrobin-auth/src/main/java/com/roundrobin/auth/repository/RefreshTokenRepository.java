package com.roundrobin.auth.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.roundrobin.auth.domain.RefreshToken;


@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

  public RefreshToken findByTokenId(String tokenId);
}

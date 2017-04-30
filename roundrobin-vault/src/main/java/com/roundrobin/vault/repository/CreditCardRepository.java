package com.roundrobin.vault.repository;

import com.roundrobin.vault.domain.CreditCard;
import com.roundrobin.vault.domain.UserProfile;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CreditCardRepository extends MongoRepository<CreditCard, String> {
  public Optional<CreditCard> findById(String id);

  public List<CreditCard> findAllByProfile(UserProfile profile);

}

package com.roundrobin.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.domain.CreditCard;

public interface CreditCardRepository extends MongoRepository<CreditCard, String> {
  public Optional<CreditCard> findById(String id);

}

package com.roundrobin.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.domain.CreditCard;

public interface CreditCardRepository extends MongoRepository<CreditCard, String>{

}

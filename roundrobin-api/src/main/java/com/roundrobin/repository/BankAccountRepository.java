package com.roundrobin.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.domain.BankAccount;

public interface BankAccountRepository extends MongoRepository<BankAccount, String> {

}

package com.roundrobin.vault.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.vault.domain.BankAccount;

public interface BankAccountRepository extends MongoRepository<BankAccount, String> {
  public Optional<BankAccount> findById(String id);

}

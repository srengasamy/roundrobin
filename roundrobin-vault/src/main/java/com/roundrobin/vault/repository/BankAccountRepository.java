package com.roundrobin.vault.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.vault.domain.BankAccount;
import com.roundrobin.vault.domain.UserProfile;

public interface BankAccountRepository extends MongoRepository<BankAccount, String> {
  public Optional<BankAccount> findById(String id);

  public List<BankAccount> findAllByProfile(UserProfile profile);
}

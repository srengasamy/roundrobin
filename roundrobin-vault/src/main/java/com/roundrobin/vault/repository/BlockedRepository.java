package com.roundrobin.vault.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.roundrobin.vault.domain.Blocked;

public interface BlockedRepository extends MongoRepository<Blocked, String> {

  // TODO complete the native query
  @Query("{'user.id':?0, 'vendor.id':{$in : ?1}}")
  public List<Blocked> findAllByVendorIds(String userId, List<String> vendorIds);
}

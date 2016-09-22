package com.roundrobin.vault.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.roundrobin.vault.domain.Blocked;
import com.roundrobin.vault.domain.Job;
import com.roundrobin.vault.domain.JobAvoided;

public interface JobAvoidedRepository extends MongoRepository<JobAvoided, String> {
  @Query("{'job':?0, 'vendor.id':{$in : ?2}}")
  public List<Blocked> findAllByJobAndVendorIds(Job job, List<String> vendorIds);
}

package com.roundrobin.vault.repository;

import java.util.Optional;

import org.joda.time.LocalDate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.roundrobin.vault.domain.JobAgenda;

public interface JobAgendaRepository extends MongoRepository<JobAgenda, String> {
  @Query("{'vendor.id': ?0, 'date':?1}")
  public Optional<JobAgenda> findByVendorIdAndDate(@Param(value = "vendorId") String vendorId,
                                                   @Param(value = "date") LocalDate date);

}

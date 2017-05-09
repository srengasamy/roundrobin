package com.roundrobin.gate.repository;

import com.roundrobin.gate.domain.Job;
import com.roundrobin.gate.domain.UserProfile;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends MongoRepository<Job, String> {
  public Optional<Job> findById(String id);

  public List<Job> findAllByProfileAndCreatedBetweenOrderByCreatedDesc(UserProfile profile, DateTime startDate, DateTime
          endDate);
}

package com.roundrobin.vault.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.vault.domain.Job;

public interface JobRepository extends MongoRepository<Job, String> {

}

package com.roundrobin.repository;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.domain.ClientDetail;

public interface ClientDetailRepository extends MongoRepository<ClientDetail, Serializable> {

  public Optional<ClientDetail> findByClientId(String clientId);

}

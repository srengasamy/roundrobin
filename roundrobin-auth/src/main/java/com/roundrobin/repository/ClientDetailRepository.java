package com.roundrobin.repository;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.roundrobin.domain.ClientDetail;

@Repository
public interface ClientDetailRepository extends MongoRepository<ClientDetail, Serializable> {

  public Optional<ClientDetail> findByClientId(String clientId);

}

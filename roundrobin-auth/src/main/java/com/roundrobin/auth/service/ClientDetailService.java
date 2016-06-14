package com.roundrobin.auth.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import com.roundrobin.auth.domain.ClientDetail;
import com.roundrobin.auth.repository.ClientDetailRepository;

@Service
public class ClientDetailService implements ClientDetailsService, ClientRegistrationService {

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private ClientDetailRepository clientDetailRepo;

  @Override
  public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
    Optional<ClientDetail> clientDetails = clientDetailRepo.findByClientId(clientId);
    if (clientDetails.isPresent()) {
      return getClientFromMongoDBClientDetails(clientDetails.get());
    }
    throw new ClientRegistrationException("Client not found with id '" + clientId + "'");
  }

  @Override
  public void addClientDetails(ClientDetails cd) throws ClientAlreadyExistsException {
    ClientDetail clientDetails = getMongoDBClientDetailsFromClient(cd);
    clientDetailRepo.save(clientDetails);
  }

  @Override
  public void updateClientDetails(ClientDetails cd) throws NoSuchClientException {
    Optional<ClientDetail> clientDetails = clientDetailRepo.findByClientId(cd.getClientId());
    if (clientDetails.isPresent()) {
      clientDetailRepo.save(getMongoDBClientDetailsFromClient(cd));
    }
    throw new NoSuchClientException("Client not found with ID '" + cd.getClientId() + "'");
  }

  @Override
  public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
    Optional<ClientDetail> clientDetails = clientDetailRepo.findByClientId(clientId);
    if (clientDetails.isPresent()) {
      clientDetails.get().setClientSecret(passwordEncoder.encode(secret));
      clientDetailRepo.save(clientDetails.get());
    }
    throw new NoSuchClientException("Client not found with ID '" + clientId + "'");
  }

  @Override
  public void removeClientDetails(String clientId) throws NoSuchClientException {
    Optional<ClientDetail> clientDetails = clientDetailRepo.findByClientId(clientId);
    if (clientDetails.isPresent()) {
      clientDetailRepo.delete(clientDetails.get());
    }
    throw new NoSuchClientException("Client not found with ID '" + clientId + "'");
  }

  @Override
  public List<ClientDetails> listClientDetails() {
    List<ClientDetail> mdbcds = clientDetailRepo.findAll();
    return getClientsFromMongoDBClientDetails(mdbcds);
  }

  private List<ClientDetails> getClientsFromMongoDBClientDetails(List<ClientDetail> clientDetails) {
    List<ClientDetails> bcds = new LinkedList<>();
    if (clientDetails != null && !clientDetails.isEmpty()) {
      clientDetails.stream().forEach(mdbcd -> {
        bcds.add(getClientFromMongoDBClientDetails(mdbcd));
      });
    }
    return bcds;
  }

  private BaseClientDetails getClientFromMongoDBClientDetails(ClientDetail clientDetails) {
    BaseClientDetails bc = new BaseClientDetails();
    bc.setAccessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds());
    bc.setAuthorizedGrantTypes(clientDetails.getAuthorizedGrantTypes());
    bc.setClientId(clientDetails.getClientId());
    bc.setClientSecret(clientDetails.getClientSecret());
    bc.setRefreshTokenValiditySeconds(clientDetails.getRefreshTokenValiditySeconds());
    bc.setRegisteredRedirectUri(clientDetails.getRegisteredRedirectUri());
    bc.setResourceIds(clientDetails.getResourceIds());
    bc.setScope(clientDetails.getScope());
    return bc;
  }

  private ClientDetail getMongoDBClientDetailsFromClient(ClientDetails cd) {
    ClientDetail clientDetails = new ClientDetail();
    clientDetails.setAccessTokenValiditySeconds(cd.getAccessTokenValiditySeconds());
    clientDetails.setAdditionalInformation(cd.getAdditionalInformation());
    clientDetails.setAuthorizedGrantTypes(cd.getAuthorizedGrantTypes());
    clientDetails.setClientId(cd.getClientId());
    clientDetails.setClientSecret(cd.getClientSecret());
    clientDetails.setRefreshTokenValiditySeconds(cd.getRefreshTokenValiditySeconds());
    clientDetails.setRegisteredRedirectUri(cd.getRegisteredRedirectUri());
    clientDetails.setResourceIds(cd.getResourceIds());
    clientDetails.setScope(cd.getScope());
    clientDetails.setScoped(cd.isScoped());
    clientDetails.setSecretRequired(cd.isSecretRequired());
    clientDetails.setId(cd.getClientId());
    return clientDetails;
  }

  public ClientDetail save(ClientDetail authClient) {
    return clientDetailRepo.save(authClient);
  }

  public void deleteAll() {
    clientDetailRepo.deleteAll();
  }

}

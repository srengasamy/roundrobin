package com.roundrobin.services;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.api.CredentialTo;
import com.roundrobin.common.ErrorCodes;
import com.roundrobin.domain.Credential;
import com.roundrobin.repository.CredentialRepository;

@Service
public class CredentialServiceImpl implements CredentialService {
  @Autowired
  private CredentialRepository credentialRepo;

  @Autowired
  private UserProfileService profileService;

  @Override
  public Credential get(String id) {
    Optional<Credential> credential = credentialRepo.findById(id);
    checkArgument(credential.isPresent(), ErrorCodes.INVALID_CREDENTIAL_ID);
    return credential.get();
  }

  @Override
  public CredentialTo read(String id) {
    Credential credential = get(id);
    CredentialTo credentialTo = new CredentialTo();
    credentialTo.setUsername(credential.getUsername());
    credentialTo.setPassword(credential.getPassword());
    credentialTo.setId(credential.getId());
    return credentialTo;
  }

  @Override
  public Credential create(CredentialTo credentialTo) {
    Credential credential = new Credential();
    credential.setUsername(credentialTo.getUsername());
    credential.setPassword(credentialTo.getPassword());
    credential.setCreated(DateTime.now());
    return save(credential);
  }

  @Override
  public CredentialTo update(CredentialTo credentialTo) {
    Credential credential = get(credentialTo.getId());
    credential.setPassword(credentialTo.getPassword());
    return read(save(credential).getId());
  }

  @Override
  public Credential save(Credential credential) {
    return credentialRepo.save(credential);
  }

}

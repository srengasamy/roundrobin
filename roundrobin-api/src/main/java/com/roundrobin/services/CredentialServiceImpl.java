package com.roundrobin.services;

import java.util.Optional;

import org.jasypt.encryption.StringEncryptor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.api.CredentialTo;
import com.roundrobin.common.Assert;
import com.roundrobin.common.ErrorCode;
import com.roundrobin.domain.Credential;
import com.roundrobin.repository.CredentialRepository;

@Service
public class CredentialServiceImpl implements CredentialService {
  @Autowired
  private CredentialRepository credentialRepo;

  @Autowired
  private StringEncryptor jasyptStringEncryptor;

  @Override
  public Credential get(String id) {
    Optional<Credential> credential = credentialRepo.findById(id);
    Assert.isTrue(credential.isPresent(), ErrorCode.INVALID_CREDENTIAL_ID);
    return credential.get();
  }

  @Override
  public CredentialTo read(String id) {
    Credential credential = get(id);
    CredentialTo credentialTo = new CredentialTo();
    credentialTo.setUsername(jasyptStringEncryptor.decrypt(credential.getUsername()));
    credentialTo.setPassword(jasyptStringEncryptor.decrypt(credential.getPassword()));
    credentialTo.setId(credential.getId());
    return credentialTo;
  }

  @Override
  public Credential create(CredentialTo credentialTo) {
    Credential credential = new Credential();
    credential.setUsername(jasyptStringEncryptor.encrypt(credentialTo.getUsername()));
    credential.setPassword(jasyptStringEncryptor.encrypt(credentialTo.getPassword()));
    credential.setCreated(DateTime.now());
    return save(credential);
  }

  @Override
  public CredentialTo update(CredentialTo credentialTo) {
    Credential credential = get(credentialTo.getId());
    credential.setPassword(jasyptStringEncryptor.encrypt(credentialTo.getPassword()));
    return read(save(credential).getId());
  }

  @Override
  public Credential save(Credential credential) {
    return credentialRepo.save(credential);
  }

}

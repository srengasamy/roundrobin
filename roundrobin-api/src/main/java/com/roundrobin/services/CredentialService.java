package com.roundrobin.services;

import com.roundrobin.api.CredentialTo;
import com.roundrobin.domain.Credential;

public interface CredentialService {
  public Credential get(String id);
  public CredentialTo read(String id); 
  public Credential create(CredentialTo credentialTo);
  public CredentialTo update(CredentialTo credentialTo);
  public Credential save(Credential credential);
}

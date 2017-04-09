package com.roundrobin.core.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultUserAuthenticationConverter implements UserAuthenticationConverter {
  @Override
  public Map<String, ?> convertUserAuthentication(Authentication userAuthentication) {
    return null;
  }

  @Override
  public Authentication extractAuthentication(Map<String, ?> map) {
    return null;
  }
}

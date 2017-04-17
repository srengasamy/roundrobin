package com.roundrobin.auth.config;

import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

public class MongoTokenConverter extends JwtAccessTokenConverter {

  @Override
  protected Map<String, Object> decode(String token) {
    return super.decode(token);
  }

  @Override
  protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    return super.encode(accessToken, authentication);
  }

}

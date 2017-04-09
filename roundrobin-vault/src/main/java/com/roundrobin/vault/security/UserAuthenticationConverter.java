package com.roundrobin.vault.security;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roundrobin.core.api.User;

public class UserAuthenticationConverter extends DefaultUserAuthenticationConverter {
  private ObjectMapper mapper = new ObjectMapper();

  @Override
  public Authentication extractAuthentication(Map<String, ?> map) {
    try {
      return new UsernamePasswordAuthenticationToken(mapper.convertValue(map, User.class), "N/A", getAuthorities(map));
    } catch (Exception e) {
      return super.extractAuthentication(map);
    }

  }

  private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
    if (!map.containsKey(AUTHORITIES)) {
      return null;
    }
    Object authorities = map.get(AUTHORITIES);
    if (authorities instanceof String) {
      return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
    }
    if (authorities instanceof Collection) {
      return AuthorityUtils.commaSeparatedStringToAuthorityList(
          StringUtils.collectionToCommaDelimitedString((Collection<?>) authorities));
    }
    throw new IllegalArgumentException("Authorities must be either a String or a Collection");
  }
}

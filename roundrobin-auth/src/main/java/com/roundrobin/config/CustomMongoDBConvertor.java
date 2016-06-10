/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.roundrobin.config;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import com.mongodb.DBObject;
import com.roundrobin.domain.User;
import com.roundrobin.repository.UserRepository;
import com.roundrobin.service.ClientDetailService;
import com.roundrobin.service.UserService;

/**
 * 
 *
 * @author malike_st
 */
@Configuration
public class CustomMongoDBConvertor implements Converter<DBObject, OAuth2Authentication> {

  // @Autowired
  private UserService authConfigService;
  // @Autowired
  private ClientDetailService clientDetailService;

  // @Autowired
  private UserRepository userRepo;


  @Override
  public OAuth2Authentication convert(DBObject source) {
    DBObject storedRequest = (DBObject) source.get("storedRequest");
    OAuth2Request oAuth2Request = new OAuth2Request((Map<String, String>) storedRequest.get("requestParameters"),
        (String) storedRequest.get("clientId"), null, true, new HashSet((List) storedRequest.get("scope")), null, null,
        null, null);
    DBObject userAuthorization = (DBObject) source.get("userAuthentication");
    if (null != userAuthorization) { // its a user
      Object prinObj = userAuthorization.get("principal");
      Optional<User> u = Optional.empty();
      if ((null != prinObj) && prinObj instanceof String) {
        u = userRepo.findByUsername((String) prinObj);
      } else if (null != prinObj) {
        DBObject principalDBO = (DBObject) prinObj;
        String email = (String) principalDBO.get("username");
        u = userRepo.findByUsername(email);
      }
      if (!u.isPresent()) {
        return null;
      }

      Authentication userAuthentication = new UserAuthenticationToken(u.get().getUsername(),
          (String) userAuthorization.get("credentials"),
          u.get().getRoles().stream().map(s -> new SimpleGrantedAuthority(s.toString())).collect(Collectors.toList()));
      OAuth2Authentication authentication = new OAuth2Authentication(oAuth2Request, userAuthentication);
      return authentication;
    } else { // its a client
      String clientId = (String) storedRequest.get("clientId");
      ClientDetails client = null;
      if ((null != clientId) && clientId instanceof String) {
        client = clientDetailService.loadClientByClientId(clientId);
      }
      if (null == client) {
        return null;
      }
      Authentication userAuthentication =
          new ClientAuthenticationToken(client.getClientId(), null, client.getAuthorities());
      return new OAuth2Authentication(oAuth2Request, userAuthentication);
    }
  }


}

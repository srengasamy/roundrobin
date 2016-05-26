package com.roundrobin.security;

/**
 * Created by rengasu on 5/25/16.
 */

import com.roundrobin.domain.Role;
import com.roundrobin.domain.User;
import com.roundrobin.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.authenticationManager(authenticationManager);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security.checkTokenAccess("isAuthenticated()");
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    // @formatter:off
    clients.inMemory().withClient("my-trusted-client")
            .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
            .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT").scopes("read", "write", "trust")
            .resourceIds("oauth2-resource").accessTokenValiditySeconds(600).and()
            .withClient("my-client-with-registered-redirect").authorizedGrantTypes("authorization_code")
            .authorities("ROLE_CLIENT").scopes("read", "trust").resourceIds("oauth2-resource")
            .redirectUris("http://anywhere?key=value").and().withClient("my-client-with-secret")
            .authorizedGrantTypes("client_credentials", "password").authorities("ROLE_CLIENT").scopes("read")
            .resourceIds("oauth2-resource").secret("secret");
    // @formatter:on
  }

  @Autowired
  public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repository) throws Exception {
    if (repository.count() == 0) {
      repository.save(new User("user", "password", Arrays.asList(new Role("USER"))));
    }
    builder.userDetailsService(userDetailsService(repository));
  }

  private UserDetailsService userDetailsService(final UserRepository repository) {
    return new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CustomUserDetails(repository.findByUsername(username));
      }
    };
  }
}

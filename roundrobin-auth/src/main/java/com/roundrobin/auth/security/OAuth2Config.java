package com.roundrobin.auth.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import com.roundrobin.auth.config.CustomTokenEnhancer;
import com.roundrobin.auth.service.ClientDetailService;
import com.roundrobin.auth.service.TokenStoreService;

@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private ClientDetailService clientService;

  @Autowired
  private TokenStoreService tokenStore;

  @Autowired
  private CustomTokenEnhancer tokenEnhancer;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.tokenServices(tokenServices()).authenticationManager(authenticationManager);
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.withClientDetails(clientService);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security.passwordEncoder(passwordEncoder);
  }

  @Bean
  public DefaultTokenServices tokenServices() {
    DefaultTokenServices tokenServices = new DefaultTokenServices();
    tokenServices.setTokenEnhancer(tokenEnhancer);
    tokenServices.setTokenStore(tokenStore);
    tokenServices.setSupportRefreshToken(true);
    return tokenServices;
  }

  @Bean
  protected ResourceServerConfiguration adminResources() {
    ResourceServerConfiguration resource = new ResourceServerConfiguration() {
      public void setConfigurers(List<ResourceServerConfigurer> configurers) {
        super.setConfigurers(configurers);
      }
    };
    resource.setConfigurers(Arrays.<ResourceServerConfigurer>asList(new ResourceServerConfigurerAdapter() {
      @Override
      public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("roundrobin-auth");
      }

      @Override
      public void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/admin/**").authorizeRequests().anyRequest().access("#oauth2.hasScope('write')");
      }
    }));
    resource.setOrder(3);
    return resource;
  }

  @Bean
  protected ResourceServerConfiguration otherResources() {
    ResourceServerConfiguration resource = new ResourceServerConfiguration() {
      public void setConfigurers(List<ResourceServerConfigurer> configurers) {
        super.setConfigurers(configurers);
      }
    };
    resource.setConfigurers(Arrays.<ResourceServerConfigurer>asList(new ResourceServerConfigurerAdapter() {
      @Override
      public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("roundrobin-auth");
      }

      @Override
      public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("user-action/**").anonymous();
      }
    }));
    resource.setOrder(4);
    return resource;
  }
}

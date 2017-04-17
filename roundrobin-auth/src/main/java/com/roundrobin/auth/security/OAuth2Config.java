package com.roundrobin.auth.security;

import com.roundrobin.auth.config.CustomTokenEnhancer;
import com.roundrobin.auth.config.MongoTokenConverter;
import com.roundrobin.auth.config.MongoTokenStore;
import com.roundrobin.auth.repository.AccessTokenRepository;
import com.roundrobin.auth.repository.RefreshTokenRepository;
import com.roundrobin.auth.service.ClientDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private ClientDetailService clientService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private CustomTokenEnhancer tokenEnhancer;

  @Autowired
  private AccessTokenRepository accessTokenRepo;

  @Autowired
  private RefreshTokenRepository refreshTokenRepo;

  @Value("${keystore.password}")
  private String keystorePassword;

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer, accessTokenConverter()));
    endpoints.tokenStore(tokenStore()).tokenEnhancer(tokenEnhancerChain).authenticationManager(authenticationManager);
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.withClientDetails(clientService);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security.checkTokenAccess("permitAll()");
    security.passwordEncoder(passwordEncoder);
  }

  @Bean
  public MongoTokenConverter accessTokenConverter() {
    KeyStoreKeyFactory keyStoreKeyFactory =
            new KeyStoreKeyFactory(new ClassPathResource("roundrobin-auth.jks"), keystorePassword.toCharArray());
    MongoTokenConverter converter = new MongoTokenConverter();
    converter.setKeyPair(keyStoreKeyFactory.getKeyPair("roundrobin-auth"));
    return converter;
  }

  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {
    DefaultTokenServices tokenServices = new DefaultTokenServices();
    tokenServices.setTokenStore(tokenStore());
    tokenServices.setSupportRefreshToken(true);
    return tokenServices;
  }

  @Bean
  public MongoTokenStore tokenStore() {
    return new MongoTokenStore(accessTokenConverter(), accessTokenRepo, refreshTokenRepo);
  }

}

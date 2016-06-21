package com.roundrobin.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import com.roundrobin.auth.config.CustomTokenEnhancer;
import com.roundrobin.auth.config.MongoTokenConverter;
import com.roundrobin.auth.config.MongoTokenStore;
import com.roundrobin.auth.domain.User;
import com.roundrobin.auth.repository.AccessTokenRepository;
import com.roundrobin.auth.repository.RefreshTokenRepository;
import com.roundrobin.auth.repository.UserRepository;

/**
 * Created by rengasu on 5/24/16.
 */
@SpringBootApplication
@ComponentScan("com.roundrobin")
public class RoundRobin {
  @Value(value = "${keystore.password}")
  private String keystorePassword;

  @Autowired
  private CustomTokenEnhancer tokenEnhancer;

  @Autowired
  private AccessTokenRepository accessTokenRepo;

  @Autowired
  private RefreshTokenRepository refreshTokenRepo;

  @Autowired
  private UserRepository userRepository;

  public static void main(String[] args) {
    SpringApplication.run(RoundRobin.class, args);
  }

  @Autowired
  public void authenticationManager(AuthenticationManagerBuilder builder, PasswordEncoder passwordEncoder)
      throws Exception {
    builder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder);
  }

  private UserDetailsService userDetailsService() {
    return new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
          return new User(user.get());
        }
        throw new UsernameNotFoundException("User not found");
      }
    };
  }

  @Bean
  public UserAuthenticationConverter userAuthenticationConverter() {
    DefaultUserAuthenticationConverter defaultUserAuthenticationConverter = new DefaultUserAuthenticationConverter();
    defaultUserAuthenticationConverter.setUserDetailsService(userDetailsService());
    return defaultUserAuthenticationConverter;
  }

  @Bean
  public MongoTokenConverter accessTokenConverter() {
    KeyStoreKeyFactory keyStoreKeyFactory =
        new KeyStoreKeyFactory(new ClassPathResource("roundrobin-auth.jks"), keystorePassword.toCharArray());
    MongoTokenConverter converter = new MongoTokenConverter();
    converter.setKeyPair(keyStoreKeyFactory.getKeyPair("roundrobin-auth"));
    ((DefaultAccessTokenConverter) converter.getAccessTokenConverter())
        .setUserTokenConverter(userAuthenticationConverter());
    return converter;
  }

  @Bean
  public MongoTokenStore tokenStore() {
    return new MongoTokenStore(accessTokenConverter(), accessTokenRepo, refreshTokenRepo);
  }

  @Bean
  public DefaultTokenServices tokenServices() {
    DefaultTokenServices tokenServices = new DefaultTokenServices();
    tokenServices.setTokenEnhancer(tokenEnhancer);
    tokenServices.setTokenStore(tokenStore());
    tokenServices.setSupportRefreshToken(true);
    return tokenServices;
  }
}

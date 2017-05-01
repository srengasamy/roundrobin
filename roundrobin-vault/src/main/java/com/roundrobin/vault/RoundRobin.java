package com.roundrobin.vault;

import com.roundrobin.vault.security.UserAuthenticationConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

//TODO: Added invalid scope, resources and expired tests for all apis
//TODO: Check all api have _ in all fields
//TODO: complete skill resource test
@SpringBootApplication
@EnableResourceServer
@ComponentScan("com.roundrobin")
public class RoundRobin {

  public static void main(String[] args) {
    SpringApplication.run(RoundRobin.class, args);
  }

  @Bean
  @Autowired
  public JwtAccessTokenConverter accessTokenConverter(JwtAccessTokenConverter tokenConverter) {
    ((DefaultAccessTokenConverter) tokenConverter.getAccessTokenConverter())
        .setUserTokenConverter(new UserAuthenticationConverter());
    return tokenConverter;
  }

}

// TODO if preference is top rated, sort by review
// TODO Limit the end date for vendor search to 10 days
// TODO Consider overlap at user end agenda
/**
 * If Urgent = Find vendors, in .5 miles, select a vendor random, if not found , search for 1 mile
 * If Top reviews = Find vendors in 2 miles, groups vendors by 3.5 to 5 star and select random. If
 * cheap = find vendors in 3 miles, pick the bottom batch and select random. If urgent, dont ask
 * permission If not, give user to select review/price based on choice.
 */
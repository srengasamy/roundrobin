package com.roundrobin.map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.roundrobin.map.config.UserAuthenticationConverter;


@EnableResourceServer
@EnableWebSecurity
@SpringBootApplication
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

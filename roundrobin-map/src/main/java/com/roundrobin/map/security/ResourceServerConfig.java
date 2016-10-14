package com.roundrobin.map.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@Order(2)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
  @Value("${security.oauth2.resource.id}")
  private String resourceId;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.requestMatchers().antMatchers("/trail/**").and().authorizeRequests().anyRequest().access("#oauth2.hasScope('read')")
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources.resourceId(resourceId);
  }

}

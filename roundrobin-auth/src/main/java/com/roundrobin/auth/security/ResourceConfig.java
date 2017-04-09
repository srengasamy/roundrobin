package com.roundrobin.auth.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * Created by rengasu on 2/13/17.
 */
@Configuration
@EnableResourceServer
public class ResourceConfig extends ResourceServerConfigurerAdapter {
  @Value("${security.oauth2.resource.id}")
  private String resourceId;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    //@formatter:off
    http.authorizeRequests().
            antMatchers("/admin/**").access("#oauth2.hasScope('write')").
            antMatchers("/user-action/**").anonymous();
    //@formatter:on
    http.csrf().disable();
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources.resourceId(resourceId);
  }
}

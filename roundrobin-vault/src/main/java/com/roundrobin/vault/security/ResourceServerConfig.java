package com.roundrobin.vault.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Value("${security.oauth2.resource.id}")
  private String resourceId;

  @Value("${security.user.name}")
  private String username;

  @Value("${security.user.password}")
  private String password;

  @Value("${security.basic.realm}")
  private String realmName;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.httpBasic().realmName(realmName);
    http.authorizeRequests().
            antMatchers("/user-profile/**").access("#oauth2.hasScope('profile')").
            antMatchers("/bank-account/**", "/credit-card/**").access("#oauth2.hasScope('vault')").
            antMatchers("/admin/**").hasAnyRole("ADMIN").
            antMatchers("/**").denyAll();
    http.csrf().disable();
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources.resourceId(resourceId);
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser(username).password(password).roles("ADMIN");
  }
}

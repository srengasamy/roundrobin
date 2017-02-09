package com.roundrobin.map.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Order(1)
public class AdminResourceConfig extends WebSecurityConfigurerAdapter {
  @Value("${admin.username}")
  private String username;

  @Value("${admin.password}")
  private String password;

  @Value("${spring.application.name}")
  private String name;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.httpBasic().realmName(name);
    http.requestMatchers().antMatchers("/admin/**").and().authorizeRequests()
        .antMatchers("/admin/**").authenticated().and().csrf().disable();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser(username).password(password).roles("ADMIN");
  }
}

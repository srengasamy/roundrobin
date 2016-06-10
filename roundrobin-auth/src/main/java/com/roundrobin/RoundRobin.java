package com.roundrobin;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.roundrobin.domain.User;
import com.roundrobin.repository.UserRepository;
import com.roundrobin.security.CustomUserDetails;

/**
 * Created by rengasu on 5/24/16.
 */
@SpringBootApplication
@EnableMongoRepositories(basePackages="com.roundrobin.repository")
public class RoundRobin {

  public static void main(String[] args) {
    SpringApplication.run(RoundRobin.class, args);
  }

  @Autowired
  public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repository,
      PasswordEncoder passwordEncoder) throws Exception {
    builder.userDetailsService(userDetailsService(repository)).passwordEncoder(passwordEncoder);
  }

  private UserDetailsService userDetailsService(final UserRepository repository) {
    return new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByUsername(username);
        if (user.isPresent()) {
          return new CustomUserDetails(user.get());
        }
        throw new UsernameNotFoundException("User not found");
      }
    };
  }

  @Bean(name = "jasyptStringEncryptor")
  public static StringEncryptor stringEncryptor() {
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setPassword("c3191151-a21d-487a-b797-f532ca39a9d1");
    config.setAlgorithm("PBEWithMD5AndDES");
    config.setKeyObtentionIterations("1000");
    config.setPoolSize("1");
    config.setProviderName("SunJCE");
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setStringOutputType("base64");
    encryptor.setConfig(config);
    return encryptor;
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

  @Bean
  public ObjectMapper registerModules() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JodaModule());
    mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    return mapper;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(11);
  }

}

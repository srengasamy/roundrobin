package com.roundrobin;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.roundrobin.repository.UserRepository;
import com.roundrobin.security.CustomUserDetails;

/**
 * Created by rengasu on 5/24/16.
 */
@SpringBootApplication
public class RoundRobin {
  public static void main(String[] args) {
    SpringApplication.run(RoundRobin.class, args);
  }

  @Autowired
  public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repository) throws Exception {
    builder.userDetailsService(userDetailsService(repository));
  }

  private UserDetailsService userDetailsService(final UserRepository repository) {
    return new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CustomUserDetails(repository.findByUsername(username));
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
}

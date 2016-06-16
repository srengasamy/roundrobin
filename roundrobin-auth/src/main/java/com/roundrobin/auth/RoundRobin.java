package com.roundrobin.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.roundrobin.auth.domain.User;
import com.roundrobin.auth.repository.UserRepository;

/**
 * Created by rengasu on 5/24/16.
 */
@SpringBootApplication
@ComponentScan("com.roundrobin")
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
          return new User(user.get());
        }
        throw new UsernameNotFoundException("User not found");
      }
    };
  }

}

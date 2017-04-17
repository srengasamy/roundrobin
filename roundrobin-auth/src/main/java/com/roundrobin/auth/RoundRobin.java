package com.roundrobin.auth;

import com.roundrobin.auth.domain.User;
import com.roundrobin.auth.domain.UserDetail;
import com.roundrobin.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

/**
 * Created by rengasu on 5/24/16.
 */
//TODO: Send user action by mail
@SpringBootApplication
@ComponentScan("com.roundrobin")
public class RoundRobin {

  public static void main(String[] args) {
    SpringApplication.run(RoundRobin.class, args);
  }

  @Autowired
  public void authenticationManager(AuthenticationManagerBuilder builder,
                                    PasswordEncoder passwordEncoder,
                                    UserRepository userRepository) throws Exception {
    UserDetailsService userDetailsService = (String username) -> {
      Optional<User> user = userRepository.findByUsername(username);
      if (user.isPresent()) {
        return new UserDetail(user.get());
      }
      throw new UsernameNotFoundException("User not found");
    };
    builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }

}

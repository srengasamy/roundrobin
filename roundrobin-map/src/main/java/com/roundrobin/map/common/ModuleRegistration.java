package com.roundrobin.map.common;

import java.util.Arrays;
import java.util.List;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.geo.GeoModule;
import org.springframework.data.mongodb.core.geo.GeoJsonModule;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@Component
@Scope("singleton")
public class ModuleRegistration {

  @Bean
  public ObjectMapper registerModules() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JodaModule());
    mapper.registerModule(new GeoModule());
    mapper.registerModule(new GeoJsonModule());
    mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    return mapper;
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
  @ConditionalOnProperty({"security.oauth2.resource.id"})
  protected ResourceServerConfiguration mapResources(final @Value("${security.oauth2.resource.id}") String resourceId) {
    ResourceServerConfiguration resource = new ResourceServerConfiguration() {
      public void setConfigurers(List<ResourceServerConfigurer> configurers) {
        super.setConfigurers(configurers);
      }
    };
    resource.setConfigurers(Arrays.<ResourceServerConfigurer>asList(new ResourceServerConfigurerAdapter() {
      @Override
      public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(resourceId);
      }

      @Override
      public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers("trail/**").and().authorizeRequests().anyRequest()
            .access("#oauth2.hasScope('read')").and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
      }
    }));
    resource.setOrder(7);
    return resource;
  }
}

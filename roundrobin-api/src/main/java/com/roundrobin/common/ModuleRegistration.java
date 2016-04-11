package com.roundrobin.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
@Scope("singleton")
public class ModuleRegistration {

  @Bean
  public Jackson2ObjectMapperBuilder registerGuava() {
    Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.json();
    builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    builder.serializationInclusion(JsonInclude.Include.NON_NULL);
    builder.modules(new GuavaModule());
    builder.modules(new Jdk8Module());
    builder.modules(new JavaTimeModule());
    return builder;
  }

}

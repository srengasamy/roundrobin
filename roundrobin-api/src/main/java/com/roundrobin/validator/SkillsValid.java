package com.roundrobin.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = SkillsValidValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SkillsValid {
  String message() default "Skills are not valid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

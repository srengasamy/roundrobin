package com.roundrobin.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = SkillsRequiredValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SkillsRequired {
  String message() default "Skills are required";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

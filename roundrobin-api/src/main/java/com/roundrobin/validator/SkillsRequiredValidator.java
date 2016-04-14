package com.roundrobin.validator;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.roundrobin.domain.User;

public class SkillsRequiredValidator implements ConstraintValidator<SkillsRequired, User> {

  @Override
  public void initialize(SkillsRequired constraintAnnotation) {

  }

  @Override
  public boolean isValid(User user, ConstraintValidatorContext context) {
    if(user.getProfile() == null){
      return false;
    }
    Optional<Boolean> vendor = Optional.ofNullable(user.getProfile().getVendor());
    if (vendor.isPresent() && vendor.get()) {
      if (user.getSkills() == null || user.getSkills().size() == 0) {
        return false;
      }
    }
    return true;
  }

}

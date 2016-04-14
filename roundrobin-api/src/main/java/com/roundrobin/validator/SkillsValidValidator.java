package com.roundrobin.validator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.common.collect.Sets;
import com.roundrobin.domain.User;

public class SkillsValidValidator implements ConstraintValidator<SkillsValid, User> {

  @Override
  public void initialize(SkillsValid constraintAnnotation) {}

  @Override
  public boolean isValid(User user, ConstraintValidatorContext context) {
    if (user.getSkills() == null) {
      return true;
    }
    List<String> allIds = user.getSkills().stream().filter(s -> s.getSkillDetails() != null)
        .map(s -> s.getSkillDetails()).filter(s -> s.getId() != null).map(s -> s.getId()).collect(Collectors.toList());
    Set<String> uniqueIds = Sets.newHashSet(allIds);
    if (allIds.size() != uniqueIds.size()) {
      return false;
    }
    return true;
  }

}

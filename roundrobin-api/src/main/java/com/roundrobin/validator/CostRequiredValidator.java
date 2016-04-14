package com.roundrobin.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.roundrobin.domain.Skill;

public class CostRequiredValidator implements ConstraintValidator<CostRequired, Skill> {

  @Override
  public void initialize(CostRequired constraintAnnotation) {

  }

  @Override
  public boolean isValid(Skill skill, ConstraintValidatorContext context) {
    if (skill.getCost().isPresent() && skill.getCost().get() > 0) {
      return true;
    }
    if ((skill.getMinCost().isPresent() && skill.getMaxCost().isPresent()) && skill.getMinCost().get() > 0
        && skill.getMaxCost().get() > 0 && (skill.getMinCost().get() < skill.getMaxCost().get())) {
      return true;
    }
    return false;
  }

}

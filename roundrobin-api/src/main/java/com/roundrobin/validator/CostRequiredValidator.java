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
    if (skill.getCost() > 0) {
      return true;
    }
    if (skill.getMinCost() > 0 && skill.getMaxCost() > 0 && (skill.getMinCost() < skill.getMaxCost())) {
      return true;
    }
    return false;
  }

}

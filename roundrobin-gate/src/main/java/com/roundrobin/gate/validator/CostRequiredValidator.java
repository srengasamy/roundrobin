package com.roundrobin.gate.validator;

import com.roundrobin.gate.api.SkillTo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CostRequiredValidator implements ConstraintValidator<CostRequired, SkillTo> {

  @Override
  public void initialize(CostRequired constraintAnnotation) {

  }

  @Override
  public boolean isValid(SkillTo skillTo, ConstraintValidatorContext context) {
    if (skillTo.getCost().isPresent() && (skillTo.getMinCost().isPresent() || skillTo.getMaxCost().isPresent())) {
      return false;
    }
    if (skillTo.getCost().isPresent() && skillTo.getCost().get() > 0) {
      return true;
    }
    if ((skillTo.getMinCost().isPresent() && skillTo.getMaxCost().isPresent()) && skillTo.getMinCost().get() > 0
            && skillTo.getMaxCost().get() > 0 && (skillTo.getMinCost().get() < skillTo.getMaxCost().get())) {
      return true;
    }
    return false;
  }

}

package com.roundrobin.services;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.roundrobin.common.ErrorCodes;
import com.roundrobin.domain.Skill;
import com.roundrobin.domain.SkillDetail;
import com.roundrobin.repository.SkillDetailRepository;
import com.roundrobin.repository.SkillRepository;

@Service
public class SkillServiceImpl implements SkillService {
  @Autowired
  private SkillDetailRepository skillDetailsRepo;

  @Autowired
  private SkillRepository skillRepo;

  @Override
  public Skill read(String id) {
    Optional<Skill> existing = skillRepo.findById(id);
    checkArgument(existing.isPresent(), ErrorCodes.INVALID_SKILL_ID);
    return existing.get();
  }

  @Override
  public Skill create(Skill skill) {
    Optional<SkillDetail> skillDetails = skillDetailsRepo.findById(skill.getSkillDetails().getId());
    checkArgument(skillDetails.isPresent(), ErrorCodes.INVALID_SKILL_DETAIL_ID);
    return skillRepo.save(skill);
  }

  @Override
  public Skill update(Skill skill) {
    checkArgument(!StringUtils.isEmpty(skill.getId()), ErrorCodes.INVALID_SKILL_DETAIL_ID);
    Optional<Skill> existing = skillRepo.findById(skill.getId());
    checkArgument(existing.isPresent(), ErrorCodes.INVALID_SKILL_DETAIL_ID);
    if (skill.getActive().isPresent()) {
      existing.get().setActive(skill.getActive());
    }
    if (skill.getCost().isPresent()) {
      existing.get().setCost(skill.getCost());
    }
    if (skill.getMinCost().isPresent()) {
      existing.get().setMinCost(skill.getMinCost());
    }
    if (skill.getMaxCost().isPresent()) {
      existing.get().setMaxCost(skill.getMaxCost());
    }
    if(skill.getTimeToComplete().isPresent()){
      existing.get().setTimeToComplete(skill.getTimeToComplete());
    }
    return skillRepo.save(existing.get());
  }

}

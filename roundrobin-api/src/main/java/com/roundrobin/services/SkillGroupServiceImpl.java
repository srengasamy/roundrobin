package com.roundrobin.services;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.common.ErrorCodes;
import com.roundrobin.domain.SkillGroup;
import com.roundrobin.repository.SkillGroupRepository;

@Service
public class SkillGroupServiceImpl implements SkillGroupService {

  @Autowired
  private SkillGroupRepository skillGroupRepo;

  @Override
  public SkillGroup read(String id) {
    Optional<SkillGroup> existing = skillGroupRepo.findById(id);
    checkArgument(existing.isPresent(), ErrorCodes.INVALID_SKILL_GROUP_ID);
    return existing.get();
  }

  @Override
  public SkillGroup create(SkillGroup skillGroup) {
    return skillGroupRepo.save(skillGroup);
  }

  @Override
  public SkillGroup update(SkillGroup skillGroup) {
    Optional<SkillGroup> existing = skillGroupRepo.findById(skillGroup.getId());
    checkArgument(existing.isPresent(), ErrorCodes.INVALID_SKILL_GROUP_ID);
    if(skillGroup.getGroupName().isPresent()){
      existing.get().setGroupName(skillGroup.getGroupName());
    }
    if(skillGroup.getActive().isPresent()){
      existing.get().setActive(skillGroup.getActive());
    }
    return skillGroupRepo.save(existing.get());
  }



}

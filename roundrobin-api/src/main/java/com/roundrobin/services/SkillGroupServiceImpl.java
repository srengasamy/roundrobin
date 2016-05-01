package com.roundrobin.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.api.SkillGroupTo;
import com.roundrobin.common.Assert;
import com.roundrobin.common.ErrorCode;
import com.roundrobin.domain.SkillGroup;
import com.roundrobin.repository.SkillGroupRepository;

@Service
public class SkillGroupServiceImpl implements SkillGroupService {

  @Autowired
  private SkillGroupRepository skillGroupRepo;

  @Override
  public SkillGroup get(String id) {
    Optional<SkillGroup> skillGroup = skillGroupRepo.findById(id);
    Assert.isTrue(skillGroup.isPresent() && skillGroup.get().getActive(), ErrorCode.INVALID_SKILL_GROUP_ID);
    return skillGroup.get();
  }

  @Override
  public SkillGroupTo read(String id) {
    SkillGroup skillGroup = get(id);
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setId(id);
    skillGroupTo.setGroupName(Optional.of(skillGroup.getGroupName()));
    return skillGroupTo;
  }

  @Override
  public SkillGroupTo create(SkillGroupTo skillGroupTo) {
    Optional<SkillGroup> existing = skillGroupRepo.findByGroupName(skillGroupTo.getGroupName().get());
    Assert.isTrue(!existing.isPresent(), ErrorCode.SKILL_GROUP_ALREADY_EXISTS);
    SkillGroup skillGroup = new SkillGroup();
    skillGroup.setActive(true);
    skillGroup.setGroupName(skillGroupTo.getGroupName().get());
    return read(skillGroupRepo.save(skillGroup).getId());
  }

  @Override
  public SkillGroupTo update(SkillGroupTo skillGroupTo) {
    SkillGroup skillGroup = get(skillGroupTo.getId());
    if (skillGroupTo.getGroupName().isPresent()) {
      skillGroup.setGroupName(skillGroupTo.getGroupName().get());
    }
    return read(skillGroupRepo.save(skillGroup).getId());
  }

  @Override
  public void delete(String id) {
    SkillGroup skillGroup = get(id);
    skillGroup.setActive(false);
    skillGroupRepo.save(skillGroup);
  }

}

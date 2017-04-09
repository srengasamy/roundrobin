package com.roundrobin.vault.service;

import com.roundrobin.vault.api.SkillGroupTo;
import com.roundrobin.vault.domain.SkillGroup;
import com.roundrobin.vault.repository.SkillGroupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.roundrobin.core.common.Preconditions.badRequest;
import static com.roundrobin.vault.common.ErrorCodes.INVALID_SKILL_GROUP_ID;
import static com.roundrobin.vault.common.ErrorCodes.SKILL_GROUP_ALREADY_EXISTS;

@Service
public class SkillGroupService {

  @Autowired
  private SkillGroupRepository skillGroupRepo;

  public SkillGroup get(String id) {
    Optional<SkillGroup> skillGroup = skillGroupRepo.findById(id);
    badRequest(skillGroup.isPresent() && skillGroup.get().getActive(),
            INVALID_SKILL_GROUP_ID, "skill_group_id");
    return skillGroup.get();
  }

  public SkillGroupTo read(String id) {
    return convert(get(id));
  }

  public SkillGroupTo create(SkillGroupTo skillGroupTo) {
    Optional<SkillGroup> existing = skillGroupRepo.findByGroupName(skillGroupTo.getGroupName().get());
    badRequest(!existing.isPresent(), SKILL_GROUP_ALREADY_EXISTS, "skill_group_name");
    SkillGroup skillGroup = new SkillGroup();
    skillGroup.setActive(true);
    skillGroup.setGroupName(skillGroupTo.getGroupName().get());
    skillGroupRepo.save(skillGroup);
    return convert(skillGroup);
  }

  public SkillGroupTo update(SkillGroupTo skillGroupTo) {
    SkillGroup skillGroup = get(skillGroupTo.getId());
    if (skillGroupTo.getGroupName().isPresent()) {
      skillGroup.setGroupName(skillGroupTo.getGroupName().get());
    }
    skillGroupRepo.save(skillGroup);
    return convert(skillGroup);
  }

  public void delete(String id) {
    SkillGroup skillGroup = get(id);
    skillGroup.setActive(false);
    skillGroupRepo.save(skillGroup);
  }

  private SkillGroupTo convert(SkillGroup skillGroup) {
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setId(skillGroup.getId());
    skillGroupTo.setGroupName(Optional.of(skillGroup.getGroupName()));
    return skillGroupTo;
  }

  public List<SkillGroupTo> list() {
    return skillGroupRepo.findAllByActive(true).stream().map(c -> convert(c)).collect(Collectors.toList());
  }
}

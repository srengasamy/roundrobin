package com.roundrobin.vault.services;

import static com.roundrobin.conditions.Preconditions.checkArgument;
import static com.roundrobin.vault.error.VaultErrorCode.INVALID_SKILL_GROUP_ID;
import static com.roundrobin.vault.error.VaultErrorCode.SKILL_GROUP_ALREADY_EXISTS;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.exception.BadRequestException;
import com.roundrobin.vault.api.SkillGroupTo;
import com.roundrobin.vault.domain.SkillGroup;
import com.roundrobin.vault.repository.SkillGroupRepository;

@Service
public class SkillGroupService {

  @Autowired
  private SkillGroupRepository skillGroupRepo;

  public SkillGroup get(String id) {
    Optional<SkillGroup> skillGroup = skillGroupRepo.findById(id);
    checkArgument(skillGroup.isPresent() && skillGroup.get().getActive(),
        new BadRequestException(INVALID_SKILL_GROUP_ID, "skill_group_id"));
    return skillGroup.get();
  }

  public SkillGroupTo read(String id) {
    return convert(get(id));
  }

  public SkillGroupTo create(SkillGroupTo skillGroupTo) {
    Optional<SkillGroup> existing = skillGroupRepo.findByGroupName(skillGroupTo.getGroupName().get());
    checkArgument(!existing.isPresent(), new BadRequestException(SKILL_GROUP_ALREADY_EXISTS, "skill_group_name"));
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

package com.roundrobin.services;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.api.SkillTo;
import com.roundrobin.common.ErrorCodes;
import com.roundrobin.domain.Skill;
import com.roundrobin.domain.SkillDetail;
import com.roundrobin.domain.UserProfile;
import com.roundrobin.repository.SkillRepository;

@Service
public class SkillServiceImpl implements SkillService {
  @Autowired
  private SkillDetailService skillDetailService;

  @Autowired
  private SkillRepository skillRepo;

  @Autowired
  private UserProfileService profileService;

  @Override
  public Skill get(String id) {
    Optional<Skill> skill = skillRepo.findById(id);
    checkArgument(skill.isPresent() && skill.get().getActive(), ErrorCodes.INVALID_SKILL_ID);
    return skill.get();
  }

  @Override
  public Skill save(Skill skill) {
    return skillRepo.save(skill);
  }

  @Override
  public SkillTo read(String id) {
    Skill skill = get(id);
    SkillTo skillTo = new SkillTo();
    skillTo.setId(skill.getId());
    skillTo.setCost(Optional.ofNullable(skill.getCost()));
    skillTo.setMinCost(Optional.ofNullable(skill.getMinCost()));
    skillTo.setMaxCost(Optional.ofNullable(skill.getMaxCost()));
    skillTo.setTimeToComplete(Optional.of(skill.getTimeToComplete()));
    return skillTo;
  }

  @Override
  public SkillTo create(SkillTo skillTo) {
    SkillDetail skillDetail = skillDetailService.get(skillTo.getSkillDetailId());
    UserProfile userProfile = profileService.get(skillTo.getUserProfileId());
    checkSkillExists(skillDetail.getId(), userProfile);
    Skill skill = new Skill();
    skill.setTimeToComplete(skillTo.getTimeToComplete().get());
    skill.setCost(skillTo.getCost().orElse(null));
    skill.setMinCost(skillTo.getMinCost().orElse(null));
    skill.setMaxCost(skillTo.getMaxCost().orElse(null));
    skill.setActive(true);
    skill.setCreated(DateTime.now());
    skill.setSkillDetails(skillDetail);
    save(skill);
    userProfile.getSkills().add(skill);
    profileService.save(userProfile);
    return read(skill.getId());
  }

  @Override
  public SkillTo update(SkillTo skillTo) {
    Skill skill = get(skillTo.getId());
    skill.setMaxCost(skillTo.getMaxCost().orElse(skill.getMaxCost()));
    skill.setMinCost(skillTo.getMinCost().orElse(skill.getMinCost()));
    skill.setCost(skillTo.getCost().orElse(skill.getCost()));
    skill.setTimeToComplete(skillTo.getTimeToComplete().orElse(skill.getTimeToComplete()));
    return read(save(skill).getId());
  }

  @Override
  public void delete(String id) {
    Skill skill = get(id);
    skill.setActive(false);
    save(skill);
  }

  private void checkSkillExists(String skillDetailId, UserProfile userProfile) {
    boolean exists = userProfile.getSkills().stream().map(s -> s.getSkillDetails().getId()).collect(Collectors.toList())
        .contains(skillDetailId);
    checkArgument(!exists, ErrorCodes.SKILL_ALREADY_EXISTS);
  }


}

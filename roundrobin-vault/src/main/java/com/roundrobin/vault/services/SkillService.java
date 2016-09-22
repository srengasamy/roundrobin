package com.roundrobin.vault.services;

import static com.roundrobin.conditions.Preconditions.checkArgument;
import static com.roundrobin.vault.error.VaultErrorCode.INVALID_SKILL_DETAIL_ID;
import static com.roundrobin.vault.error.VaultErrorCode.INVALID_SKILL_ID;
import static com.roundrobin.vault.error.VaultErrorCode.SKILL_ALREADY_EXISTS;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.exception.BadRequestException;
import com.roundrobin.vault.api.SkillTo;
import com.roundrobin.vault.domain.Skill;
import com.roundrobin.vault.domain.SkillDetail;
import com.roundrobin.vault.domain.UserProfile;
import com.roundrobin.vault.repository.SkillRepository;

@Service
public class SkillService {
  @Autowired
  private SkillDetailService skillDetailService;

  @Autowired
  private SkillRepository skillRepo;

  @Autowired
  private UserProfileService profileService;

  private Skill get(String userId, String skillId) {
    UserProfile profile = profileService.getByUserId(userId);
    Optional<Skill> skill =
        profile.getSkills().stream().filter(c -> c.getActive() && c.getId().equals(skillId)).findFirst();
    checkArgument(skill.isPresent(), new BadRequestException(INVALID_SKILL_ID, "skill_id"));
    return skill.get();
  }

  public Skill getBySkillDetailId(String userId, String skillDetailId) {
    UserProfile profile = profileService.getByUserId(userId);
    Optional<Skill> skill = profile.getSkills().stream()
        .filter(c -> c.getActive() && c.getSkillDetails().getId().equals(skillDetailId)).findFirst();
    checkArgument(skill.isPresent(), new BadRequestException(INVALID_SKILL_DETAIL_ID, "skill_detail_id"));
    return skill.get();
  }

  private Skill save(Skill skill) {
    return skillRepo.save(skill);
  }

  public SkillTo read(String userId, String skillId) {
    return convert(get(userId, skillId));
  }

  public SkillTo create(String userId, SkillTo skillTo) {
    SkillDetail skillDetail = skillDetailService.get(skillTo.getSkillDetailId());
    UserProfile userProfile = profileService.getByUserId(userId);
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
    return convert(skill);
  }

  public SkillTo update(String userId, SkillTo skillTo) {
    Skill skill = get(userId, skillTo.getId());
    skill.setMaxCost(skillTo.getMaxCost().orElse(skill.getMaxCost()));
    skill.setMinCost(skillTo.getMinCost().orElse(skill.getMinCost()));
    skill.setCost(skillTo.getCost().orElse(skill.getCost()));
    skill.setTimeToComplete(skillTo.getTimeToComplete().orElse(skill.getTimeToComplete()));
    save(skill);
    return convert(skill);
  }

  public void delete(String userId, String skillId) {
    Skill skill = get(userId, skillId);
    skill.setActive(false);
    save(skill);
  }

  private void checkSkillExists(String skillDetailId, UserProfile userProfile) {
    boolean exists = userProfile.getSkills().stream().map(s -> s.getSkillDetails().getId()).collect(Collectors.toList())
        .contains(skillDetailId);
    checkArgument(!exists, new BadRequestException(SKILL_ALREADY_EXISTS, "skill_detail_id"));
  }

  public List<SkillTo> list(String userId) {
    UserProfile profile = profileService.getByUserId(userId);
    return profile.getSkills().stream().filter(c -> c.getActive()).map(c -> convert(c)).collect(Collectors.toList());
  }

  private SkillTo convert(Skill skill) {
    SkillTo skillTo = new SkillTo();
    skillTo.setId(skill.getId());
    skillTo.setCost(Optional.ofNullable(skill.getCost()));
    skillTo.setMinCost(Optional.ofNullable(skill.getMinCost()));
    skillTo.setMaxCost(Optional.ofNullable(skill.getMaxCost()));
    skillTo.setTimeToComplete(Optional.of(skill.getTimeToComplete()));
    return skillTo;
  }
}

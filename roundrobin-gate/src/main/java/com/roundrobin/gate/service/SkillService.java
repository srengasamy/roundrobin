package com.roundrobin.gate.service;

import com.roundrobin.core.api.User;
import com.roundrobin.core.service.GenericService;
import com.roundrobin.gate.api.SkillTo;
import com.roundrobin.gate.domain.Skill;
import com.roundrobin.gate.domain.SkillDetail;
import com.roundrobin.gate.domain.UserProfile;
import com.roundrobin.gate.repository.SkillRepository;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.roundrobin.core.common.Preconditions.badRequest;
import static com.roundrobin.gate.common.ErrorCodes.INVALID_SKILL_ID;
import static com.roundrobin.gate.common.ErrorCodes.SKILL_ALREADY_EXISTS;

@Service
public class SkillService implements GenericService<Skill, SkillTo> {
  @Autowired
  private SkillDetailService skillDetailService;

  @Autowired
  private SkillRepository skillRepo;

  @Autowired
  private UserProfileService profileService;

  @Override
  public Skill get(User user, String id) {
    Optional<Skill> skill = skillRepo.findById(id);
    badRequest(skill.isPresent()
            && skill.get().isActive()
            && skill.get().getProfile().getId().equals(user.getUserId()), INVALID_SKILL_ID, "skill_id");
    return skill.get();
  }

  @Override
  public Skill save(Skill skill) {
    return skillRepo.save(skill);
  }

  @Override
  public SkillTo read(User user, String id) {
    return convert(get(user, id));
  }

  @Override
  public SkillTo create(User user, SkillTo skillTo) {
    SkillDetail skillDetail = skillDetailService.get(skillTo.getSkillDetailId());
    UserProfile userProfile = profileService.get(user);
    checkSkillExists(skillDetail.getId(), userProfile);
    Skill skill = new Skill();
    skill.setTimeToComplete(skillTo.getTimeToComplete().get());
    skill.setCost(skillTo.getCost().orElse(null));
    skill.setMinCost(skillTo.getMinCost().orElse(null));
    skill.setMaxCost(skillTo.getMaxCost().orElse(null));
    skill.setActive(true);
    skill.setCreated(DateTime.now());
    skill.setSkillDetails(skillDetail);
    skill.setProfile(userProfile);
    return convert(save(skill));
  }

  @Override
  public SkillTo update(User user, SkillTo skillTo) {
    Skill skill = get(user, skillTo.getId());
    skill.setMaxCost(skillTo.getMaxCost().orElse(skill.getMaxCost()));
    skill.setMinCost(skillTo.getMinCost().orElse(skill.getMinCost()));
    skill.setCost(skillTo.getCost().orElse(skill.getCost()));
    skill.setTimeToComplete(skillTo.getTimeToComplete().orElse(skill.getTimeToComplete()));
    return convert(save(skill));
  }

  @Override
  public void delete(User user, String id) {
    Skill skill = get(user, id);
    skill.setActive(false);
    save(skill);
  }

  @Override
  public List<SkillTo> list(User user) {
    return skillRepo.findAllByProfile(profileService.get(user))
            .stream()
            .filter(c -> c.isActive())
            .map(c -> convert(c))
            .collect(Collectors.toList());
  }

  @Override
  public SkillTo convert(Skill skill) {
    SkillTo skillTo = new SkillTo();
    skillTo.setId(skill.getId());
    skillTo.setCost(Optional.ofNullable(skill.getCost()));
    skillTo.setMinCost(Optional.ofNullable(skill.getMinCost()));
    skillTo.setMaxCost(Optional.ofNullable(skill.getMaxCost()));
    skillTo.setTimeToComplete(Optional.of(skill.getTimeToComplete()));
    return skillTo;
  }

  private void checkSkillExists(String skillDetailId, UserProfile userProfile) {
    Optional<Skill> existing = skillRepo.findAllByProfile(userProfile)
            .stream()
            .filter(s -> s.getSkillDetails().getId().equals(skillDetailId))
            .findFirst();
    badRequest(!existing.isPresent(), SKILL_ALREADY_EXISTS, "skill_detail_id");
  }
}

package com.roundrobin.services;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.api.SkillDetailTo;
import com.roundrobin.common.ErrorCodes;
import com.roundrobin.domain.SkillDetail;
import com.roundrobin.domain.SkillGroup;
import com.roundrobin.repository.SkillDetailRepository;

@Service
public class SkillDetailServiceImpl implements SkillDetailService {

  @Autowired
  private SkillDetailRepository skillDetailRepo;

  @Autowired
  private SkillGroupService skillGroupService;

  @Override
  public SkillDetail get(String id) {
    Optional<SkillDetail> skillDetail = skillDetailRepo.findById(id);
    checkArgument(skillDetail.isPresent() && skillDetail.get().getActive(), ErrorCodes.INVALID_SKILL_DETAIL_ID);
    return skillDetail.get();
  }

  @Override
  public SkillDetailTo read(String id) {
    SkillDetail skillDetail = get(id);
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setDeliveryType(Optional.of(skillDetail.getDeliveryType()));
    skillDetailTo.setName(Optional.of(skillDetail.getName()));
    skillDetailTo.setId(skillDetail.getId());
    return skillDetailTo;
  }

  @Override
  public SkillDetailTo create(SkillDetailTo skillDetailTo) {
    SkillGroup skillGroup = skillGroupService.get(skillDetailTo.getSkillGroupId());
    SkillDetail skillDetail = new SkillDetail();
    skillDetail.setActive(true);
    skillDetail.setDeliveryType(skillDetailTo.getDeliveryType().get());
    skillDetail.setName(skillDetailTo.getName().get());
    skillDetail.setSkillGroup(skillGroup);
    return read(skillDetailRepo.save(skillDetail).getId());
  }

  @Override
  public SkillDetailTo update(SkillDetailTo skillDetailTo) {
    SkillDetail skillDetail = get(skillDetailTo.getId());
    if (skillDetailTo.getDeliveryType().isPresent()) {
      skillDetail.setDeliveryType(skillDetailTo.getDeliveryType().get());
    }
    if (skillDetailTo.getName().isPresent()) {
      skillDetail.setName(skillDetailTo.getName().get());
    }
    return read(skillDetailRepo.save(skillDetail).getId());
  }

  @Override
  public void delete(String id) {
    SkillDetail skillDetail = get(id);
    skillDetail.setActive(false);
    skillDetailRepo.save(skillDetail);
  }

}

package com.roundrobin.services;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.roundrobin.common.ErrorCodes;
import com.roundrobin.domain.SkillDetail;
import com.roundrobin.domain.SkillGroup;
import com.roundrobin.repository.SkillDetailRepository;
import com.roundrobin.repository.SkillGroupRepository;

@Service
public class SkillDetailServiceImpl implements SkillDetailService {

  @Autowired
  private SkillDetailRepository skillDetailRepo;

  @Autowired
  private SkillGroupRepository skillGroupRepo;

  @Override
  public SkillDetail read(String id) {
    Optional<SkillDetail> skillDetail = skillDetailRepo.findById(id);
    checkArgument(skillDetail.isPresent(), ErrorCodes.INVALID_SKILL_DETAIL_ID);
    return skillDetail.get();
  }

  @Override
  public SkillDetail create(SkillDetail skillDetail) {
    checkArgument(!StringUtils.isEmpty(skillDetail.getSkillGroup().getId()), ErrorCodes.INVALID_SKILL_GROUP_ID);
    Optional<SkillGroup> existing = skillGroupRepo.findById(skillDetail.getSkillGroup().getId());
    checkArgument(existing.isPresent(), ErrorCodes.INVALID_SKILL_GROUP_ID);
    skillDetail.setSkillGroup(existing.get());
    return skillDetailRepo.save(skillDetail);
  }

  @Override
  public SkillDetail update(SkillDetail skillDetail) {
    Optional<SkillDetail> existing = skillDetailRepo.findById(skillDetail.getId());
    checkArgument(existing.isPresent(), ErrorCodes.INVALID_SKILL_DETAIL_ID);
    if (skillDetail.getDeliveryType().isPresent()) {
      existing.get().setDeliveryType(skillDetail.getDeliveryType());
    }
    if(skillDetail.getName().isPresent()){
      existing.get().setName(skillDetail.getName());
    }
    if(skillDetail.getActive().isPresent()){
      existing.get().setActive(skillDetail.getActive());
    }
    return skillDetailRepo.save(existing.get());
  }

}

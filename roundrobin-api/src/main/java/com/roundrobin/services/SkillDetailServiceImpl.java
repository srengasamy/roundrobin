package com.roundrobin.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.api.SkillDetailTo;
import com.roundrobin.common.Assert;
import com.roundrobin.common.ErrorCode;
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
    Assert.isTrue(skillDetail.isPresent() && skillDetail.get().getActive(), ErrorCode.INVALID_SKILL_DETAIL_ID);
    return skillDetail.get();
  }

  @Override
  public SkillDetailTo read(String id) {
    return convert(get(id));
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

  private SkillDetailTo convert(SkillDetail skillDetail) {
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setDeliveryType(Optional.of(skillDetail.getDeliveryType()));
    skillDetailTo.setName(Optional.of(skillDetail.getName()));
    skillDetailTo.setId(skillDetail.getId());
    return skillDetailTo;
  }

  @Override
  public List<SkillDetailTo> list() {
    return skillDetailRepo.findAllByActive(true).stream().map(c -> convert(c)).collect(Collectors.toList());
  }
}

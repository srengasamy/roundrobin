package com.roundrobin.vault.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.common.Assert;
import com.roundrobin.vault.api.SkillDetailTo;
import com.roundrobin.vault.common.ErrorCode;
import com.roundrobin.vault.domain.SkillDetail;
import com.roundrobin.vault.domain.SkillGroup;
import com.roundrobin.vault.repository.SkillDetailRepository;

@Service
public class SkillDetailService {

  @Autowired
  private SkillDetailRepository skillDetailRepo;

  @Autowired
  private SkillGroupService skillGroupService;

  public SkillDetail get(String id) {
    Optional<SkillDetail> skillDetail = skillDetailRepo.findById(id);
    Assert.isTrue(skillDetail.isPresent() && skillDetail.get().getActive(), ErrorCode.INVALID_SKILL_DETAIL_ID);
    return skillDetail.get();
  }

  public SkillDetailTo read(String id) {
    return convert(get(id));
  }

  public SkillDetailTo create(SkillDetailTo skillDetailTo) {
    SkillGroup skillGroup = skillGroupService.get(skillDetailTo.getSkillGroupId());
    SkillDetail skillDetail = new SkillDetail();
    skillDetail.setActive(true);
    skillDetail.setDeliveryType(skillDetailTo.getDeliveryType().get());
    skillDetail.setName(skillDetailTo.getName().get());
    skillDetail.setSkillGroup(skillGroup);
    skillDetailRepo.save(skillDetail);
    return convert(skillDetail);
  }

  public SkillDetailTo update(SkillDetailTo skillDetailTo) {
    SkillDetail skillDetail = get(skillDetailTo.getId());
    if (skillDetailTo.getDeliveryType().isPresent()) {
      skillDetail.setDeliveryType(skillDetailTo.getDeliveryType().get());
    }
    if (skillDetailTo.getName().isPresent()) {
      skillDetail.setName(skillDetailTo.getName().get());
    }
    skillDetailRepo.save(skillDetail);
    return convert(skillDetail);
  }

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

  public List<SkillDetailTo> list() {
    return skillDetailRepo.findAllByActive(true).stream().map(c -> convert(c)).collect(Collectors.toList());
  }
}

package com.roundrobin.services;

import com.roundrobin.domain.SkillDetail;

public interface SkillDetailService {
  public SkillDetail read(String id);

  public SkillDetail create(SkillDetail skillDetail);

  public SkillDetail update(SkillDetail skillDetail);

}

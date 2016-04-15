package com.roundrobin.services;

import com.roundrobin.api.SkillDetailTo;
import com.roundrobin.domain.SkillDetail;

public interface SkillDetailService {
  public SkillDetail get(String id);

  public SkillDetailTo read(String id);

  public SkillDetailTo create(SkillDetailTo skillDetail);

  public SkillDetailTo update(SkillDetailTo skillDetail);
  
  public void delete(String id);

}

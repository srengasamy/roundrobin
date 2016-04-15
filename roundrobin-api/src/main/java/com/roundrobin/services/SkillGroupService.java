package com.roundrobin.services;

import com.roundrobin.api.SkillGroupTo;
import com.roundrobin.domain.SkillGroup;

public interface SkillGroupService {
  public SkillGroup get(String id);

  public SkillGroupTo read(String id);

  public SkillGroupTo create(SkillGroupTo skillGroup);

  public SkillGroupTo update(SkillGroupTo skillGroup);

  public void delete(String id);
}

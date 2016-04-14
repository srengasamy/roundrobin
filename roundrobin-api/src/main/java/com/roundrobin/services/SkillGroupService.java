package com.roundrobin.services;

import com.roundrobin.domain.SkillGroup;

public interface SkillGroupService {
  public SkillGroup read(String id);

  public SkillGroup create(SkillGroup skillGroup);

  public SkillGroup update(SkillGroup skillGroup);
}

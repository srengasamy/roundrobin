package com.roundrobin.services;

import com.roundrobin.domain.Skill;

public interface SkillService {
  public Skill read(String id);

  public Skill create(Skill skill);

  public Skill update(Skill skill);
}

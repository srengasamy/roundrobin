package com.roundrobin.services;

import com.roundrobin.api.SkillTo;
import com.roundrobin.domain.Skill;

import java.util.List;

public interface SkillService {
  public Skill get(String id);

  public Skill save(Skill skill);

  public SkillTo read(String id);

  public SkillTo create(SkillTo skill);

  public SkillTo update(SkillTo skill);

  public void delete(String id);

  public List<SkillTo> list(String id);
}

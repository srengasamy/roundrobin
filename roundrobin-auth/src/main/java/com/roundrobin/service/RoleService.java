package com.roundrobin.service;

import com.roundrobin.domain.Role;

public interface RoleService {
  public Role get(String id);

  public Role getByName(String name);
}

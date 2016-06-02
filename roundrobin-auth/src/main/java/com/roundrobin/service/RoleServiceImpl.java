package com.roundrobin.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.common.Assert;
import com.roundrobin.common.ErrorCode;
import com.roundrobin.domain.Role;
import com.roundrobin.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  private RoleRepository roleRepo;

  @Override
  public Role get(String id) {
    Optional<Role> role = roleRepo.findById(id);
    Assert.isTrue(role.isPresent(), ErrorCode.INVALID_ROLE_ID);
    return role.get();
  }

  @Override
  public Role getByName(String name) {
    Optional<Role> role = roleRepo.findByName(name);
    Assert.isTrue(role.isPresent(), ErrorCode.INVALID_ROLE_NAME);
    return role.get();
  }

}

package com.roundrobin.core.service;

import com.roundrobin.core.api.User;

import java.util.List;

/**
 * Created by rengasu on 3/2/17.
 */
public interface GenericService<D, T> {
  public D get(User user, String id);

  public D save(D d);

  public T read(User user, String id);

  public T create(User user, T t);

  public T update(User user, T t);

  public void delete(User user, String id);

  public List<T> list(User user);

  public T convert(D d);
}

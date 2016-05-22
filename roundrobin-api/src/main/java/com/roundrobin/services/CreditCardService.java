package com.roundrobin.services;

import com.roundrobin.api.CreditCardTo;
import com.roundrobin.domain.CreditCard;

import java.util.List;

public interface CreditCardService {
  public CreditCard get(String id);

  public CreditCardTo read(String id);

  public CreditCardTo create(CreditCardTo creditCardTo);

  public CreditCardTo update(CreditCardTo creditCardTo);

  public void delete(String id);

  public CreditCard save(CreditCard creditCard);

  public List<CreditCardTo> list(String id);

}

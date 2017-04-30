package com.roundrobin.vault.service;

import com.roundrobin.core.api.User;
import com.roundrobin.core.service.GenericService;
import com.roundrobin.vault.api.CreditCardTo;
import com.roundrobin.vault.domain.CreditCard;
import com.roundrobin.vault.domain.UserProfile;
import com.roundrobin.vault.repository.CreditCardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.roundrobin.core.common.Preconditions.badRequest;
import static com.roundrobin.vault.common.ErrorCodes.INVALID_CREDIT_CARD_ID;

@Service
public class CreditCardService implements GenericService<CreditCard, CreditCardTo> {
  @Autowired
  private CreditCardRepository creditCardRepo;

  @Autowired
  private UserProfileService profileService;

  @Override
  public CreditCard get(User user, String id) {
    Optional<CreditCard> creditCard = creditCardRepo.findById(id);
    badRequest(creditCard.isPresent()
                    && creditCard.get().isActive()
                    && creditCard.get().getProfile().getId().equals(user.getUserId()),
            INVALID_CREDIT_CARD_ID,
            "credit_card_id");
    return creditCard.get();
  }

  @Override
  public CreditCard save(CreditCard creditCard) {
    return creditCardRepo.save(creditCard);
  }

  @Override
  public CreditCardTo read(User user, String id) {
    return convert(get(user, id));
  }

  @Override
  public CreditCardTo create(User user, CreditCardTo creditCardTo) {
    UserProfile userProfile = profileService.get(user);
    CreditCard creditCard = new CreditCard();
    creditCard.setBrand(creditCardTo.getBrand());
    creditCard.setLast4(creditCardTo.getLast4());
    creditCard.setExpiryMonth(creditCardTo.getExpiryMonth());
    creditCard.setExpiryYear(creditCardTo.getExpiryYear());
    creditCard.setPostalCode(creditCardTo.getPostalCode());
    creditCard.setProfile(userProfile);
    creditCard.setActive(true);
    return convert(save(creditCard));
  }

  @Override
  public CreditCardTo update(User user, CreditCardTo creditCardTo) {
    CreditCard creditCard = get(user, creditCardTo.getId());
    creditCard.setPostalCode(creditCardTo.getPostalCode());
    creditCard.setExpiryYear(creditCardTo.getExpiryYear());
    creditCard.setExpiryMonth(creditCardTo.getExpiryMonth());
    return convert(save(creditCard));
  }

  @Override
  public void delete(User user, String id) {
    CreditCard creditCar = get(user, id);
    creditCar.setActive(false);
    save(creditCar);
  }

  @Override
  public List<CreditCardTo> list(User user) {
    UserProfile userProfile = profileService.get(user);
    return creditCardRepo.findAllByProfile(userProfile)
            .stream()
            .filter(c -> c.isActive()).map(c -> convert(c))
            .collect(Collectors.toList());
  }

  @Override
  public CreditCardTo convert(CreditCard creditCard) {
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setId(creditCard.getId());
    creditCardTo.setExpiryMonth(creditCard.getExpiryMonth());
    creditCardTo.setExpiryYear(creditCard.getExpiryYear());
    creditCardTo.setPostalCode(creditCard.getPostalCode());
    creditCardTo.setBrand(creditCard.getBrand());
    creditCardTo.setLast4(creditCard.getLast4());
    return creditCardTo;
  }
}

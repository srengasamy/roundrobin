package com.roundrobin.services;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.api.CreditCardTo;
import com.roundrobin.common.ErrorCodes;
import com.roundrobin.domain.CreditCard;
import com.roundrobin.domain.UserProfile;
import com.roundrobin.repository.CreditCardRepository;

@Service
public class CreditCardServiceImpl implements CreditCardService {
  @Autowired
  private CreditCardRepository creditCardRepo;
  @Autowired
  private UserProfileService profileService;

  @Override
  public CreditCard get(String id) {
    Optional<CreditCard> creditCard = creditCardRepo.findById(id);
    checkArgument(creditCard.isPresent() && creditCard.get().getActive(), ErrorCodes.INVALID_CREDIT_CARD_ID);
    return creditCard.get();
  }

  @Override
  public CreditCardTo read(String id) {
    CreditCard creditCard = get(id);
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setCardNumber(Optional.of(creditCard.getCardNumber()));
    creditCardTo.setNameOnCard(Optional.of(creditCard.getNameOnCard()));
    creditCardTo.setCvv(Optional.of(creditCard.getCvv()));
    creditCardTo.setExpiryMonth(Optional.of(creditCard.getExpiryMonth()));
    creditCardTo.setExpiryYear(Optional.of(creditCard.getExpiryYear()));
    creditCardTo.setPostalCode(Optional.of(creditCard.getPostalCode()));
    creditCardTo.setId(creditCard.getId());
    return creditCardTo;
  }

  @Override
  public CreditCardTo create(CreditCardTo creditCardTo) {
    UserProfile userProfile = profileService.get(creditCardTo.getUserProfileId());
    CreditCard creditCard = new CreditCard();
    creditCard.setCardNumber(creditCardTo.getCardNumber().get());
    creditCard.setNameOnCard(creditCardTo.getNameOnCard().get());
    creditCard.setCvv(creditCardTo.getCvv().get());
    creditCard.setExpiryMonth(creditCardTo.getExpiryMonth().get());
    creditCard.setExpiryYear(creditCardTo.getExpiryYear().get());
    creditCard.setPostalCode(creditCardTo.getPostalCode().get());
    creditCard.setActive(true);
    creditCard.setCreated(DateTime.now());
    save(creditCard);
    userProfile.getCreditCards().add(creditCard);
    profileService.save(userProfile);
    return read(creditCard.getId());
  }

  @Override
  public CreditCardTo update(CreditCardTo creditCardTo) {
    CreditCard creditCard = get(creditCardTo.getId());
    creditCard.setNameOnCard(creditCardTo.getNameOnCard().orElse(creditCard.getNameOnCard()));
    creditCard.setCardNumber(creditCardTo.getCardNumber().orElse(creditCard.getCardNumber()));
    creditCard.setCvv(creditCardTo.getCvv().orElse(creditCard.getCvv()));
    creditCard.setExpiryMonth(creditCardTo.getExpiryMonth().orElse(creditCard.getExpiryMonth()));
    creditCard.setExpiryYear(creditCardTo.getExpiryYear().orElse(creditCard.getExpiryYear()));
    creditCard.setPostalCode(creditCardTo.getPostalCode().orElse(creditCard.getPostalCode()));
    return read(save(creditCard).getId());
  }

  @Override
  public void delete(String id) {
    CreditCard creditCar = get(id);
    creditCar.setActive(false);
    save(creditCar);

  }

  @Override
  public CreditCard save(CreditCard creditCard) {
    return creditCardRepo.save(creditCard);
  }

}

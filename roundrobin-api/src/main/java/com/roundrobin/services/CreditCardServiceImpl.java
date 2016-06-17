package com.roundrobin.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jasypt.encryption.StringEncryptor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.api.CreditCardTo;
import com.roundrobin.common.Assert;
import com.roundrobin.common.ErrorCode;
import com.roundrobin.domain.CreditCard;
import com.roundrobin.domain.UserProfile;
import com.roundrobin.repository.CreditCardRepository;
import com.roundrobin.utils.StringUtils;

@Service
public class CreditCardServiceImpl implements CreditCardService {
  @Autowired
  private CreditCardRepository creditCardRepo;

  @Autowired
  private UserProfileService profileService;

  @Autowired
  private StringEncryptor encryptor;

  @Override
  public CreditCard get(String id) {
    Optional<CreditCard> creditCard = creditCardRepo.findById(id);
    Assert.isTrue(creditCard.isPresent() && creditCard.get().getActive(), ErrorCode.INVALID_CREDIT_CARD_ID);
    return creditCard.get();
  }

  @Override
  public CreditCardTo read(String id) {
    return convert(get(id));
  }

  @Override
  public CreditCardTo create(CreditCardTo creditCardTo) {
    UserProfile userProfile = profileService.get(creditCardTo.getUserProfileId());
    CreditCard creditCard = new CreditCard();
    creditCard.setCardNumber(encryptor.encrypt(creditCardTo.getCardNumber().get()));
    creditCard.setCvv(encryptor.encrypt(creditCardTo.getCvv().get()));
    creditCard.setExpiryMonth(creditCardTo.getExpiryMonth().get());
    creditCard.setExpiryYear(creditCardTo.getExpiryYear().get());
    creditCard.setPostalCode(creditCardTo.getPostalCode().get());
    creditCard.setMaskedCardNumber(StringUtils.mask(creditCardTo.getCardNumber().get()));
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

  @Override
  public List<CreditCardTo> list(String id) {
    UserProfile profile = profileService.get(id);
    return profile.getCreditCards().stream().filter(c -> c.getActive()).map(c -> convert(c)).collect(Collectors
            .toList());
  }

  private CreditCardTo convert(CreditCard creditCard) {
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setCardNumber(Optional.of(creditCard.getMaskedCardNumber()));
    creditCardTo.setExpiryMonth(Optional.of(creditCard.getExpiryMonth()));
    creditCardTo.setExpiryYear(Optional.of(creditCard.getExpiryYear()));
    creditCardTo.setPostalCode(Optional.of(creditCard.getPostalCode()));
    creditCardTo.setId(creditCard.getId());
    return creditCardTo;
  }
}

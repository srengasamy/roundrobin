package com.roundrobin.vault.service;

import com.roundrobin.vault.api.CreditCardTo;
import com.roundrobin.vault.domain.CreditCard;
import com.roundrobin.vault.domain.UserProfile;
import com.roundrobin.vault.repository.CreditCardRepository;
import com.roundrobin.vault.utils.StringUtils;

import org.jasypt.encryption.StringEncryptor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.roundrobin.core.common.Preconditions.badRequest;
import static com.roundrobin.vault.common.ErrorCodes.INVALID_CREDIT_CARD_ID;

@Service
public class CreditCardService {
  @Autowired
  private CreditCardRepository creditCardRepo;

  @Autowired
  private UserProfileService profileService;

  @Autowired
  private StringEncryptor encryptor;

  private CreditCard get(String userId, String creditCardId) {
    UserProfile profile = profileService.getByUserId(userId);
    Optional<CreditCard> creditCard =
            profile.getCreditCards().stream().filter(c -> c.getActive() && c.getId().equals(creditCardId)).findFirst();
    badRequest(creditCard.isPresent(), INVALID_CREDIT_CARD_ID, "credit_card_id");
    return creditCard.get();
  }

  public CreditCardTo read(String userId, String creditCardId) {
    return convert(get(userId, creditCardId));
  }

  public CreditCardTo create(String userId, CreditCardTo creditCardTo) {
    UserProfile userProfile = profileService.getByUserId(userId);
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
    return convert(creditCard);
  }

  public CreditCardTo update(String userId, CreditCardTo creditCardTo) {
    CreditCard creditCard = get(userId, creditCardTo.getId());
    creditCard.setCvv(creditCardTo.getCvv().orElse(creditCard.getCvv()));
    creditCard.setExpiryMonth(creditCardTo.getExpiryMonth().orElse(creditCard.getExpiryMonth()));
    creditCard.setExpiryYear(creditCardTo.getExpiryYear().orElse(creditCard.getExpiryYear()));
    creditCard.setPostalCode(creditCardTo.getPostalCode().orElse(creditCard.getPostalCode()));
    save(creditCard);
    return convert(creditCard);
  }

  public void delete(String userId, String creditCardId) {
    CreditCard creditCar = get(userId, creditCardId);
    creditCar.setActive(false);
    save(creditCar);
  }

  private CreditCard save(CreditCard creditCard) {
    return creditCardRepo.save(creditCard);
  }

  public List<CreditCardTo> list(String userId) {
    UserProfile profile = profileService.getByUserId(userId);
    return profile.getCreditCards().stream().filter(c -> c.getActive()).map(c -> convert(c))
            .collect(Collectors.toList());
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

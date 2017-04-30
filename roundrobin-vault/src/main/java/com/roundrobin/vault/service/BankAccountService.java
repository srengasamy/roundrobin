package com.roundrobin.vault.service;

import com.roundrobin.core.api.User;
import com.roundrobin.core.service.GenericService;
import com.roundrobin.vault.api.BankAccountTo;
import com.roundrobin.vault.domain.BankAccount;
import com.roundrobin.vault.domain.UserProfile;
import com.roundrobin.vault.repository.BankAccountRepository;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.roundrobin.core.common.Preconditions.badRequest;
import static com.roundrobin.vault.common.ErrorCodes.INVALID_BANK_ACCOUNT_ID;

@Service
public class BankAccountService implements GenericService<BankAccount, BankAccountTo> {

  @Autowired
  private BankAccountRepository bankAccountRepo;

  @Autowired
  private UserProfileService profileService;

  @Override
  public BankAccount get(User user, String id) {
    Optional<BankAccount> bankAccount = bankAccountRepo.findById(id);
    badRequest(bankAccount.isPresent()
                    && bankAccount.get().isActive()
                    && bankAccount.get().getProfile().getId().equals(user.getUserId()),
            INVALID_BANK_ACCOUNT_ID,
            "bank_account_id");
    return bankAccount.get();
  }

  @Override
  public BankAccount save(BankAccount bankAccount) {
    return bankAccountRepo.save(bankAccount);
  }

  @Override
  public BankAccountTo read(User user, String id) {
    return convert(get(user, id));
  }

  @Override
  public BankAccountTo create(User user, BankAccountTo bankAccountTo) {
    UserProfile userProfile = profileService.get(user);
    BankAccount bankAccount = new BankAccount();
    bankAccount.setBankName(bankAccountTo.getBankName());
    bankAccount.setLast4(bankAccountTo.getLast4());
    bankAccount.setProfile(userProfile);
    bankAccount.setActive(true);
    bankAccount.setCreated(DateTime.now());
    return convert(save(bankAccount));
  }

  @Override
  public BankAccountTo update(User user, BankAccountTo bankAccountTo) {
    return null;
  }

  @Override
  public void delete(User user, String id) {
    BankAccount bankAccount = get(user, id);
    bankAccount.setActive(false);
    save(bankAccount);
  }

  @Override
  public List<BankAccountTo> list(User user) {
    UserProfile profile = profileService.get(user);
    return bankAccountRepo.findAllByProfile(profile)
            .stream()
            .filter(c -> c.isActive())
            .map(c -> convert(c))
            .collect(Collectors.toList());
  }

  @Override
  public BankAccountTo convert(BankAccount bankAccount) {
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setId(bankAccount.getId());
    bankAccountTo.setBankName(bankAccount.getBankName());
    bankAccountTo.setLast4(bankAccount.getLast4());
    return bankAccountTo;
  }
}

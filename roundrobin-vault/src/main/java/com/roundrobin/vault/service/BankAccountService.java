package com.roundrobin.vault.service;

import com.roundrobin.core.api.User;
import com.roundrobin.core.service.GenericService;
import com.roundrobin.vault.api.BankAccountTo;
import com.roundrobin.vault.domain.BankAccount;
import com.roundrobin.vault.domain.UserProfile;
import com.roundrobin.vault.repository.BankAccountRepository;
import com.roundrobin.vault.utils.StringUtils;

import org.jasypt.encryption.StringEncryptor;
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

  @Autowired
  private StringEncryptor jasyptStringEncryptor;

  @Override
  public BankAccount get(User user, String bankAccountId) {
    UserProfile profile = profileService.getByUserId(user.getUserId());
    Optional<BankAccount> bankAccount =
            profile.getBankAccounts().stream().filter(c -> c.getActive() && c.getId().equals(bankAccountId)).findFirst();
    badRequest(bankAccount.isPresent(), INVALID_BANK_ACCOUNT_ID, "bank_account_id");
    return bankAccount.get();
  }

  @Override
  public BankAccount save(BankAccount bankAccount) {
    return bankAccountRepo.save(bankAccount);
  }

  @Override
  public BankAccountTo read(User user, String bankAccountId) {
    return convert(get(user, bankAccountId));
  }

  @Override
  public BankAccountTo create(User user, BankAccountTo bankAccountTo) {
    UserProfile userProfile = profileService.getByUserId(user.getUserId());
    BankAccount bankAccount = new BankAccount();
    bankAccount.setAccountNumber(jasyptStringEncryptor.encrypt(bankAccountTo.getAccountNumber().get()));
    bankAccount.setRoutingNumber(jasyptStringEncryptor.encrypt(bankAccountTo.getRoutingNumber().get()));
    bankAccount.setMaskedAccountNumber(StringUtils.mask(bankAccountTo.getAccountNumber().get()));
    bankAccount.setBankName(bankAccountTo.getBankName().get());
    bankAccount.setNameOnAccount(bankAccountTo.getNameOnAccount().get());
    bankAccount.setDescription(bankAccountTo.getDescription().orElse(null));
    bankAccount.setActive(true);
    bankAccount.setCreated(DateTime.now());
    save(bankAccount);
    userProfile.getBankAccounts().add(bankAccount);
    profileService.save(userProfile);
    return convert(bankAccount);
  }

  @Override
  public BankAccountTo update(User user, BankAccountTo bankAccountTo) {
    BankAccount bankAccount = get(user, bankAccountTo.getId());
    bankAccount.setAccountNumber(bankAccountTo.getAccountNumber().orElse(bankAccount.getAccountNumber()));
    bankAccount.setRoutingNumber(bankAccountTo.getRoutingNumber().orElse(bankAccount.getRoutingNumber()));
    bankAccount.setBankName(bankAccountTo.getBankName().orElse(bankAccount.getBankName()));
    bankAccount.setNameOnAccount(bankAccountTo.getNameOnAccount().orElse(bankAccount.getNameOnAccount()));
    bankAccount.setDescription(bankAccountTo.getDescription().orElse(bankAccount.getDescription()));
    save(bankAccount);
    return convert(bankAccount);
  }

  @Override
  public void delete(User user, String bankAccountId) {
    BankAccount bankAccount = get(user, bankAccountId);
    bankAccount.setActive(false);
    save(bankAccount);
  }

  @Override
  public List<BankAccountTo> list(User user) {
    UserProfile profile = profileService.getByUserId(user.getUserId());
    return profile.getBankAccounts().stream().filter(c -> c.getActive()).map(c -> convert(c))
            .collect(Collectors.toList());
  }

  @Override
  public BankAccountTo convert(BankAccount bankAccount) {
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setId(bankAccount.getId());
    bankAccountTo.setNameOnAccount(Optional.ofNullable(bankAccount.getNameOnAccount()));
    bankAccountTo.setBankName(Optional.of(bankAccount.getBankName()));
    bankAccountTo.setAccountNumber(Optional.of(bankAccount.getMaskedAccountNumber()));
    bankAccountTo.setDescription(Optional.ofNullable(bankAccount.getDescription()));
    return bankAccountTo;
  }
}

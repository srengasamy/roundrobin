package com.roundrobin.vault.services;

import static com.roundrobin.common.Assert.isTrue;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jasypt.encryption.StringEncryptor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.vault.api.BankAccountTo;
import com.roundrobin.vault.common.ErrorCode;
import com.roundrobin.vault.domain.BankAccount;
import com.roundrobin.vault.domain.UserProfile;
import com.roundrobin.vault.repository.BankAccountRepository;
import com.roundrobin.vault.utils.StringUtils;

@Service
public class BankAccountService {

  @Autowired
  private BankAccountRepository bankAccountRepo;

  @Autowired
  private UserProfileService profileService;

  @Autowired
  private StringEncryptor jasyptStringEncryptor;

  private BankAccount get(String userId, String bankAccountId) {
    UserProfile profile = profileService.getByUserId(userId);
    Optional<BankAccount> bankAccount =
        profile.getBankAccounts().stream().filter(c -> c.getActive() && c.getId().equals(bankAccountId)).findFirst();
    isTrue(bankAccount.isPresent(), ErrorCode.INVALID_BANK_ACCOUNT_ID);
    return bankAccount.get();
  }

  public BankAccountTo read(String userId, String bankAccountId) {
    return convert(get(userId, bankAccountId));
  }

  public BankAccountTo create(BankAccountTo bankAccountTo) {
    UserProfile userProfile = profileService.getByUserId(bankAccountTo.getUserId().get());
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

  public BankAccountTo update(BankAccountTo bankAccountTo) {
    BankAccount bankAccount = get(bankAccountTo.getUserId().get(), bankAccountTo.getId());
    bankAccount.setAccountNumber(bankAccountTo.getAccountNumber().orElse(bankAccount.getAccountNumber()));
    bankAccount.setRoutingNumber(bankAccountTo.getRoutingNumber().orElse(bankAccount.getRoutingNumber()));
    bankAccount.setBankName(bankAccountTo.getBankName().orElse(bankAccount.getBankName()));
    bankAccount.setNameOnAccount(bankAccountTo.getNameOnAccount().orElse(bankAccount.getNameOnAccount()));
    bankAccount.setDescription(bankAccountTo.getDescription().orElse(bankAccount.getDescription()));
    save(bankAccount);
    return convert(bankAccount);
  }

  public void delete(String userId, String bankAccountId) {
    BankAccount bankAccount = get(userId, bankAccountId);
    bankAccount.setActive(false);
    save(bankAccount);
  }

  private BankAccount save(BankAccount bankAccount) {
    return bankAccountRepo.save(bankAccount);
  }

  public List<BankAccountTo> list(String userId) {
    UserProfile profile = profileService.getByUserId(userId);
    return profile.getBankAccounts().stream().filter(c -> c.getActive()).map(c -> convert(c))
        .collect(Collectors.toList());
  }

  private BankAccountTo convert(BankAccount bankAccount) {
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setId(bankAccount.getId());
    bankAccountTo.setNameOnAccount(Optional.ofNullable(bankAccount.getNameOnAccount()));
    bankAccountTo.setBankName(Optional.of(bankAccount.getBankName()));
    bankAccountTo.setAccountNumber(Optional.of(bankAccount.getMaskedAccountNumber()));
    bankAccountTo.setDescription(Optional.ofNullable(bankAccount.getDescription()));
    return bankAccountTo;
  }
}

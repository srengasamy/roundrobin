package com.roundrobin.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jasypt.encryption.StringEncryptor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.api.BankAccountTo;
import com.roundrobin.api.SkillTo;
import com.roundrobin.common.Assert;
import com.roundrobin.common.ErrorCode;
import com.roundrobin.domain.BankAccount;
import com.roundrobin.domain.Skill;
import com.roundrobin.domain.UserProfile;
import com.roundrobin.repository.BankAccountRepository;
import com.roundrobin.utils.StringUtils;

@Service
public class BankAccountServiceImpl implements BankAccountService {

  @Autowired
  private BankAccountRepository bankAccountRepo;

  @Autowired
  private UserProfileService profileService;

  @Autowired
  private StringEncryptor jasyptStringEncryptor;

  @Override
  public BankAccount get(String id) {
    Optional<BankAccount> bankAccount = bankAccountRepo.findById(id);
    Assert.isTrue(bankAccount.isPresent() && bankAccount.get().getActive(), ErrorCode.INVALID_BANK_ACCOUNT_ID);
    return bankAccount.get();
  }

  @Override
  public BankAccountTo read(String id) {
    return convert(get(id));
  }

  @Override
  public BankAccountTo create(BankAccountTo bankAccountTo) {
    UserProfile userProfile = profileService.get(bankAccountTo.getUserProfileId());
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
    return read(bankAccount.getId());
  }

  @Override
  public BankAccountTo update(BankAccountTo bankAccountTo) {
    BankAccount bankAccount = get(bankAccountTo.getId());
    bankAccount.setAccountNumber(bankAccountTo.getAccountNumber().orElse(bankAccount.getAccountNumber()));
    bankAccount.setRoutingNumber(bankAccountTo.getRoutingNumber().orElse(bankAccount.getRoutingNumber()));
    bankAccount.setBankName(bankAccountTo.getBankName().orElse(bankAccount.getBankName()));
    bankAccount.setNameOnAccount(bankAccountTo.getNameOnAccount().orElse(bankAccount.getNameOnAccount()));
    bankAccount.setDescription(bankAccountTo.getDescription().orElse(bankAccount.getDescription()));
    return read(save(bankAccount).getId());
  }

  @Override
  public void delete(String id) {
    BankAccount bankAccount = get(id);
    bankAccount.setActive(false);
    save(bankAccount);
  }

  @Override
  public BankAccount save(BankAccount bankAccount) {
    return bankAccountRepo.save(bankAccount);
  }

  @Override
  public List<BankAccountTo> list(String id) {
    UserProfile profile = profileService.get(id);
    return profile.getBankAccounts().stream().filter(c -> c.getActive()).map(c -> convert(c)).collect(Collectors
            .toList());
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

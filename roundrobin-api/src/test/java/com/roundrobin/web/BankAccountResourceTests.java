package com.roundrobin.web;

import com.roundrobin.api.BankAccountTo;
import com.roundrobin.api.Error;
import com.roundrobin.api.Response;
import com.roundrobin.api.UserProfileTo;
import com.roundrobin.common.ErrorCode;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Optional;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by rengasu on 5/12/16.
 */

//TODO Dont allow delete when list has only one
//TODO Test getAll
//TODO Test masked values
public class BankAccountResourceTests extends ResourceTests {

  @Test
  public void testCreate() {
    Response<UserProfileTo> profile = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setAccountNumber(Optional.of("1234"));
    bankAccountTo.setRoutingNumber(Optional.of("1234"));
    bankAccountTo.setBankName(Optional.of("BoA"));
    bankAccountTo.setNameOnAccount(Optional.of("Suresh"));
    bankAccountTo.setDescription(Optional.of("Desc"));
    bankAccountTo.setUserProfileId(profile.getEntity().getId());
    Response<BankAccountTo> created = helper.post(url + "bank-account", bankAccountTo, new ParameterizedTypeReference<Response<BankAccountTo>>() {
    }, port).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity().getAccountNumber().get(), is(bankAccountTo.getAccountNumber().get()));
    assertThat(created.getEntity().getRoutingNumber().get(), is(bankAccountTo.getRoutingNumber().get()));
    assertThat(created.getEntity().getDescription().get(), is(bankAccountTo.getDescription().get()));
    assertThat(created.getEntity().getBankName().get(), is(bankAccountTo.getBankName().get()));
    assertThat(created.getEntity().getNameOnAccount().get(), is(bankAccountTo.getNameOnAccount().get()));
  }

  @Test
  public void testCreateWithEmptyValue() {
    BankAccountTo bankAccountTo = new BankAccountTo();
    Response<BankAccountTo> created = helper.post(url + "bank-account", bankAccountTo, new ParameterizedTypeReference<Response<BankAccountTo>>() {
    }, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "userProfileId: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "bankName: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "routingNumber: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "accountNumber: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "nameOnAccount: may not be empty")
    ));
  }

  @Test
  public void testCreateWithInvalidValue() {
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setAccountNumber(Optional.of("1shakjshdkajshdkajshdkjashdkjahsdkjhaskjdhaskjdhak"));
    bankAccountTo.setRoutingNumber(Optional.of("1shakjshdkajshdkajshdkjashdkjahsdkjhaskjdhaskjdhak"));
    bankAccountTo.setBankName(Optional.of("1shakjshdkajshdkajshdkjashdkjdsfjsdhfjsdhfjsdhjfhsdjfhjsdhfjsdhfjshdjfhsdjfhjsdhfjsdhfjsdahsdkjhaskjdhaskjdhak"));
    bankAccountTo.setDescription(Optional.of("1shakjshdkajshdkajshdkjashdkjahsdkjhaskjdhaskfdshfjsdhjfhsdjfhjsdhfjsdhfjsdhfjjdhak"));
    bankAccountTo.setNameOnAccount(Optional.of("1shakjsdfsdjdfjsdjfksdhfksdhfjsdhfjsdhfjshdfjhsdjhfdddjsdhfjsdhfjsdjfsshdkajshdkajshdkjashdkjahsdkjhaskjdhaskjdhakd"));
    Response<BankAccountTo> created = helper.post(url + "bank-account", bankAccountTo, new ParameterizedTypeReference<Response<BankAccountTo>>() {
    }, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "userProfileId: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "bankName: length must be between 0 and 50"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "routingNumber: length must be between 0 and 35"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "accountNumber: length must be between 0 and 35"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "nameOnAccount: length must be between 0 and 100"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "description: length must be between 0 and 50")
    ));
  }

  @Test
  public void testCreateWithInvalidProfileId() {
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setAccountNumber(Optional.of("1234"));
    bankAccountTo.setRoutingNumber(Optional.of("1234"));
    bankAccountTo.setBankName(Optional.of("BoA"));
    bankAccountTo.setNameOnAccount(Optional.of("Suresh"));
    bankAccountTo.setDescription(Optional.of("Desc"));
    bankAccountTo.setUserProfileId("testing");
    Response<String> created = helper.post(url + "bank-account", bankAccountTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_PROFILE_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_PROFILE_ID))));
  }

  @Test
  public void testUpdate() {
    Response<UserProfileTo> profile = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setAccountNumber(Optional.of("1234"));
    bankAccountTo.setRoutingNumber(Optional.of("1234"));
    bankAccountTo.setBankName(Optional.of("BoA"));
    bankAccountTo.setNameOnAccount(Optional.of("Suresh"));
    bankAccountTo.setDescription(Optional.of("Desc"));
    bankAccountTo.setUserProfileId(profile.getEntity().getId());
    Response<BankAccountTo> created = helper.post(url + "bank-account", bankAccountTo, new ParameterizedTypeReference<Response<BankAccountTo>>() {
    }, port).getBody();
    assertThat(created.getEntity(), notNullValue());
    created.getEntity().setNameOnAccount(Optional.of("testing"));
    created.getEntity().setDescription(Optional.of("testing"));
    created.getEntity().setRoutingNumber(Optional.of("testing"));
    created.getEntity().setAccountNumber(Optional.of("testing"));
    created.getEntity().setBankName(Optional.of("testing"));
    Response<BankAccountTo> updated = helper.put(url + "bank-account", created.getEntity(), new ParameterizedTypeReference<Response<BankAccountTo>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), notNullValue());
    Response<BankAccountTo> read = helper.get(url + "bank-account/{bankAccountId}", new ParameterizedTypeReference<Response<BankAccountTo>>() {
    }, port, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getAccountNumber().get(), is(created.getEntity().getAccountNumber().get()));
    assertThat(read.getEntity().getRoutingNumber().get(), is(created.getEntity().getRoutingNumber().get()));
    assertThat(read.getEntity().getDescription().get(), is(created.getEntity().getDescription().get()));
    assertThat(read.getEntity().getBankName().get(), is(created.getEntity().getBankName().get()));
    assertThat(read.getEntity().getNameOnAccount().get(), is(created.getEntity().getNameOnAccount().get()));
  }

  @Test
  public void testUpdateWithEmptyValues() {
    BankAccountTo bankAccountTo = new BankAccountTo();
    Response<String> updated = helper.put(url + "bank-account", bankAccountTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "id: may not be empty")));
  }

  @Test
  public void testUpdateWithInvalidValue() {
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setAccountNumber(Optional.of("1shakjshdkajshdkajshdkjashdkjahsdkjhaskjdhaskjdhak"));
    bankAccountTo.setRoutingNumber(Optional.of("1shakjshdkajshdkajshdkjashdkjahsdkjhaskjdhaskjdhak"));
    bankAccountTo.setBankName(Optional.of("1shakjshdkajshdkajshdkjashdkjdsfjsdhfjsdhfjsdhjfhsdjfhjsdhfjsdhfjshdjfhsdjfhjsdhfjsdhfjsdahsdkjhaskjdhaskjdhak"));
    bankAccountTo.setDescription(Optional.of("1shakjshdkajshdkajshdkjashdkjahsdkjhaskjdhaskfdshfjsdhjfhsdjfhjsdhfjsdhfjsdhfjjdhak"));
    bankAccountTo.setNameOnAccount(Optional.of("1shakjsdfsdjdfjsdjfksdhfksdhfjsdhfjsdhfjshdfjhsdjhfdddjsdhfjsdhfjsdjfsshdkajshdkajshdkjashdkjahsdkjhaskjdhaskjdhakd"));
    Response<String> updated = helper.put(url + "bank-account", bankAccountTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "bankName: length must be between 0 and 50"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "routingNumber: length must be between 0 and 35"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "accountNumber: length must be between 0 and 35"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "nameOnAccount: length must be between 0 and 100"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "description: length must be between 0 and 50")
    ));
  }

  @Test
  public void testUpdateWithInvalidBankId() {
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setId("testing");
    Response<String> updated = helper.put(url + "bank-account", bankAccountTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_BANK_ACCOUNT_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_BANK_ACCOUNT_ID))));
  }

  @Test
  public void testRead() {
    Response<UserProfileTo> profile = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setAccountNumber(Optional.of("1234"));
    bankAccountTo.setRoutingNumber(Optional.of("1234"));
    bankAccountTo.setBankName(Optional.of("BoA"));
    bankAccountTo.setNameOnAccount(Optional.of("Suresh"));
    bankAccountTo.setDescription(Optional.of("Desc"));
    bankAccountTo.setUserProfileId(profile.getEntity().getId());
    Response<BankAccountTo> created = helper.post(url + "bank-account", bankAccountTo, new ParameterizedTypeReference<Response<BankAccountTo>>() {
    }, port).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<BankAccountTo> read = helper.get(url + "bank-account/{bankAccountId}", new ParameterizedTypeReference<Response<BankAccountTo>>() {
    }, port, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getAccountNumber().get(), is(created.getEntity().getAccountNumber().get()));
    assertThat(read.getEntity().getRoutingNumber().get(), is(created.getEntity().getRoutingNumber().get()));
    assertThat(read.getEntity().getDescription().get(), is(created.getEntity().getDescription().get()));
    assertThat(read.getEntity().getBankName().get(), is(created.getEntity().getBankName().get()));
    assertThat(read.getEntity().getNameOnAccount().get(), is(created.getEntity().getNameOnAccount().get()));
  }

  @Test
  public void testReadWithEmptyId() {
    Response<String> read = helper.get(url + "bank-account/{bankAccountId}", new ParameterizedTypeReference<Response<String>>() {
    }, port, " ").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_URL.getCode(), messages.getErrorMessage(ErrorCode.INVALID_URL))));
  }

  @Test
  public void testReadWithInvalidId() {
    Response<String> read = helper.get(url + "bank-account/{bankAccountId}", new ParameterizedTypeReference<Response<String>>() {
    }, port, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_BANK_ACCOUNT_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_BANK_ACCOUNT_ID))));
  }

  @Test
  public void testDelete() {
    Response<UserProfileTo> profile = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setAccountNumber(Optional.of("1234"));
    bankAccountTo.setRoutingNumber(Optional.of("1234"));
    bankAccountTo.setBankName(Optional.of("BoA"));
    bankAccountTo.setNameOnAccount(Optional.of("Suresh"));
    bankAccountTo.setDescription(Optional.of("Desc"));
    bankAccountTo.setUserProfileId(profile.getEntity().getId());
    Response<BankAccountTo> created = helper.post(url + "bank-account", bankAccountTo, new ParameterizedTypeReference<Response<BankAccountTo>>() {
    }, port).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<Boolean> deleted = helper.delete(url + "bank-account/{bankAccountId}", new ParameterizedTypeReference<Response<Boolean>>() {
    }, port, created.getEntity().getId()).getBody();
    assertThat(deleted.getEntity(), notNullValue());
    assertThat(deleted.getEntity(), is(true));
    Response<Boolean> read = helper.get(url + "bank-account/{bankAccountId}", new ParameterizedTypeReference<Response<Boolean>>() {
    }, port, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_BANK_ACCOUNT_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_BANK_ACCOUNT_ID))));

  }

  @Test
  public void testDeleteWithEmptyId() {
    Response<String> deleted = helper.delete(url + "bank-account/{bankAccountId}", new ParameterizedTypeReference<Response<String>>() {
    }, port, " ").getBody();
    assertThat(deleted.getEntity(), nullValue());
    assertThat(deleted.getErrors(), notNullValue());
    assertThat(deleted.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_URL.getCode(), messages.getErrorMessage(ErrorCode.INVALID_URL))));
  }

  @Test
  public void testDeleteWithInvalidId() {
    Response<String> deleted = helper.delete(url + "bank-account/{bankAccountId}", new ParameterizedTypeReference<Response<String>>() {
    }, port, "testing").getBody();
    assertThat(deleted.getEntity(), nullValue());
    assertThat(deleted.getErrors(), notNullValue());
    assertThat(deleted.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_BANK_ACCOUNT_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_BANK_ACCOUNT_ID))));
  }
}

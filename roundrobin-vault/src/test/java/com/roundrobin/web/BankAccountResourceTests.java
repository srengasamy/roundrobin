package com.roundrobin.web;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import com.roundrobin.api.Error;
import com.roundrobin.api.Response;
import com.roundrobin.vault.api.BankAccountTo;
import com.roundrobin.vault.common.ErrorCode;

/**
 * Created by rengasu on 5/12/16.
 */

// TODO Dont allow delete when list has only one
// TODO Test getAll
public class BankAccountResourceTests extends ResourceTests {

  @Test
  public void testCreate() {
    String username = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setAccountNumber(Optional.of("234234321234"));
    bankAccountTo.setRoutingNumber(Optional.of("2324321234"));
    bankAccountTo.setBankName(Optional.of("BoA"));
    bankAccountTo.setNameOnAccount(Optional.of("Suresh"));
    bankAccountTo.setDescription(Optional.of("Desc"));
    Response<BankAccountTo> created =
        helper.post(vaultUrl + "bank-account", createBearerHeaders(getAccessToken(username)), bankAccountTo,
            new ParameterizedTypeReference<Response<BankAccountTo>>() {}).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity().getAccountNumber().get(), is("********1234"));
    assertThat(created.getEntity().getDescription().get(), is(bankAccountTo.getDescription().get()));
    assertThat(created.getEntity().getBankName().get(), is(bankAccountTo.getBankName().get()));
    assertThat(created.getEntity().getNameOnAccount().get(), is(bankAccountTo.getNameOnAccount().get()));
  }

  @Test
  public void testCreateWithEmptyValue() {
    String username = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    Response<BankAccountTo> created =
        helper.post(vaultUrl + "bank-account", createBearerHeaders(getAccessToken(username)), bankAccountTo,
            new ParameterizedTypeReference<Response<BankAccountTo>>() {}).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "bankName: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD, "routingNumber: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD, "accountNumber: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD, "nameOnAccount: may not be empty")));
  }

  @Test
  public void testCreateWithInvalidValue() {
    String username = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setAccountNumber(Optional.of("1shakjshdkajshdkajshdkjashdkjahsdkjhaskjdhaskjdhak"));
    bankAccountTo.setRoutingNumber(Optional.of("1shakjshdkajshdkajshdkjashdkjahsdkjhaskjdhaskjdhak"));
    bankAccountTo.setBankName(Optional.of(
        "1shakjshdkajshdkajshdkjashdkjdsfjsdhfjsdhfjsdhjfhsdjfhjsdhfjsdhfjshdjfhsdjfhjsdhfjsdhfjsdahsdkjhaskjdhaskjdhak"));
    bankAccountTo.setDescription(
        Optional.of("1shakjshdkajshdkajshdkjashdkjahsdkjhaskjdhaskfdshfjsdhjfhsdjfhjsdhfjsdhfjsdhfjjdhak"));
    bankAccountTo.setNameOnAccount(Optional.of(
        "1shakjsdfsdjdfjsdjfksdhfksdhfjsdhfjsdhfjshdfjhsdjhfdddjsdhfjsdhfjsdjfsshdkajshdkajshdkjashdkjahsdkjhaskjdhaskjdhakd"));
    Response<BankAccountTo> created =
        helper.post(vaultUrl + "bank-account", createBearerHeaders(getAccessToken(username)), bankAccountTo,
            new ParameterizedTypeReference<Response<BankAccountTo>>() {}).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "bankName: length must be between 0 and 50"),
            new Error(ErrorCode.INVALID_FIELD, "routingNumber: length must be between 0 and 35"),
            new Error(ErrorCode.INVALID_FIELD, "accountNumber: length must be between 0 and 35"),
            new Error(ErrorCode.INVALID_FIELD, "nameOnAccount: length must be between 0 and 100"),
            new Error(ErrorCode.INVALID_FIELD, "description: length must be between 0 and 50")));
  }

  @Test
  public void testUpdate() {
    String username = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setAccountNumber(Optional.of("1231231231234"));
    bankAccountTo.setRoutingNumber(Optional.of("1234"));
    bankAccountTo.setBankName(Optional.of("BoA"));
    bankAccountTo.setNameOnAccount(Optional.of("Suresh"));
    bankAccountTo.setDescription(Optional.of("Desc"));
    Response<BankAccountTo> created =
        helper.post(vaultUrl + "bank-account", createBearerHeaders(getAccessToken(username)), bankAccountTo,
            new ParameterizedTypeReference<Response<BankAccountTo>>() {}).getBody();
    assertThat(created.getEntity(), notNullValue());
    created.getEntity().setNameOnAccount(Optional.of("testing"));
    created.getEntity().setDescription(Optional.of("testing"));
    created.getEntity().setRoutingNumber(Optional.of("testing"));
    created.getEntity().setAccountNumber(Optional.of("testing"));
    created.getEntity().setBankName(Optional.of("testing"));
    Response<BankAccountTo> updated =
        helper.put(vaultUrl + "bank-account", createBearerHeaders(getAccessToken(username)), created.getEntity(),
            new ParameterizedTypeReference<Response<BankAccountTo>>() {}).getBody();
    assertThat(updated.getEntity(), notNullValue());
    Response<BankAccountTo> read =
        helper.get(vaultUrl + "bank-account/{bankAccountId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<BankAccountTo>>() {}, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getDescription().get(), is(created.getEntity().getDescription().get()));
    assertThat(read.getEntity().getBankName().get(), is(created.getEntity().getBankName().get()));
    assertThat(read.getEntity().getNameOnAccount().get(), is(created.getEntity().getNameOnAccount().get()));
  }

  @Test
  public void testUpdateWithEmptyValues() {
    BankAccountTo bankAccountTo = new BankAccountTo();
    Response<String> updated = helper.put(vaultUrl + "bank-account", createBearerHeaders(), bankAccountTo,
        new ParameterizedTypeReference<Response<String>>() {}).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "id: may not be empty"),
        new Error(ErrorCode.INVALID_FIELD, "routingNumber: may not be empty")));
  }

  @Test
  public void testUpdateWithInvalidValue() {
    String username = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setAccountNumber(Optional.of("1shakjshdkajshdkajshdkjashdkjahsdkjhaskjdhaskjdhak"));
    bankAccountTo.setRoutingNumber(Optional.of("1shakjshdkajshdkajshdkjashdkjahsdkjhaskjdhaskjdhak"));
    bankAccountTo.setBankName(Optional.of(
        "1shakjshdkajshdkajshdkjashdkjdsfjsdhfjsdhfjsdhjfhsdjfhjsdhfjsdhfjshdjfhsdjfhjsdhfjsdhfjsdahsdkjhaskjdhaskjdhak"));
    bankAccountTo.setDescription(
        Optional.of("1shakjshdkajshdkajshdkjashdkjahsdkjhaskjdhaskfdshfjsdhjfhsdjfhjsdhfjsdhfjsdhfjjdhak"));
    bankAccountTo.setNameOnAccount(Optional.of(
        "1shakjsdfsdjdfjsdjfksdhfksdhfjsdhfjsdhfjshdfjhsdjhfdddjsdhfjsdhfjsdjfsshdkajshdkajshdkjashdkjahsdkjhaskjdhaskjdhakd"));
    Response<String> updated = helper.put(vaultUrl + "bank-account", createBearerHeaders(getAccessToken(username)),
        bankAccountTo, new ParameterizedTypeReference<Response<String>>() {}).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "bankName: length must be between 0 and 50"),
            new Error(ErrorCode.INVALID_FIELD, "routingNumber: length must be between 0 and 35"),
            new Error(ErrorCode.INVALID_FIELD, "accountNumber: length must be between 0 and 35"),
            new Error(ErrorCode.INVALID_FIELD, "nameOnAccount: length must be between 0 and 100"),
            new Error(ErrorCode.INVALID_FIELD, "description: length must be between 0 and 50")));
  }

  @Test
  public void testUpdateWithInvalidBankId() {
    String username = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setId("testing");
    bankAccountTo.setRoutingNumber(Optional.of("testing"));
    Response<String> updated = helper.put(vaultUrl + "bank-account", createBearerHeaders(getAccessToken(username)),
        bankAccountTo, new ParameterizedTypeReference<Response<String>>() {}).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(
        new Error(ErrorCode.INVALID_BANK_ACCOUNT_ID, messages.getErrorMessage(ErrorCode.INVALID_BANK_ACCOUNT_ID))));
  }

  @Test
  public void testRead() {
    String username = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setAccountNumber(Optional.of("1234"));
    bankAccountTo.setRoutingNumber(Optional.of("1234"));
    bankAccountTo.setBankName(Optional.of("BoA"));
    bankAccountTo.setNameOnAccount(Optional.of("Suresh"));
    bankAccountTo.setDescription(Optional.of("Desc"));
    Response<BankAccountTo> created =
        helper.post(vaultUrl + "bank-account", createBearerHeaders(getAccessToken(username)), bankAccountTo,
            new ParameterizedTypeReference<Response<BankAccountTo>>() {}).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<BankAccountTo> read =
        helper.get(vaultUrl + "bank-account/{bankAccountId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<BankAccountTo>>() {}, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getAccountNumber().get(), is(created.getEntity().getAccountNumber().get()));
    assertThat(read.getEntity().getDescription().get(), is(created.getEntity().getDescription().get()));
    assertThat(read.getEntity().getBankName().get(), is(created.getEntity().getBankName().get()));
    assertThat(read.getEntity().getNameOnAccount().get(), is(created.getEntity().getNameOnAccount().get()));
  }

  @Test
  public void testReadWithEmptyId() {
    String username = createUserProfile();
    Response<List<BankAccountTo>> read =
        helper.get(vaultUrl + "bank-account/{bankAccountId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<List<BankAccountTo>>>() {}, " ").getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().size(), is(0));
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors().size(), is(0));
  }

  @Test
  public void testReadWithInvalidId() {
    String username = createUserProfile();
    Response<String> read =
        helper.get(vaultUrl + "bank-account/{bankAccountId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<String>>() {}, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(
        new Error(ErrorCode.INVALID_BANK_ACCOUNT_ID, messages.getErrorMessage(ErrorCode.INVALID_BANK_ACCOUNT_ID))));
  }

  @Test
  public void testDelete() {
    String username = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setAccountNumber(Optional.of("1234"));
    bankAccountTo.setRoutingNumber(Optional.of("1234"));
    bankAccountTo.setBankName(Optional.of("BoA"));
    bankAccountTo.setNameOnAccount(Optional.of("Suresh"));
    bankAccountTo.setDescription(Optional.of("Desc"));
    Response<BankAccountTo> created =
        helper.post(vaultUrl + "bank-account", createBearerHeaders(getAccessToken(username)), bankAccountTo,
            new ParameterizedTypeReference<Response<BankAccountTo>>() {}).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<Boolean> deleted =
        helper.delete(vaultUrl + "bank-account/{bankAccountId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<Boolean>>() {}, created.getEntity().getId()).getBody();
    assertThat(deleted.getEntity(), notNullValue());
    assertThat(deleted.getEntity(), is(true));
    Response<Boolean> read =
        helper.get(vaultUrl + "bank-account/{bankAccountId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<Boolean>>() {}, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(
        new Error(ErrorCode.INVALID_BANK_ACCOUNT_ID, messages.getErrorMessage(ErrorCode.INVALID_BANK_ACCOUNT_ID))));

  }

  @Test
  public void testDeleteWithEmptyId() {
    String username = createUserProfile();
    Response<String> deleted =
        helper.delete(vaultUrl + "bank-account/{bankAccountId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<String>>() {}, " ").getBody();
    assertThat(deleted.getEntity(), nullValue());
    assertThat(deleted.getErrors(), notNullValue());
    assertThat(deleted.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_URL, messages.getErrorMessage(ErrorCode.INVALID_URL))));
  }

  @Test
  public void testDeleteWithInvalidId() {
    String username = createUserProfile();
    Response<String> deleted =
        helper.delete(vaultUrl + "bank-account/{bankAccountId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<String>>() {}, "testing").getBody();
    assertThat(deleted.getEntity(), nullValue());
    assertThat(deleted.getErrors(), notNullValue());
    assertThat(deleted.getErrors(), hasItems(
        new Error(ErrorCode.INVALID_BANK_ACCOUNT_ID, messages.getErrorMessage(ErrorCode.INVALID_BANK_ACCOUNT_ID))));
  }

  @Test
  public void testList() {
    String username = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setAccountNumber(Optional.of("234234321234"));
    bankAccountTo.setRoutingNumber(Optional.of("2324321234"));
    bankAccountTo.setBankName(Optional.of("BoA"));
    bankAccountTo.setNameOnAccount(Optional.of("Suresh"));
    bankAccountTo.setDescription(Optional.of("Desc"));
    Response<BankAccountTo> created =
        helper.post(vaultUrl + "bank-account", createBearerHeaders(getAccessToken(username)), bankAccountTo,
            new ParameterizedTypeReference<Response<BankAccountTo>>() {}).getBody();
    assertThat(created.getEntity(), notNullValue());

    Response<List<BankAccountTo>> list =
        helper.get(vaultUrl + "bank-account", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<List<BankAccountTo>>>() {}).getBody();
    assertThat(list.getEntity(), notNullValue());
    assertThat(list.getEntity().size(), is(1));
  }

}

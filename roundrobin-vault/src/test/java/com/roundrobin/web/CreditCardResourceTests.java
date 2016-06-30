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
import com.roundrobin.vault.api.CreditCardTo;
import com.roundrobin.vault.common.ErrorCode;

/**
 * Created by rengasu on 5/13/16.
 */
public class CreditCardResourceTests extends ResourceTests {

  @Test
  public void testCreate() {
    String username = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setCardNumber(Optional.of("4015260001266035"));
    creditCardTo.setCvv(Optional.of("124"));
    creditCardTo.setExpiryMonth(Optional.of((byte) 6));
    creditCardTo.setExpiryYear(Optional.of((short) 17));
    creditCardTo.setPostalCode(Optional.of("87878"));
    Response<CreditCardTo> created =
        helper.post(vaultUrl + "credit-card", createBearerHeaders(getAccessToken(username)), creditCardTo,
            new ParameterizedTypeReference<Response<CreditCardTo>>() {}).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<CreditCardTo> read =
        helper.get(vaultUrl + "credit-card/{creditCardId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<CreditCardTo>>() {}, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getCardNumber().get(), is("************6035"));
    assertThat(read.getEntity().getExpiryMonth().get(), is(creditCardTo.getExpiryMonth().get()));
    assertThat(read.getEntity().getExpiryYear().get(), is(creditCardTo.getExpiryYear().get()));
    assertThat(read.getEntity().getCvv().isPresent(), is(false));
    assertThat(read.getEntity().getPostalCode().get(), is(creditCardTo.getPostalCode().get()));
  }

  @Test
  public void testCreateWithEmptyValues() {
    CreditCardTo creditCardTo = new CreditCardTo();
    Response<String> created = helper.post(vaultUrl + "credit-card", createBearerHeaders(), creditCardTo,
        new ParameterizedTypeReference<Response<String>>() {}).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "cvv: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD, "expiryMonth: may not be null"),
            new Error(ErrorCode.INVALID_FIELD, "postalCode: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD, "expiryYear: may not be null"),
            new Error(ErrorCode.INVALID_FIELD, "cardNumber: may not be empty")));
  }

  @Test
  public void testCreateWithInvalidValues() {
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setExpiryYear(Optional.of((short) 11));
    creditCardTo.setExpiryMonth(Optional.of((byte) 15));
    creditCardTo.setPostalCode(Optional.of("1213213123"));
    creditCardTo.setCvv(Optional.of("dfskfjsdkfjs"));
    creditCardTo.setCardNumber(Optional.of("abcd"));
    Response<String> created = helper.post(vaultUrl + "credit-card", createBearerHeaders(), creditCardTo,
        new ParameterizedTypeReference<Response<String>>() {}).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "cvv: length must be between 0 and 5"),
            new Error(ErrorCode.INVALID_FIELD, "expiryMonth: must be between 1 and 12"),
            new Error(ErrorCode.INVALID_FIELD, "postalCode: length must be between 0 and 7"),
            new Error(ErrorCode.INVALID_FIELD, "expiryYear: must be between 16 and 99"),
            new Error(ErrorCode.INVALID_FIELD, "cardNumber: invalid credit card number")));
  }

  @Test
  public void testUpdate() {
    String username = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setCardNumber(Optional.of("4015260001266035"));
    creditCardTo.setCvv(Optional.of("124"));
    creditCardTo.setExpiryMonth(Optional.of((byte) 6));
    creditCardTo.setExpiryYear(Optional.of((short) 17));
    creditCardTo.setPostalCode(Optional.of("87878"));
    Response<CreditCardTo> created =
        helper.post(vaultUrl + "credit-card", createBearerHeaders(getAccessToken(username)), creditCardTo,
            new ParameterizedTypeReference<Response<CreditCardTo>>() {}).getBody();
    assertThat(created.getEntity(), notNullValue());
    created.getEntity().setCvv(Optional.of("test"));
    created.getEntity().setPostalCode(Optional.of("test"));
    created.getEntity().setExpiryMonth(Optional.of((byte) 12));
    created.getEntity().setExpiryYear(Optional.of((short) 99));
    Response<CreditCardTo> updated = helper.put(vaultUrl + "credit-card", createBearerHeaders(getAccessToken(username)),
        created.getEntity(), new ParameterizedTypeReference<Response<CreditCardTo>>() {}).getBody();
    assertThat(updated.getEntity(), notNullValue());
    Response<CreditCardTo> read =
        helper.get(vaultUrl + "credit-card/{creditCardId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<CreditCardTo>>() {}, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getExpiryMonth().get(), is(created.getEntity().getExpiryMonth().get()));
    assertThat(read.getEntity().getExpiryYear().get(), is(created.getEntity().getExpiryYear().get()));
    assertThat(read.getEntity().getPostalCode().get(), is(created.getEntity().getPostalCode().get()));
  }

  @Test
  public void testUpdateWithEmptyValues() {
    CreditCardTo creditCardTo = new CreditCardTo();
    Response<CreditCardTo> updated = helper.put(vaultUrl + "credit-card", createBearerHeaders(), creditCardTo,
        new ParameterizedTypeReference<Response<CreditCardTo>>() {}).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "cvv: may not be empty")));
  }

  @Test
  public void testUpdateWithInvalidValues() {
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setExpiryYear(Optional.of((short) 11));
    creditCardTo.setExpiryMonth(Optional.of((byte) 15));
    creditCardTo.setPostalCode(Optional.of("1213213123"));
    creditCardTo.setCvv(Optional.of("dfskfjsdkfjs"));
    creditCardTo.setCardNumber(Optional.of("abcd"));
    Response<CreditCardTo> updated = helper.put(vaultUrl + "credit-card", createBearerHeaders(), creditCardTo,
        new ParameterizedTypeReference<Response<CreditCardTo>>() {}).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "cvv: length must be between 0 and 5"),
            new Error(ErrorCode.INVALID_FIELD, "expiryMonth: must be between 1 and 12"),
            new Error(ErrorCode.INVALID_FIELD, "postalCode: length must be between 0 and 7"),
            new Error(ErrorCode.INVALID_FIELD, "expiryYear: must be between 16 and 99")));
  }

  @Test
  public void testUpdateWithInvalidId() {
    String username = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setId("testing");
    creditCardTo.setCvv(Optional.of("123"));
    Response<CreditCardTo> updated = helper.put(vaultUrl + "credit-card", createBearerHeaders(getAccessToken(username)),
        creditCardTo, new ParameterizedTypeReference<Response<CreditCardTo>>() {}).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(
        new Error(ErrorCode.INVALID_CREDIT_CARD_ID, messages.getErrorMessage(ErrorCode.INVALID_CREDIT_CARD_ID))));
  }

  @Test
  public void testRead() {
    String username = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setCardNumber(Optional.of("4015260001266035"));
    creditCardTo.setCvv(Optional.of("124"));
    creditCardTo.setExpiryMonth(Optional.of((byte) 6));
    creditCardTo.setExpiryYear(Optional.of((short) 17));
    creditCardTo.setPostalCode(Optional.of("87878"));
    Response<CreditCardTo> created =
        helper.post(vaultUrl + "credit-card", createBearerHeaders(getAccessToken(username)), creditCardTo,
            new ParameterizedTypeReference<Response<CreditCardTo>>() {}).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<CreditCardTo> read =
        helper.get(vaultUrl + "credit-card/{creditCardId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<CreditCardTo>>() {}, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getCardNumber().get(), is("************6035"));
    assertThat(read.getEntity().getExpiryMonth().get(), is(creditCardTo.getExpiryMonth().get()));
    assertThat(read.getEntity().getExpiryYear().get(), is(creditCardTo.getExpiryYear().get()));
    assertThat(read.getEntity().getPostalCode().get(), is(creditCardTo.getPostalCode().get()));
  }

  @Test
  public void testReadWithEmptyId() {
    String username = createUserProfile();
    Response<List<CreditCardTo>> read =
        helper.get(vaultUrl + "credit-card/{creditCardId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<List<CreditCardTo>>>() {}, " ").getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getEntity().size(), is(0));
    assertThat(read.getErrors().size(), is(0));
  }

  @Test
  public void testReadWithInvalidId() {
    String username = createUserProfile();
    Response<CreditCardTo> read =
        helper.get(vaultUrl + "credit-card/{creditCardId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<CreditCardTo>>() {}, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(
        new Error(ErrorCode.INVALID_CREDIT_CARD_ID, messages.getErrorMessage(ErrorCode.INVALID_CREDIT_CARD_ID))));
  }

  @Test
  public void testDelete() {
    String username = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setCardNumber(Optional.of("4015260001266035"));
    creditCardTo.setCvv(Optional.of("124"));
    creditCardTo.setExpiryMonth(Optional.of((byte) 6));
    creditCardTo.setExpiryYear(Optional.of((short) 17));
    creditCardTo.setPostalCode(Optional.of("87878"));
    Response<CreditCardTo> created =
        helper.post(vaultUrl + "credit-card", createBearerHeaders(getAccessToken(username)), creditCardTo,
            new ParameterizedTypeReference<Response<CreditCardTo>>() {}).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<Boolean> deleted =
        helper.delete(vaultUrl + "credit-card/{creditCardId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<Boolean>>() {}, created.getEntity().getId()).getBody();
    assertThat(deleted.getEntity(), notNullValue());
    assertThat(deleted.getEntity(), is(true));
    Response<Boolean> read =
        helper.get(vaultUrl + "credit-card/{creditCardId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<Boolean>>() {}, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(
        new Error(ErrorCode.INVALID_CREDIT_CARD_ID, messages.getErrorMessage(ErrorCode.INVALID_CREDIT_CARD_ID))));
  }

  @Test
  public void testDeleteWithEmptyId() {
    Response<CreditCardTo> deleted = helper.delete(vaultUrl + "credit-card/{creditCardId}", createBearerHeaders(),
        new ParameterizedTypeReference<Response<CreditCardTo>>() {}, " ").getBody();
    assertThat(deleted.getEntity(), nullValue());
    assertThat(deleted.getErrors(), notNullValue());
    assertThat(deleted.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_URL, messages.getErrorMessage(ErrorCode.INVALID_URL))));
  }

  @Test
  public void testDeleteWithInvalidId() {
    String username = createUserProfile();
    Response<CreditCardTo> deleted =
        helper.delete(vaultUrl + "credit-card/{creditCardId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<CreditCardTo>>() {}, "testing").getBody();
    assertThat(deleted.getEntity(), nullValue());
    assertThat(deleted.getErrors(), notNullValue());
    assertThat(deleted.getErrors(), hasItems(
        new Error(ErrorCode.INVALID_CREDIT_CARD_ID, messages.getErrorMessage(ErrorCode.INVALID_CREDIT_CARD_ID))));
  }

  @Test
  public void testList() {
    String username = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setCardNumber(Optional.of("4015260001266035"));
    creditCardTo.setCvv(Optional.of("124"));
    creditCardTo.setExpiryMonth(Optional.of((byte) 6));
    creditCardTo.setExpiryYear(Optional.of((short) 17));
    creditCardTo.setPostalCode(Optional.of("87878"));
    Response<CreditCardTo> created =
        helper.post(vaultUrl + "credit-card", createBearerHeaders(getAccessToken(username)), creditCardTo,
            new ParameterizedTypeReference<Response<CreditCardTo>>() {}).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<List<CreditCardTo>> list =
        helper.get(vaultUrl + "credit-card", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<List<CreditCardTo>>>() {}).getBody();
    assertThat(list.getEntity(), notNullValue());
    assertThat(list.getEntity().size(), is(1));
  }

  @Test
  public void testListWithEmptyId() {
    String username = createUserProfile();
    Response<List<CreditCardTo>> list =
        helper.get(vaultUrl + "credit-card", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<List<CreditCardTo>>>() {}).getBody();
    assertThat(list.getEntity(), notNullValue());
    assertThat(list.getErrors(), notNullValue());
    assertThat(list.getEntity().size(), is(0));
    assertThat(list.getErrors().size(), is(0));
  }

}

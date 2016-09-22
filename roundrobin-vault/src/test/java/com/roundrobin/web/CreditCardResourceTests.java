package com.roundrobin.web;

import static com.roundrobin.error.ErrorCode.INVALID_FIELD;
import static com.roundrobin.error.ErrorType.INVALID_REQUEST_ERROR;
import static com.roundrobin.vault.error.VaultErrorCode.INVALID_CREDIT_CARD_ID;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import com.roundrobin.api.Response;
import com.roundrobin.vault.api.CreditCardTo;

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
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("cvv: may not be empty", "expiryMonth: may not be null",
        "postalCode: may not be empty", "expiryYear: may not be null", "cardNumber: may not be empty"));
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
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(),
        hasItems("cvv: length must be between 0 and 5", "expiryMonth: must be between 1 and 12",
            "postalCode: length must be between 0 and 7", "expiryYear: must be between 16 and 99",
            "cardNumber: invalid credit card number"));
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
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_FIELD));
    assertThat(updated.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(updated.getError().getFieldErrors(), hasItems("cvv: may not be empty"));
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
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_FIELD));
    assertThat(updated.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(updated.getError().getFieldErrors(),
        hasItems("cvv: length must be between 0 and 5", "expiryMonth: must be between 1 and 12",
            "postalCode: length must be between 0 and 7", "expiryYear: must be between 16 and 99"));
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
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_CREDIT_CARD_ID));
    assertThat(updated.getError().getMessage(), is("Invalid credit card id"));
    assertThat(updated.getError().getParam(), is("credit_card_id"));
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
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_CREDIT_CARD_ID));
    assertThat(read.getError().getMessage(), is("Invalid credit card id"));
    assertThat(read.getError().getParam(), is("credit_card_id"));
  }

  @Test
  public void testReadWithInvalidId() {
    String username = createUserProfile();
    Response<CreditCardTo> read =
        helper.get(vaultUrl + "credit-card/{creditCardId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<CreditCardTo>>() {}, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_CREDIT_CARD_ID));
    assertThat(read.getError().getMessage(), is("Invalid credit card id"));
    assertThat(read.getError().getParam(), is("credit_card_id"));
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
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_CREDIT_CARD_ID));
    assertThat(read.getError().getMessage(), is("Invalid credit card id"));
    assertThat(read.getError().getParam(), is("credit_card_id"));
  }

  @Test
  public void testDeleteWithEmptyId() {
    Response<CreditCardTo> deleted =
        helper.delete(vaultUrl + "credit-card/{creditCardId}", createBearerHeaders(getAccessToken(createUserProfile())),
            new ParameterizedTypeReference<Response<CreditCardTo>>() {}, " ").getBody();
    assertThat(deleted.getEntity(), nullValue());
    assertThat(deleted.getError(), notNullValue());
    assertThat(deleted.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(deleted.getError().getCode(), is(INVALID_CREDIT_CARD_ID));
    assertThat(deleted.getError().getMessage(), is("Invalid credit card id"));
    assertThat(deleted.getError().getParam(), is("credit_card_id"));
  }

  @Test
  public void testDeleteWithInvalidId() {
    String username = createUserProfile();
    Response<CreditCardTo> deleted =
        helper.delete(vaultUrl + "credit-card/{creditCardId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<CreditCardTo>>() {}, "testing").getBody();
    assertThat(deleted.getEntity(), nullValue());
    assertThat(deleted.getError(), notNullValue());
    assertThat(deleted.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(deleted.getError().getCode(), is(INVALID_CREDIT_CARD_ID));
    assertThat(deleted.getError().getMessage(), is("Invalid credit card id"));
    assertThat(deleted.getError().getParam(), is("credit_card_id"));
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
    assertThat(list.getError(), nullValue());
  }

}

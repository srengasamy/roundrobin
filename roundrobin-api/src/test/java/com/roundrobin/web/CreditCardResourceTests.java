package com.roundrobin.web;

import com.roundrobin.api.BankAccountTo;
import com.roundrobin.api.CredentialTo;
import com.roundrobin.api.CreditCardTo;
import com.roundrobin.api.Error;
import com.roundrobin.api.Response;
import com.roundrobin.api.UserProfileTo;
import com.roundrobin.common.ErrorCode;
import com.roundrobin.domain.CreditCard;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by rengasu on 5/13/16.
 */
public class CreditCardResourceTests extends ResourceTests {

  @Test
  public void testCreate() {
    Response<UserProfileTo> profile = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setCardNumber(Optional.of("4015260001266035"));
    creditCardTo.setCvv(Optional.of("124"));
    creditCardTo.setExpiryMonth(Optional.of((byte) 6));
    creditCardTo.setExpiryYear(Optional.of((short) 17));
    creditCardTo.setPostalCode(Optional.of("87878"));
    creditCardTo.setUserProfileId(profile.getEntity().getId());
    Response<CreditCardTo> created = helper.post(url + "credit-card", creditCardTo, new ParameterizedTypeReference<Response<CreditCardTo>>() {
    }, port).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<CreditCardTo> read = helper.get(url + "credit-card/{creditCardId}", new ParameterizedTypeReference<Response<CreditCardTo>>() {
    }, port, created.getEntity().getId()).getBody();
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
    Response<String> created = helper.post(url + "credit-card", creditCardTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "cvv: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "expiryMonth: may not be null"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "postalCode: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "expiryYear: may not be null"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "userProfileId: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "cardNumber: may not be empty")
    ));
  }

  @Test
  public void testCreateWithInvalidValues() {
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setExpiryYear(Optional.of((short) 11));
    creditCardTo.setExpiryMonth(Optional.of((byte) 15));
    creditCardTo.setPostalCode(Optional.of("1213213123"));
    creditCardTo.setCvv(Optional.of("dfskfjsdkfjs"));
    creditCardTo.setCardNumber(Optional.of("abcd"));
    Response<String> created = helper.post(url + "credit-card", creditCardTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "cvv: length must be between 0 and 5"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "expiryMonth: must be between 1 and 12"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "postalCode: length must be between 0 and 7"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "expiryYear: must be between 16 and 99"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "cardNumber: invalid credit card number")
    ));
  }

  @Test
  public void testCreateWithInvalidProfileId() {
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setCardNumber(Optional.of("4015260001266035"));
    creditCardTo.setCvv(Optional.of("124"));
    creditCardTo.setExpiryMonth(Optional.of((byte) 6));
    creditCardTo.setExpiryYear(Optional.of((short) 17));
    creditCardTo.setPostalCode(Optional.of("87878"));
    creditCardTo.setUserProfileId("testing");
    Response<String> created = helper.post(url + "credit-card", creditCardTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_PROFILE_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_PROFILE_ID))
    ));
  }

  @Test
  public void testUpdate() {
    Response<UserProfileTo> profile = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setCardNumber(Optional.of("4015260001266035"));
    creditCardTo.setCvv(Optional.of("124"));
    creditCardTo.setExpiryMonth(Optional.of((byte) 6));
    creditCardTo.setExpiryYear(Optional.of((short) 17));
    creditCardTo.setPostalCode(Optional.of("87878"));
    creditCardTo.setUserProfileId(profile.getEntity().getId());
    Response<CreditCardTo> created = helper.post(url + "credit-card", creditCardTo, new ParameterizedTypeReference<Response<CreditCardTo>>() {
    }, port).getBody();
    assertThat(created.getEntity(), notNullValue());
    created.getEntity().setCvv(Optional.of("test"));
    created.getEntity().setPostalCode(Optional.of("test"));
    created.getEntity().setExpiryMonth(Optional.of((byte) 12));
    created.getEntity().setExpiryYear(Optional.of((short) 99));
    Response<CreditCardTo> updated = helper.put(url + "credit-card", created.getEntity(), new ParameterizedTypeReference<Response<CreditCardTo>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), notNullValue());
    Response<CreditCardTo> read = helper.get(url + "credit-card/{creditCardId}", new ParameterizedTypeReference<Response<CreditCardTo>>() {
    }, port, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getExpiryMonth().get(), is(created.getEntity().getExpiryMonth().get()));
    assertThat(read.getEntity().getExpiryYear().get(), is(created.getEntity().getExpiryYear().get()));
    assertThat(read.getEntity().getPostalCode().get(), is(created.getEntity().getPostalCode().get()));
  }

  @Test
  public void testUpdateWithEmptyValues() {
    CreditCardTo creditCardTo = new CreditCardTo();
    Response<CreditCardTo> updated = helper.put(url + "credit-card", creditCardTo, new ParameterizedTypeReference<Response<CreditCardTo>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "id: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "cvv: may not be empty")
    ));
  }

  @Test
  public void testUpdateWithInvalidValues() {
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setExpiryYear(Optional.of((short) 11));
    creditCardTo.setExpiryMonth(Optional.of((byte) 15));
    creditCardTo.setPostalCode(Optional.of("1213213123"));
    creditCardTo.setCvv(Optional.of("dfskfjsdkfjs"));
    creditCardTo.setCardNumber(Optional.of("abcd"));
    Response<CreditCardTo> updated = helper.put(url + "credit-card", creditCardTo, new ParameterizedTypeReference<Response<CreditCardTo>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "cvv: length must be between 0 and 5"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "expiryMonth: must be between 1 and 12"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "postalCode: length must be between 0 and 7"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "expiryYear: must be between 16 and 99")
    ));
  }

  @Test
  public void testUpdateWithInvalidId() {
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setId("testing");
    creditCardTo.setCvv(Optional.of("123"));
    Response<CreditCardTo> updated = helper.put(url + "credit-card", creditCardTo, new ParameterizedTypeReference<Response<CreditCardTo>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_CREDIT_CARD_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_CREDIT_CARD_ID))
    ));
  }

  @Test
  public void testRead() {
    Response<UserProfileTo> profile = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setCardNumber(Optional.of("4015260001266035"));
    creditCardTo.setCvv(Optional.of("124"));
    creditCardTo.setExpiryMonth(Optional.of((byte) 6));
    creditCardTo.setExpiryYear(Optional.of((short) 17));
    creditCardTo.setPostalCode(Optional.of("87878"));
    creditCardTo.setUserProfileId(profile.getEntity().getId());
    Response<CreditCardTo> created = helper.post(url + "credit-card", creditCardTo, new ParameterizedTypeReference<Response<CreditCardTo>>() {
    }, port).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<CreditCardTo> read = helper.get(url + "credit-card/{creditCardId}", new ParameterizedTypeReference<Response<CreditCardTo>>() {
    }, port, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getCardNumber().get(), is("************6035"));
    assertThat(read.getEntity().getExpiryMonth().get(), is(creditCardTo.getExpiryMonth().get()));
    assertThat(read.getEntity().getExpiryYear().get(), is(creditCardTo.getExpiryYear().get()));
    assertThat(read.getEntity().getPostalCode().get(), is(creditCardTo.getPostalCode().get()));
  }

  @Test
  public void testReadWithEmptyId() {
    Response<CreditCardTo> read = helper.get(url + "credit-card/{creditCardId}", new ParameterizedTypeReference<Response<CreditCardTo>>() {
    }, port, " ").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_URL.getCode(), messages.getErrorMessage(ErrorCode.INVALID_URL))
    ));
  }

  @Test
  public void testReadWithInvalidId() {
    Response<CreditCardTo> read = helper.get(url + "credit-card/{creditCardId}", new ParameterizedTypeReference<Response<CreditCardTo>>() {
    }, port, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_CREDIT_CARD_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_CREDIT_CARD_ID))
    ));
  }

  @Test
  public void testDelete() {
    Response<UserProfileTo> profile = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setCardNumber(Optional.of("4015260001266035"));
    creditCardTo.setCvv(Optional.of("124"));
    creditCardTo.setExpiryMonth(Optional.of((byte) 6));
    creditCardTo.setExpiryYear(Optional.of((short) 17));
    creditCardTo.setPostalCode(Optional.of("87878"));
    creditCardTo.setUserProfileId(profile.getEntity().getId());
    Response<CreditCardTo> created = helper.post(url + "credit-card", creditCardTo, new ParameterizedTypeReference<Response<CreditCardTo>>() {
    }, port).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<Boolean> deleted = helper.delete(url + "credit-card/{creditCardId}", new ParameterizedTypeReference<Response<Boolean>>() {
    }, port, created.getEntity().getId()).getBody();
    assertThat(deleted.getEntity(), notNullValue());
    assertThat(deleted.getEntity(), is(true));
    Response<Boolean> read = helper.get(url + "credit-card/{creditCardId}", new ParameterizedTypeReference<Response<Boolean>>() {
    }, port, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_CREDIT_CARD_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_CREDIT_CARD_ID))
    ));
  }

  @Test
  public void testDeleteWithEmptyId() {
    Response<CreditCardTo> deleted = helper.delete(url + "credit-card/{creditCardId}", new ParameterizedTypeReference<Response<CreditCardTo>>() {
    }, port, " ").getBody();
    assertThat(deleted.getEntity(), nullValue());
    assertThat(deleted.getErrors(), notNullValue());
    assertThat(deleted.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_URL.getCode(), messages.getErrorMessage(ErrorCode.INVALID_URL))
    ));
  }

  @Test
  public void testDeleteWithInvalidId() {
    Response<CreditCardTo> deleted = helper.delete(url + "credit-card/{creditCardId}", new ParameterizedTypeReference<Response<CreditCardTo>>() {
    }, port, "testing").getBody();
    assertThat(deleted.getEntity(), nullValue());
    assertThat(deleted.getErrors(), notNullValue());
    assertThat(deleted.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_CREDIT_CARD_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_CREDIT_CARD_ID))
    ));
  }

  @Test
  public void testList() {
    Response<UserProfileTo> profile = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setCardNumber(Optional.of("4015260001266035"));
    creditCardTo.setCvv(Optional.of("124"));
    creditCardTo.setExpiryMonth(Optional.of((byte) 6));
    creditCardTo.setExpiryYear(Optional.of((short) 17));
    creditCardTo.setPostalCode(Optional.of("87878"));
    creditCardTo.setUserProfileId(profile.getEntity().getId());
    Response<CreditCardTo> created = helper.post(url + "credit-card", creditCardTo, new ParameterizedTypeReference<Response<CreditCardTo>>() {
    }, port).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<List<CreditCardTo>> list = helper.get(url + "credit-card?profileId={creditCardId}", new
            ParameterizedTypeReference<Response<List<CreditCardTo>>>() {
            }, port, profile.getEntity().getId()).getBody();
    assertThat(list.getEntity(), notNullValue());
    assertThat(list.getEntity().size(), is(1));
  }

  @Test
  public void testListWithEmptyId() {
    Response<List<CreditCardTo>> list = helper.get(url + "credit-card", new
            ParameterizedTypeReference<Response<List<CreditCardTo>>>() {
            }, port).getBody();
    assertThat(list.getEntity(), nullValue());
    assertThat(list.getErrors(), notNullValue());
    assertThat(list.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_URL.getCode(), messages.getErrorMessage(ErrorCode.INVALID_URL))));
  }

  @Test
  public void testListWithEmptyProfileId() {
    Response<List<CreditCardTo>> list = helper.get(url + "credit-card?profileId={creditCardId}", new
            ParameterizedTypeReference<Response<List<CreditCardTo>>>() {
            }, port, "").getBody();
    assertThat(list.getEntity(), nullValue());
    assertThat(list.getErrors(), notNullValue());
    assertThat(list.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_PROFILE_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_PROFILE_ID))));
  }

  @Test
  public void testListWithInvalidProfileId() {
    Response<List<CreditCardTo>> list = helper.get(url + "credit-card?profileId={creditCardId}", new
            ParameterizedTypeReference<Response<List<CreditCardTo>>>() {
            }, port, "testing").getBody();
    assertThat(list.getEntity(), nullValue());
    assertThat(list.getErrors(), notNullValue());
    assertThat(list.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_PROFILE_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_PROFILE_ID))));
  }
}

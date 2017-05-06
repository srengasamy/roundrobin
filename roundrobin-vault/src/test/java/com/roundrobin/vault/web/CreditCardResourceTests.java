package com.roundrobin.vault.web;

import com.roundrobin.core.api.Response;
import com.roundrobin.test.api.UnauthorizedError;
import com.roundrobin.vault.ResourceTests;
import com.roundrobin.vault.api.CreditCardTo;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.List;

import static com.roundrobin.core.common.ErrorCodes.INVALID_FIELD;
import static com.roundrobin.core.common.ErrorTypes.INVALID_REQUEST_ERROR;
import static com.roundrobin.vault.common.ErrorCodes.INVALID_CREDIT_CARD_ID;
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
    String token = getMockAccessToken("1234");
    String userId = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setLast4("1234");
    creditCardTo.setBrand("VISA");
    creditCardTo.setExpiryMonth((byte) 9);
    creditCardTo.setExpiryYear((short) 2019);
    creditCardTo.setPostalCode("95051");
    Response<CreditCardTo> created =
            template.exchange(vaultUrl + "credit-card", HttpMethod.POST,
                    createHttpEntity(creditCardTo, createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<CreditCardTo>>() {
                    }).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getError(), nullValue());
    assertThat(created.getEntity().getLast4(), is(creditCardTo.getLast4()));
    assertThat(created.getEntity().getBrand(), is(creditCardTo.getBrand()));
    assertThat(created.getEntity().getExpiryMonth(), is(creditCardTo.getExpiryMonth()));
    assertThat(created.getEntity().getExpiryYear(), is(creditCardTo.getExpiryYear()));
    assertThat(created.getEntity().getPostalCode(), is(creditCardTo.getPostalCode()));
  }

  @Test
  public void testCreateWithEmptyValues() {
    String userId = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    Response<String> created = template.exchange(vaultUrl + "credit-card", HttpMethod.POST,
            createHttpEntity(creditCardTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("expiryMonth: may not be null",
            "postalCode: may not be empty", "expiryYear: may not be null", "last4: may not be empty", "brand: may not" +
                    " be empty"));
  }

  @Test
  public void testCreateWithInvalidValues() {
    String userId = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setExpiryYear((short) 11);
    creditCardTo.setExpiryMonth((byte) 15);
    creditCardTo.setPostalCode("1213213123");
    creditCardTo.setBrand("dfskfjssdfksdfkjsdkfjsdkfsdjhfksdhfjksdhfkjsdhfkdkfjs");
    creditCardTo.setLast4("abcd2");
    Response<String> created = template.exchange(vaultUrl + "credit-card", HttpMethod.POST,
            createHttpEntity(creditCardTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(),
            hasItems("last4: length must be between 0 and 4", "expiryMonth: must be between 1 and 12",
                    "postalCode: length must be between 0 and 7", "expiryYear: must be between 2017 and 2099",
                    "brand: length must be between 0 and 25"));
  }

  @Test
  public void testCreateWithEmptyToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "credit-card",
            HttpMethod.POST,
            createHttpEntity(new HashMap<String, String>()), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("unauthorized"));
  }

  @Test
  public void testCreateWithInvalidToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "credit-card",
            HttpMethod.POST,
            createHttpEntity(createBearerHeader("testing")), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("invalid_token"));
  }

  @Test
  public void testUpdate() {
    String userId = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setLast4("1234");
    creditCardTo.setBrand("VISA");
    creditCardTo.setExpiryMonth((byte) 9);
    creditCardTo.setExpiryYear((short) 2019);
    creditCardTo.setPostalCode("95051");
    Response<CreditCardTo> created =
            template.exchange(vaultUrl + "credit-card", HttpMethod.POST,
                    createHttpEntity(creditCardTo, createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<CreditCardTo>>() {
                    }).getBody();
    assertThat(created.getEntity(), notNullValue());
    created.getEntity().setPostalCode("test");
    created.getEntity().setExpiryMonth((byte) 12);
    created.getEntity().setExpiryYear((short) 2099);
    Response<CreditCardTo> updated = template.exchange(vaultUrl + "credit-card",
            HttpMethod.PUT,
            createHttpEntity(created.getEntity(), createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<CreditCardTo>>() {
            })
            .getBody();
    assertThat(updated.getEntity(), notNullValue());
    Response<CreditCardTo> read =
            template.exchange(vaultUrl + "credit-card/{creditCardId}", HttpMethod.GET,
                    createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<CreditCardTo>>() {
                    }, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getExpiryMonth(), is(created.getEntity().getExpiryMonth()));
    assertThat(read.getEntity().getExpiryYear(), is(created.getEntity().getExpiryYear()));
    assertThat(read.getEntity().getPostalCode(), is(created.getEntity().getPostalCode()));
  }

  @Test
  public void testUpdateWithEmptyValues() {
    String userId = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    Response<CreditCardTo> updated = template.exchange(vaultUrl + "credit-card",
            HttpMethod.PUT,
            createHttpEntity(creditCardTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<CreditCardTo>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_FIELD));
    assertThat(updated.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(updated.getError().getFieldErrors(), hasItems("id: may not be empty"));
  }

  @Test
  public void testUpdateWithInvalidValues() {
    String userId = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setExpiryYear((short) 2015);
    creditCardTo.setExpiryMonth((byte) 15);
    creditCardTo.setPostalCode("1213213123");
    Response<CreditCardTo> updated = template.exchange(vaultUrl + "credit-card", HttpMethod.PUT,
            createHttpEntity(creditCardTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<CreditCardTo>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_FIELD));
    assertThat(updated.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(updated.getError().getFieldErrors(),
            hasItems("expiryMonth: must be between 1 and 12",
                    "postalCode: length must be between 0 and 7", "expiryYear: must be between 2017 and 2099"));
  }

  @Test
  public void testUpdateWithInvalidId() {
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setId("testing");
    Response<CreditCardTo> updated = template.exchange(vaultUrl + "credit-card", HttpMethod.PUT,
            createHttpEntity(creditCardTo, createBearerHeader(getMockAccessToken(createUserProfile()))),
            new ParameterizedTypeReference<Response<CreditCardTo>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_CREDIT_CARD_ID));
    assertThat(updated.getError().getMessage(), is("Invalid credit card id"));
    assertThat(updated.getError().getParam(), is("credit_card_id"));
  }

  @Test
  public void testUpdateWithWrongId() {
    String userId = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setLast4("1234");
    creditCardTo.setBrand("VISA");
    creditCardTo.setExpiryMonth((byte) 9);
    creditCardTo.setExpiryYear((short) 2019);
    creditCardTo.setPostalCode("95051");
    Response<CreditCardTo> created =
            template.exchange(vaultUrl + "credit-card", HttpMethod.POST,
                    createHttpEntity(creditCardTo, createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<CreditCardTo>>() {
                    }).getBody();
    assertThat(created.getEntity(), notNullValue());
    CreditCardTo newCreditCardTo = new CreditCardTo();
    newCreditCardTo.setId(created.getEntity().getId());
    Response<CreditCardTo> updated = template.exchange(vaultUrl + "credit-card", HttpMethod.PUT,
            createHttpEntity(newCreditCardTo, createBearerHeader(getMockAccessToken(createUserProfile()))),
            new ParameterizedTypeReference<Response<CreditCardTo>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_CREDIT_CARD_ID));
    assertThat(updated.getError().getMessage(), is("Invalid credit card id"));
    assertThat(updated.getError().getParam(), is("credit_card_id"));
  }

  @Test
  public void testUpdateWithEmptyToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "credit-card",
            HttpMethod.PUT,
            createHttpEntity(new HashMap<String, String>()), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("unauthorized"));
  }

  @Test
  public void testUpdateWithInvalidToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "credit-card",
            HttpMethod.PUT,
            createHttpEntity(createBearerHeader("testing")), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("invalid_token"));
  }

  @Test
  public void testRead() {
    String userId = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setLast4("1234");
    creditCardTo.setBrand("VISA");
    creditCardTo.setExpiryMonth((byte) 9);
    creditCardTo.setExpiryYear((short) 2019);
    creditCardTo.setPostalCode("95051");
    Response<CreditCardTo> created =
            template.exchange(vaultUrl + "credit-card", HttpMethod.POST,
                    createHttpEntity(creditCardTo, createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<CreditCardTo>>() {
                    }).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<CreditCardTo> read =
            template.exchange(vaultUrl + "credit-card/{creditCardId}", HttpMethod.GET,
                    createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<CreditCardTo>>() {
                    }, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getLast4(), is(creditCardTo.getLast4()));
    assertThat(read.getEntity().getBrand(), is(creditCardTo.getBrand()));
    assertThat(read.getEntity().getExpiryMonth(), is(creditCardTo.getExpiryMonth()));
    assertThat(read.getEntity().getExpiryYear(), is(creditCardTo.getExpiryYear()));
    assertThat(read.getEntity().getPostalCode(), is(creditCardTo.getPostalCode()));
  }

  @Test
  public void testReadWithEmptyId() {
    String userId = createUserProfile();
    Response<List<CreditCardTo>> read =
            template.exchange(vaultUrl + "credit-card/{creditCardId}", HttpMethod.GET,
                    createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<List<CreditCardTo>>>() {
                    }, " ").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_CREDIT_CARD_ID));
    assertThat(read.getError().getMessage(), is("Invalid credit card id"));
    assertThat(read.getError().getParam(), is("credit_card_id"));
  }

  @Test
  public void testReadWithInvalidId() {
    String userId = createUserProfile();
    Response<CreditCardTo> read =
            template.exchange(vaultUrl + "credit-card/{creditCardId}", HttpMethod.GET,
                    createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<CreditCardTo>>() {
                    }, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_CREDIT_CARD_ID));
    assertThat(read.getError().getMessage(), is("Invalid credit card id"));
    assertThat(read.getError().getParam(), is("credit_card_id"));
  }

  @Test
  public void testReadWithWrongId() {
    String userId = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setLast4("1234");
    creditCardTo.setBrand("VISA");
    creditCardTo.setExpiryMonth((byte) 9);
    creditCardTo.setExpiryYear((short) 2019);
    creditCardTo.setPostalCode("95051");
    Response<CreditCardTo> created =
            template.exchange(vaultUrl + "credit-card", HttpMethod.POST,
                    createHttpEntity(creditCardTo, createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<CreditCardTo>>() {
                    }).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<CreditCardTo> read =
            template.exchange(vaultUrl + "credit-card/{creditCardId}", HttpMethod.GET,
                    createHttpEntity(createBearerHeader(getMockAccessToken(createUserProfile()))),
                    new ParameterizedTypeReference<Response<CreditCardTo>>() {
                    }, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_CREDIT_CARD_ID));
    assertThat(read.getError().getMessage(), is("Invalid credit card id"));
    assertThat(read.getError().getParam(), is("credit_card_id"));
  }

  @Test
  public void testReadWithEmptyToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "credit-card/1",
            HttpMethod.GET,
            createHttpEntity(new HashMap<String, String>()), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("unauthorized"));
  }

  @Test
  public void testReadWithInvalidToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "credit-card/1",
            HttpMethod.GET,
            createHttpEntity(createBearerHeader("testing")), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("invalid_token"));
  }

  @Test
  public void testDelete() {
    String userId = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setLast4("1234");
    creditCardTo.setBrand("VISA");
    creditCardTo.setExpiryMonth((byte) 9);
    creditCardTo.setExpiryYear((short) 2019);
    creditCardTo.setPostalCode("95051");
    Response<CreditCardTo> created =
            template.exchange(vaultUrl + "credit-card", HttpMethod.POST,
                    createHttpEntity(creditCardTo, createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<CreditCardTo>>() {
                    }).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<Boolean> deleted =
            template.exchange(vaultUrl + "credit-card/{creditCardId}", HttpMethod.DELETE,
                    createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<Boolean>>() {
                    }, created.getEntity().getId()).getBody();
    assertThat(deleted.getEntity(), notNullValue());
    assertThat(deleted.getEntity(), is(true));
    Response<Boolean> read =
            template.exchange(vaultUrl + "credit-card/{creditCardId}", HttpMethod.GET,
                    createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<Boolean>>() {
                    }, created.getEntity().getId()).getBody();
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
            template.exchange(vaultUrl + "credit-card/{creditCardId}", HttpMethod.DELETE,
                    createHttpEntity(createBearerHeader(getMockAccessToken(createUserProfile()))),
                    new ParameterizedTypeReference<Response<CreditCardTo>>() {
                    }, " ").getBody();
    assertThat(deleted.getEntity(), nullValue());
    assertThat(deleted.getError(), notNullValue());
    assertThat(deleted.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(deleted.getError().getCode(), is(INVALID_CREDIT_CARD_ID));
    assertThat(deleted.getError().getMessage(), is("Invalid credit card id"));
    assertThat(deleted.getError().getParam(), is("credit_card_id"));
  }

  @Test
  public void testDeleteWithInvalidId() {
    String userId = createUserProfile();
    Response<CreditCardTo> deleted =
            template.exchange(vaultUrl + "credit-card/{creditCardId}", HttpMethod.DELETE,
                    createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<CreditCardTo>>() {
                    }, "testing").getBody();
    assertThat(deleted.getEntity(), nullValue());
    assertThat(deleted.getError(), notNullValue());
    assertThat(deleted.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(deleted.getError().getCode(), is(INVALID_CREDIT_CARD_ID));
    assertThat(deleted.getError().getMessage(), is("Invalid credit card id"));
    assertThat(deleted.getError().getParam(), is("credit_card_id"));
  }

  @Test
  public void testDeleteWithWrongId() {
    String userId = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setLast4("1234");
    creditCardTo.setBrand("VISA");
    creditCardTo.setExpiryMonth((byte) 9);
    creditCardTo.setExpiryYear((short) 2019);
    creditCardTo.setPostalCode("95051");
    Response<CreditCardTo> created =
            template.exchange(vaultUrl + "credit-card", HttpMethod.POST,
                    createHttpEntity(creditCardTo, createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<CreditCardTo>>() {
                    }).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<CreditCardTo> deleted =
            template.exchange(vaultUrl + "credit-card/{creditCardId}", HttpMethod.DELETE,
                    createHttpEntity(createBearerHeader(getMockAccessToken(createUserProfile()))),
                    new ParameterizedTypeReference<Response<CreditCardTo>>() {
                    }, created.getEntity().getId()).getBody();
    assertThat(deleted.getEntity(), nullValue());
    assertThat(deleted.getError(), notNullValue());
    assertThat(deleted.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(deleted.getError().getCode(), is(INVALID_CREDIT_CARD_ID));
    assertThat(deleted.getError().getMessage(), is("Invalid credit card id"));
    assertThat(deleted.getError().getParam(), is("credit_card_id"));
  }

  @Test
  public void testDeleteWithEmptyToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "credit-card/1",
            HttpMethod.DELETE,
            createHttpEntity(new HashMap<String, String>()), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("unauthorized"));
  }

  @Test
  public void testDeleteWithInvalidToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "credit-card/1",
            HttpMethod.DELETE,
            createHttpEntity(createBearerHeader("testing")), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("invalid_token"));
  }

  @Test
  public void testList() {
    String userId = createUserProfile();
    CreditCardTo creditCardTo = new CreditCardTo();
    creditCardTo.setLast4("1234");
    creditCardTo.setBrand("VISA");
    creditCardTo.setExpiryMonth((byte) 9);
    creditCardTo.setExpiryYear((short) 2019);
    creditCardTo.setPostalCode("95051");
    Response<CreditCardTo> created =
            template.exchange(vaultUrl + "credit-card", HttpMethod.POST,
                    createHttpEntity(creditCardTo, createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<CreditCardTo>>() {
                    }).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<List<CreditCardTo>> list =
            template.exchange(vaultUrl + "credit-card", HttpMethod.GET,
                    createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<List<CreditCardTo>>>() {
                    }).getBody();
    assertThat(list.getEntity(), notNullValue());
    assertThat(list.getEntity().size(), is(1));
  }

  @Test
  public void testListWithEmptyId() {
    String userId = createUserProfile();
    Response<List<CreditCardTo>> list =
            template.exchange(vaultUrl + "credit-card", HttpMethod.GET,
                    createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<List<CreditCardTo>>>() {
                    }).getBody();
    assertThat(list.getEntity(), notNullValue());
    assertThat(list.getEntity().size(), is(0));
    assertThat(list.getError(), nullValue());
  }

  @Test
  public void testListWithEmptyToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "credit-card",
            HttpMethod.GET,
            createHttpEntity(new HashMap<String, String>()), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("unauthorized"));
  }

  @Test
  public void testListWithInvalidToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "credit-card",
            HttpMethod.GET,
            createHttpEntity(createBearerHeader("testing")), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("invalid_token"));
  }

}

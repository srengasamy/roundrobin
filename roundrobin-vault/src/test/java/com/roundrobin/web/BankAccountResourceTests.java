package com.roundrobin.web;

import com.roundrobin.core.api.Response;
import com.roundrobin.test.api.UnauthorizedError;
import com.roundrobin.vault.api.BankAccountTo;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.roundrobin.core.common.ErrorCodes.INVALID_FIELD;
import static com.roundrobin.core.common.ErrorTypes.INVALID_REQUEST_ERROR;
import static com.roundrobin.vault.common.ErrorCodes.INVALID_BANK_ACCOUNT_ID;
import static com.roundrobin.vault.common.ErrorCodes.INVALID_USER_ID;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by rengasu on 5/12/16.
 */

// TODO Dont allow delete when list has only one
// TODO Test getAll
// TODO Revisit all services
// TODO Reconsider allowing update
public class BankAccountResourceTests extends ResourceTests {

  @Test
  public void testCreate() {
    String userId = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setLast4("1234");
    bankAccountTo.setBankName("Bank of America");
    Response<BankAccountTo> created =
            template.exchange(vaultUrl + "bank-account", HttpMethod.POST,
                    createHttpEntity(bankAccountTo, createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<BankAccountTo>>() {
                    }).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity().getLast4(), is("1234"));
    assertThat(created.getEntity().getBankName(), is(bankAccountTo.getBankName()));
  }


  @Test
  public void testCreateWithEmptyValue() {
    String userId = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    Response<BankAccountTo> created =
            template.exchange(vaultUrl + "bank-account", HttpMethod.POST,
                    createHttpEntity(bankAccountTo, createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<BankAccountTo>>() {
                    }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("bankName: may not be empty",
            "last4: may not be empty"));
  }


  @Test
  public void testCreateWithInvalidValue() {
    String userId = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setLast4("12342");
    bankAccountTo.setBankName("Bank of Americaasdjh sadjhajskhdk asjdhkjas dkashdkjas dkashd kjas dashdjk " +
            "asdkasdkasdkasdk");
    Response<BankAccountTo> created =
            template.exchange(vaultUrl + "bank-account", HttpMethod.POST,
                    createHttpEntity(bankAccountTo, createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<BankAccountTo>>() {
                    }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(),
            hasItems("bankName: length must be between 0 and 50", "last4: length must be between 0 and 4"));
  }

  @Test
  public void testCreateWithEmptyToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "bank-account",
            HttpMethod.POST,
            createHttpEntity(new HashMap<String, String>()), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("unauthorized"));
  }

  @Test
  public void testCreateWithInvalidToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "bank-account",
            HttpMethod.POST,
            createHttpEntity(createBearerHeader("testing")), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("invalid_token"));
  }

  @Test
  public void testRead() {
    String userId = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setLast4("1234");
    bankAccountTo.setBankName("Bank of America");
    Response<BankAccountTo> created =
            template.exchange(vaultUrl + "bank-account", HttpMethod.POST,
                    createHttpEntity(bankAccountTo, createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<BankAccountTo>>() {
                    }).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<BankAccountTo> read =
            template.exchange(vaultUrl + "bank-account/{bankAccountId}", HttpMethod.GET,
                    createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<BankAccountTo>>() {
                    }, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getLast4(), is(bankAccountTo.getLast4()));
    assertThat(read.getEntity().getBankName(), is(bankAccountTo.getBankName()));
  }


  @Test
  public void testReadWithEmptyId() {
    String userId = createUserProfile();
    Response<List<BankAccountTo>> read =
            template.exchange(vaultUrl + "bank-account/{bankAccountId}",
                    HttpMethod.GET,
                    createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<List<BankAccountTo>>>() {
                    }, " ").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_BANK_ACCOUNT_ID));
    assertThat(read.getError().getMessage(), is("Invalid bank account id"));
    assertThat(read.getError().getParam(), is("bank_account_id"));
  }


  @Test
  public void testReadWithInvalidId() {
    String userId = createUserProfile();
    Response<List<BankAccountTo>> read =
            template.exchange(vaultUrl + "bank-account/{bankAccountId}",
                    HttpMethod.GET,
                    createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<List<BankAccountTo>>>() {
                    }, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_BANK_ACCOUNT_ID));
    assertThat(read.getError().getMessage(), is("Invalid bank account id"));
    assertThat(read.getError().getParam(), is("bank_account_id"));
  }

  @Test
  public void testReadWithEmptyToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "bank-account/1",
            HttpMethod.GET,
            createHttpEntity(new HashMap<String, String>()), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("unauthorized"));
  }

  @Test
  public void testReadWithInvalidToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "bank-account/1",
            HttpMethod.GET,
            createHttpEntity(createBearerHeader("testing")), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("invalid_token"));
  }

  @Test
  public void testDelete() {
    String userId = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setLast4("1234");
    bankAccountTo.setBankName("Bank of America");
    Response<BankAccountTo> created =
            template.exchange(vaultUrl + "bank-account", HttpMethod.POST,
                    createHttpEntity(bankAccountTo, createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<BankAccountTo>>() {
                    }).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<Boolean> deleted =
            template.exchange(vaultUrl + "bank-account/{bankAccountId}", HttpMethod.DELETE,
                    createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<Boolean>>() {
                    }, created.getEntity().getId()).getBody();
    assertThat(deleted.getEntity(), notNullValue());
    assertThat(deleted.getEntity(), is(true));
    Response<Boolean> read =
            template.exchange(vaultUrl + "bank-account/{bankAccountId}", HttpMethod.GET,
                    createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<Boolean>>() {
                    }, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_BANK_ACCOUNT_ID));
    assertThat(read.getError().getMessage(), is("Invalid bank account id"));
    assertThat(read.getError().getParam(), is("bank_account_id"));
  }

  @Test
  public void testDeleteWithEmptyId() {
    String userId = createUserProfile();
    Response<String> deleted =
            template.exchange(vaultUrl + "bank-account/{bankAccountId}", HttpMethod.DELETE,
                    createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<String>>() {
                    }, " ").getBody();
    assertThat(deleted.getEntity(), nullValue());
    assertThat(deleted.getError(), notNullValue());
    assertThat(deleted.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(deleted.getError().getCode(), is(INVALID_BANK_ACCOUNT_ID));
    assertThat(deleted.getError().getMessage(), is("Invalid bank account id"));
    assertThat(deleted.getError().getParam(), is("bank_account_id"));
  }

  @Test
  public void testDeleteWithInvalidId() {
    String userId = createUserProfile();
    Response<String> deleted =
            template.exchange(vaultUrl + "bank-account/{bankAccountId}", HttpMethod.DELETE,
                    createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<String>>() {
                    }, "testing").getBody();
    assertThat(deleted.getEntity(), nullValue());
    assertThat(deleted.getError(), notNullValue());
    assertThat(deleted.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(deleted.getError().getCode(), is(INVALID_BANK_ACCOUNT_ID));
    assertThat(deleted.getError().getMessage(), is("Invalid bank account id"));
    assertThat(deleted.getError().getParam(), is("bank_account_id"));
  }

  @Test
  public void testDeleteWithEmptyToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "bank-account/1",
            HttpMethod.DELETE,
            createHttpEntity(new HashMap<String, String>()), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("unauthorized"));
  }

  @Test
  public void testDeleteWithInvalidToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "bank-account/1",
            HttpMethod.DELETE,
            createHttpEntity(createBearerHeader("testing")), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("invalid_token"));
  }

  @Test
  public void testList() {
    String userId = createUserProfile();
    BankAccountTo bankAccountTo = new BankAccountTo();
    bankAccountTo.setLast4("1234");
    bankAccountTo.setBankName("Bank of America");
    Response<BankAccountTo> created =
            template.exchange(vaultUrl + "bank-account", HttpMethod.POST,
                    createHttpEntity(bankAccountTo, createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<BankAccountTo>>() {
                    }).getBody();
    assertThat(created.getEntity(), notNullValue());

    Response<List<BankAccountTo>> list =
            template.exchange(vaultUrl + "bank-account", HttpMethod.GET,
                    createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<List<BankAccountTo>>>() {
                    }).getBody();
    assertThat(list.getEntity(), notNullValue());
    assertThat(list.getEntity().size(), is(1));
  }

  @Test
  public void testListWithoutProfile() {
    Response<List<BankAccountTo>> list =
            template.exchange(vaultUrl + "bank-account", HttpMethod.GET,
                    createHttpEntity(createBearerHeader(getMockAccessToken(UUID.randomUUID().toString()))),
                    new ParameterizedTypeReference<Response<List<BankAccountTo>>>() {
                    }).getBody();
    assertThat(list.getEntity(), nullValue());
    assertThat(list.getError(), notNullValue());
    assertThat(list.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(list.getError().getCode(), is(INVALID_USER_ID));
    assertThat(list.getError().getMessage(), is("Invalid user id"));
  }

  @Test
  public void testListWithEmptyToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "bank-account",
            HttpMethod.GET,
            createHttpEntity(new HashMap<String, String>()), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("unauthorized"));
  }

  @Test
  public void testListWithInvalidToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "bank-account",
            HttpMethod.GET,
            createHttpEntity(createBearerHeader("testing")), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("invalid_token"));
  }
}

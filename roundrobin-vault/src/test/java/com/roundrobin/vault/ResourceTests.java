package com.roundrobin.vault;

import com.roundrobin.core.api.Response;
import com.roundrobin.test.common.AbstractResourceTests;
import com.roundrobin.vault.api.UserProfileTo;
import com.roundrobin.vault.enums.SexType;
import org.joda.time.LocalDate;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by rengasu on 4/25/17.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RoundRobin.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ResourceTests extends AbstractResourceTests {

  public String createUserProfile() {
    String userId = UUID.randomUUID().toString();
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setFirstName(Optional.of("Suresh"));
    userProfileTo.setLastName(Optional.of("Rengasamy"));
    userProfileTo.setMobileNumber(Optional.of("5555555555"));
    userProfileTo.setHomeNumber(Optional.of("5555555555"));
    userProfileTo.setVendor(Optional.of(false));
    userProfileTo.setDob(Optional.of(LocalDate.now()));
    userProfileTo.setSex(Optional.of(SexType.MALE));
    Response<UserProfileTo> created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<UserProfileTo>>() {
            }).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getError(), nullValue());
    return userId;
  }

}

package com.roundrobin.auth.web;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.nio.charset.Charset;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.roundrobin.api.Response;
import com.roundrobin.api.Token;
import com.roundrobin.auth.RoundRobin;
import com.roundrobin.auth.api.UserTo;
import com.roundrobin.common.ClientMessages;
import com.roundrobin.helper.TestHttpHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(RoundRobin.class)
@WebIntegrationTest(randomPort = true)
@Ignore
public class ResourceTests {
  @Value("${local.server.port}")
  protected int port;
  protected final String url = "http://localhost:{port}/roundrobin-auth/";

  @Autowired
  protected TestHttpHelper helper;

  @Autowired
  protected ClientMessages messages;

  protected String createUser() {
    return createUser(false);
  }

  protected String createUser(boolean vendor) {
    UserTo userTo = new UserTo();
    userTo.setUsername(Optional.of("testing" + System.currentTimeMillis() + "@testing.com"));
    userTo.setVendor(Optional.of(vendor));
    userTo.setPassword(Optional.of("testing"));
    Response<Boolean> created = helper
        .post(url + "user-action/create-user", userTo, new ParameterizedTypeReference<Response<Boolean>>() {}, port)
        .getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity(), is(true));
    return userTo.getUsername().get();
  }

  protected Token getToken(String username) {
    return getToken(username, "testing");
  }

  protected Token getToken(String username, String password) {
    Token token = helper.post(url + "oauth/token?grant_type=password&username={username}&password={password}",
        createWebClientHeaders(), (String) null, new ParameterizedTypeReference<Token>() {}, port, username, password)
        .getBody();
    assertThat(token, notNullValue());
    return token;
  }

  protected HttpHeaders createWebClientHeaders() {
    return new HttpHeaders() {
      private static final long serialVersionUID = 2987123839863076324L;
      {
        String auth = "web2:secret";
        byte[] encodedAuth = Base64.encode(auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);
        set("Authorization", authHeader);
      }
    };
  }

  protected HttpHeaders createBearerHeaders(String token) {
    return new HttpHeaders() {
      private static final long serialVersionUID = 2987123839863076324L;
      {
        String authHeader = "Bearer " + token;
        set("Authorization", authHeader);
      }
    };
  }
  /*
   * protected Response<SkillGroupTo> createSkillGroup(SkillGroupTo skillGroupTo) { return helper
   * .post(url + "skill-group", skillGroupTo, new
   * ParameterizedTypeReference<Response<SkillGroupTo>>() { }, port) .getBody(); }
   * 
   * protected Response<UserProfileTo> createUserProfile() { return
   * createUserProfile("testing@testing.com"); }
   * 
   * protected Response<UserProfileTo> createUserProfile(String email) { UserProfileTo userProfileTo
   * = new UserProfileTo(); userProfileTo.setFirstName(Optional.of("Suresh"));
   * userProfileTo.setLastName(Optional.of("Rengasamy"));
   * userProfileTo.setEmail(Optional.of(email));
   * userProfileTo.setMobileNumber(Optional.of("5555555555"));
   * userProfileTo.setHomeNumber(Optional.of("5555555555"));
   * userProfileTo.setVendor(Optional.of(false)); userProfileTo.setPassword(Optional.of("testing"));
   * userProfileTo.setDob(Optional.of(LocalDate.now()));
   * userProfileTo.setSex(Optional.of(UserProfile.SexType.MALE)); return helper.post(url +
   * "user-profile", userProfileTo, new ParameterizedTypeReference<Response<UserProfileTo>>() { },
   * port).getBody(); }
   * 
   * protected SkillGroupTo createSkillGroup() { String groupName = "Testing" +
   * System.currentTimeMillis(); SkillGroupTo skillGroupTo = new SkillGroupTo();
   * skillGroupTo.setGroupName(Optional.of(groupName)); Response<SkillGroupTo> created =
   * createSkillGroup(skillGroupTo); return created.getEntity(); }
   * 
   * protected SkillDetailTo createSkillDetail() { SkillGroupTo skillGroupTo = createSkillGroup();
   * String skillName = "Testing" + System.currentTimeMillis(); SkillDetailTo skillDetailTo = new
   * SkillDetailTo(); skillDetailTo.setDeliveryType(Optional.of(Skill.DeliveryType.HOME));
   * skillDetailTo.setName(Optional.of(skillName));
   * skillDetailTo.setSkillGroupId(skillGroupTo.getId()); Response<SkillDetailTo> createdSkill =
   * helper.post(url + "skill-detail", skillDetailTo, new
   * ParameterizedTypeReference<Response<SkillDetailTo>>() { }, port).getBody(); return
   * createdSkill.getEntity(); }
   */
}

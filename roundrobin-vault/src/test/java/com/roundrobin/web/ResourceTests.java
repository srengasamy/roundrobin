package com.roundrobin.web;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.joda.time.LocalDate;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.roundrobin.api.Response;
import com.roundrobin.common.AbstractResourceTests;
import com.roundrobin.vault.RoundRobin;
import com.roundrobin.vault.api.SkillDetailTo;
import com.roundrobin.vault.api.SkillGroupTo;
import com.roundrobin.vault.api.UserProfileTo;
import com.roundrobin.vault.domain.Skill;
import com.roundrobin.vault.domain.UserProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(RoundRobin.class)
@WebIntegrationTest(value = "server.port:9090")
@Ignore
public class ResourceTests extends AbstractResourceTests {

  protected Response<SkillGroupTo> createSkillGroup(SkillGroupTo skillGroupTo) {
    Response<SkillGroupTo> response = helper.post(vaultUrl + "skill-group", createBearerHeaders(), skillGroupTo,
        new ParameterizedTypeReference<Response<SkillGroupTo>>() {}).getBody();
    assertThat(response.getEntity(), notNullValue());
    assertThat(response.getErrors().size(), is(0));
    return response;
  }

  protected String createUserProfile() {
    String email = "testing" + System.currentTimeMillis() + "@testing.com";
    createUser(email);
    createUserProfile(email);
    return email;
  }

  private void createUserProfile(String email) {
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setFirstName(Optional.of("Suresh"));
    userProfileTo.setLastName(Optional.of("Rengasamy"));
    userProfileTo.setEmail(Optional.of(email));
    userProfileTo.setMobileNumber(Optional.of("5555555555"));
    userProfileTo.setHomeNumber(Optional.of("5555555555"));
    userProfileTo.setVendor(Optional.of(false));
    userProfileTo.setPassword(Optional.of("testing"));
    userProfileTo.setDob(Optional.of(LocalDate.now()));
    userProfileTo.setSex(Optional.of(UserProfile.SexType.MALE));
    Response<UserProfileTo> response =
        helper.post(vaultUrl + "user-profile", createBearerHeaders(getAccessToken(email)), userProfileTo,
            new ParameterizedTypeReference<Response<UserProfileTo>>() {}).getBody();
    assertThat(response.getEntity(), notNullValue());
    assertThat(response.getErrors().size(), is(0));
  }

  protected SkillGroupTo createSkillGroup() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = createSkillGroup(skillGroupTo);
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getErrors().size(), is(0));
    return created.getEntity();
  }

  protected SkillDetailTo createSkillDetail() {
    SkillGroupTo skillGroupTo = createSkillGroup();
    String skillName = "Testing" + System.currentTimeMillis();
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setDeliveryType(Optional.of(Skill.DeliveryType.HOME));
    skillDetailTo.setName(Optional.of(skillName));
    skillDetailTo.setSkillGroupId(skillGroupTo.getId());
    Response<SkillDetailTo> createdSkill = helper.post(vaultUrl + "skill-detail", createBearerHeaders(), skillDetailTo,
        new ParameterizedTypeReference<Response<SkillDetailTo>>() {}).getBody();
    return createdSkill.getEntity();
  }
}

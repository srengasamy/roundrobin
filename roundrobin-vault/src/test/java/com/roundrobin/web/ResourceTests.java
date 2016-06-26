package com.roundrobin.web;

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
    return helper
        .post(vaultUrl + "skill-group", skillGroupTo, new ParameterizedTypeReference<Response<SkillGroupTo>>() {})
        .getBody();
  }

  protected Response<UserProfileTo> createUserProfile() {
    return createUserProfile("testing@testing.com");
  }

  protected Response<UserProfileTo> createUserProfile(String email) {
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
    return helper.post(vaultUrl + "user-profile", createBearerHeaders(getToken(email).getAccessToken()), userProfileTo,
        new ParameterizedTypeReference<Response<UserProfileTo>>() {}).getBody();
  }

  protected SkillGroupTo createSkillGroup() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = createSkillGroup(skillGroupTo);
    return created.getEntity();
  }

  protected SkillDetailTo createSkillDetail() {
    SkillGroupTo skillGroupTo = createSkillGroup();
    String skillName = "Testing" + System.currentTimeMillis();
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setDeliveryType(Optional.of(Skill.DeliveryType.HOME));
    skillDetailTo.setName(Optional.of(skillName));
    skillDetailTo.setSkillGroupId(skillGroupTo.getId());
    Response<SkillDetailTo> createdSkill = helper
        .post(vaultUrl + "skill-detail", skillDetailTo, new ParameterizedTypeReference<Response<SkillDetailTo>>() {})
        .getBody();
    return createdSkill.getEntity();
  }
}

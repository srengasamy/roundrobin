package com.roundrobin.web;

import org.joda.time.LocalDate;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.roundrobin.RoundRobin;
import com.roundrobin.api.Response;
import com.roundrobin.api.SkillDetailTo;
import com.roundrobin.api.SkillGroupTo;
import com.roundrobin.api.UserProfileTo;
import com.roundrobin.common.ClientMessages;
import com.roundrobin.common.TestHttpHelper;
import com.roundrobin.domain.Skill;
import com.roundrobin.domain.UserProfile;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(RoundRobin.class)
@WebIntegrationTest(randomPort = true)
@Ignore
public class ResourceTests {
  @Value("${local.server.port}")
  protected int port;
  protected final String url = "http://localhost:{port}/roundrobin/";

  @Autowired
  protected TestHttpHelper helper;

  @Autowired
  protected ClientMessages messages;

  protected Response<SkillGroupTo> createSkillGroup(SkillGroupTo skillGroupTo) {
    return helper
            .post(url + "skill-group", skillGroupTo, new ParameterizedTypeReference<Response<SkillGroupTo>>() {
            }, port)
            .getBody();
  }

  protected Response<UserProfileTo> createUserProfile() {
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setFirstName(Optional.of("Suresh"));
    userProfileTo.setLastName(Optional.of("Rengasamy"));
    userProfileTo.setEmail(Optional.of("testing@testing.com"));
    userProfileTo.setMobileNumber(Optional.of("5555555555"));
    userProfileTo.setHomeNumber(Optional.of("5555555555"));
    userProfileTo.setVendor(Optional.of(false));
    userProfileTo.setPassword(Optional.of("testing"));
    userProfileTo.setDob(Optional.of(LocalDate.now()));
    userProfileTo.setSex(Optional.of(UserProfile.SexType.MALE));
    return helper.post(url + "user-profile", userProfileTo, new ParameterizedTypeReference<Response<UserProfileTo>>() {
    }, port).getBody();
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
    Response<SkillDetailTo> createdSkill = helper.post(url + "skill-detail", skillDetailTo, new ParameterizedTypeReference<Response<SkillDetailTo>>() {
    }, port).getBody();
    return createdSkill.getEntity();
  }
}

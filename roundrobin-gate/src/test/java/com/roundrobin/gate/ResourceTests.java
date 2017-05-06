package com.roundrobin.gate;

import com.roundrobin.core.api.Response;
import com.roundrobin.gate.api.SkillDetailTo;
import com.roundrobin.gate.api.SkillGroupTo;
import com.roundrobin.gate.api.UserProfileTo;
import com.roundrobin.gate.enums.DeliveryType;
import com.roundrobin.test.common.AbstractResourceTests;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by rengasu on 5/1/17.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RoundRobin.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ResourceTests extends AbstractResourceTests {

  @Value("${security.user.name}")
  private String username;

  @Value("${security.user.password}")
  private String password;

  public String createUserProfile() {
    String userId = UUID.randomUUID().toString();
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setClockIn((short) 0);
    userProfileTo.setClockOut((short) 1);
    userProfileTo.setLocation(new GeoJsonPoint(0, 0));
    userProfileTo.setOvertime((short) 15);
    userProfileTo.setRadius((short) 5);
    Response<UserProfileTo> created = template.exchange(gateUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<UserProfileTo>>() {
            }).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getError(), nullValue());
    return userId;
  }

  protected Response<SkillGroupTo> createSkillGroup(SkillGroupTo skillGroupTo) {
    Response<SkillGroupTo> response = template.exchange(gateUrl + "admin/skill-group", HttpMethod.POST,
            createHttpEntity(skillGroupTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillGroupTo>>() {
            }).getBody();
    assertThat(response.getEntity(), notNullValue());
    assertThat(response.getError(), nullValue());
    return response;
  }

  protected SkillGroupTo createSkillGroup() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = createSkillGroup(skillGroupTo);
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getError(), nullValue());
    return created.getEntity();
  }

  protected SkillDetailTo createSkillDetail() {
    SkillGroupTo skillGroupTo = createSkillGroup();
    String skillName = "Testing" + System.currentTimeMillis();
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setDeliveryType(Optional.of(DeliveryType.HOME));
    skillDetailTo.setName(Optional.of(skillName));
    skillDetailTo.setSkillGroupId(skillGroupTo.getId());
    Response<SkillDetailTo> createdSkill = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.POST,
            createHttpEntity(skillDetailTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillDetailTo>>() {
            }).getBody();
    return createdSkill.getEntity();
  }

  protected HttpHeaders createAdminHeader() {
    return createAuthHeader(username, password);
  }
}

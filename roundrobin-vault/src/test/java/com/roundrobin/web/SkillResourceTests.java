package com.roundrobin.web;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import com.roundrobin.api.Error;
import com.roundrobin.api.Response;
import com.roundrobin.vault.api.SkillDetailTo;
import com.roundrobin.vault.api.SkillTo;
import com.roundrobin.vault.api.UserProfileTo;
import com.roundrobin.vault.common.ErrorCode;

/**
 * Created by rengasu on 5/13/16.
 */
public class SkillResourceTests extends ResourceTests {

  @Test
  public void testCreate() {
    Response<UserProfileTo> profile = createUserProfile();
    SkillDetailTo skillDetailTo = createSkillDetail();
    SkillTo skillTo = new SkillTo();
    // skillTo.setUserProfileId(profile.getEntity().getId());
    skillTo.setMinCost(Optional.of(1.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId(skillDetailTo.getId());
    skillTo.setTimeToComplete(Optional.of(10));
    Response<SkillTo> created =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    Response<SkillTo> read = helper.get(url + "skill/{skillId}", new ParameterizedTypeReference<Response<SkillTo>>() {},
        port, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getMaxCost().get(), is(skillTo.getMaxCost().get()));
    assertThat(read.getEntity().getMinCost().get(), is(skillTo.getMinCost().get()));
    assertThat(read.getEntity().getTimeToComplete(), is(skillTo.getTimeToComplete()));
  }

  @Test
  public void testCreateWithInvalidProfileId() {
    SkillDetailTo skillDetailTo = createSkillDetail();
    SkillTo skillTo = new SkillTo();
    // skillTo.setUserProfileId("testing");
    skillTo.setMinCost(Optional.of(1.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId(skillDetailTo.getId());
    skillTo.setTimeToComplete(Optional.of(10));
    Response<String> created =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<String>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_PROFILE_ID, messages.getErrorMessage(ErrorCode.INVALID_PROFILE_ID))));
  }

  @Test
  public void testCreateWithInvalidSkillDetailId() {
    Response<UserProfileTo> profile = createUserProfile();
    SkillTo skillTo = new SkillTo();
    // skillTo.setUserProfileId(profile.getEntity().getId());
    skillTo.setMinCost(Optional.of(1.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId("testing");
    skillTo.setTimeToComplete(Optional.of(10));
    Response<String> created =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<String>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), hasItems(
        new Error(ErrorCode.INVALID_SKILL_DETAIL_ID, messages.getErrorMessage(ErrorCode.INVALID_SKILL_DETAIL_ID))));
  }

  @Test
  public void testCreateWithInvalidTTC() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(-5));
    Response<SkillTo> created =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "timeToComplete: must be between 10 and 600")));
  }

  @Test
  public void testCreateWithEmptyValues() {
    SkillTo skillTo = new SkillTo();
    Response<SkillTo> created =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "skillDetailId: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD, "timeToComplete: may not be null"),
            new Error(ErrorCode.INVALID_FIELD, "userProfileId: may not be empty")));
  }

  @Test
  public void testCreateWithEmptyCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    // skillTo.setUserProfileId("testing");
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "Invalid cost value")));
  }

  @Test
  public void testCreateWithNegativeCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setCost(Optional.of(-10.0));
    // skillTo.setUserProfileId("testing");
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "Invalid cost value")));
  }

  @Test
  public void testCreateWithNoMinCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMaxCost(Optional.of(10.0));
    // skillTo.setUserProfileId("testing");
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "Invalid cost value")));
  }

  @Test
  public void testCreateWithNoMaxCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMinCost(Optional.of(10.0));
    // skillTo.setUserProfileId("testing");
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "Invalid cost value")));
  }

  @Test
  public void testCreateWithNegativeMinCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMinCost(Optional.of(-5.0));
    skillTo.setMaxCost(Optional.of(10.0));
    // skillTo.setUserProfileId("testing");
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "Invalid cost value")));
  }

  @Test
  public void testCreateWithNegativeMaxCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMinCost(Optional.of(5.0));
    skillTo.setMaxCost(Optional.of(-10.0));
    // skillTo.setUserProfileId("testing");
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "Invalid cost value")));
  }

  @Test
  public void testCreateWithInvalidMinMaxCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMinCost(Optional.of(15.0));
    skillTo.setMaxCost(Optional.of(10.0));
    // skillTo.setUserProfileId("testing");
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "Invalid cost value")));
  }

  @Test
  public void testCreateWithBothCostAndRange() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setCost(Optional.of(5.0));
    skillTo.setMinCost(Optional.of(15.0));
    skillTo.setMaxCost(Optional.of(10.0));
    Response<SkillTo> created =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "Invalid cost value")));
  }

  @Test
  public void testDuplicateCreate() {
    Response<UserProfileTo> profile = createUserProfile();
    SkillDetailTo skillDetailTo = createSkillDetail();
    SkillTo skillTo = new SkillTo();
    // skillTo.setUserProfileId(profile.getEntity().getId());
    skillTo.setMinCost(Optional.of(1.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId(skillDetailTo.getId());
    skillTo.setTimeToComplete(Optional.of(10));
    Response<SkillTo> created =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    Response<SkillTo> read = helper.get(url + "skill/{skillId}", new ParameterizedTypeReference<Response<SkillTo>>() {},
        port, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    Response<SkillTo> duplicate =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(duplicate.getEntity(), nullValue());
    assertThat(duplicate.getErrors(),
        hasItems(new Error(ErrorCode.SKILL_ALREADY_EXISTS, messages.getErrorMessage(ErrorCode.SKILL_ALREADY_EXISTS))));
  }

  @Test
  public void testUpdate() {
    Response<UserProfileTo> profile = createUserProfile();
    SkillDetailTo skillDetailTo = createSkillDetail();
    SkillTo skillTo = new SkillTo();
    // skillTo.setUserProfileId(profile.getEntity().getId());
    skillTo.setMinCost(Optional.of(1.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId(skillDetailTo.getId());
    skillTo.setTimeToComplete(Optional.of(10));
    Response<SkillTo> created =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    Response<SkillTo> read = helper.get(url + "skill/{skillId}", new ParameterizedTypeReference<Response<SkillTo>>() {},
        port, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    read.getEntity().setTimeToComplete(Optional.of(50));
    read.getEntity().setMinCost(Optional.of(50.0));
    read.getEntity().setMaxCost(Optional.of(100.0));
    Response<SkillTo> updated = helper
        .put(url + "skill", read.getEntity(), new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(updated.getEntity(), notNullValue());
    read = helper.get(url + "skill/{skillId}", new ParameterizedTypeReference<Response<SkillTo>>() {}, port,
        created.getEntity().getId()).getBody();
    assertThat(read.getEntity().getMaxCost().get(), is(100.0));
    assertThat(read.getEntity().getMinCost().get(), is(50.0));
    assertThat(read.getEntity().getTimeToComplete().get(), is(50));
  }

  @Test
  public void testUpdateWithEmptyValues() {
    Response<String> updated =
        helper.put(url + "skill", new SkillTo(), new ParameterizedTypeReference<Response<String>>() {}, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "Invalid cost value"),
        new Error(ErrorCode.INVALID_FIELD, "id: may not be empty")));
  }

  @Test
  public void testUpdateWithInvalidSkillId() {
    SkillTo skillTo = new SkillTo();
    skillTo.setId("testing");
    skillTo.setMinCost(Optional.of(1.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setTimeToComplete(Optional.of(10));
    Response<String> updated =
        helper.put(url + "skill", skillTo, new ParameterizedTypeReference<Response<String>>() {}, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_SKILL_ID, messages.getErrorMessage(ErrorCode.INVALID_SKILL_ID))));
  }

  @Test
  public void testUpdateWithInvalidTTC() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(5));
    Response<String> updated =
        helper.put(url + "skill", skillTo, new ParameterizedTypeReference<Response<String>>() {}, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "timeToComplete: must be between 10 and 600")));
  }

  @Test
  public void testUpdateWithEmptyCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    // skillTo.setUserProfileId("testing");
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created =
        helper.put(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "Invalid cost value")));
  }

  @Test
  public void testUpdateWithNegativeCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setCost(Optional.of(-10.0));
    // skillTo.setUserProfileId("testing");
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created =
        helper.put(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "Invalid cost value")));
  }

  @Test
  public void testUpdateWithNoMinCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMaxCost(Optional.of(10.0));
    // skillTo.setUserProfileId("testing");
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created =
        helper.put(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "Invalid cost value")));
  }

  @Test
  public void testUpdateWithNoMaxCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMinCost(Optional.of(10.0));
    // skillTo.setUserProfileId("testing");
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created =
        helper.put(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "Invalid cost value")));
  }

  @Test
  public void testUpdateWithNegativeMinCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMinCost(Optional.of(-5.0));
    skillTo.setMaxCost(Optional.of(10.0));
    // skillTo.setUserProfileId("testing");
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created =
        helper.put(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "Invalid cost value")));
  }

  @Test
  public void testUpdateWithNegativeMaxCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMinCost(Optional.of(5.0));
    skillTo.setMaxCost(Optional.of(-10.0));
    // skillTo.setUserProfileId("testing");
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created =
        helper.put(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "Invalid cost value")));
  }

  @Test
  public void testUpdateWithInvalidMinMaxCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMinCost(Optional.of(15.0));
    skillTo.setMaxCost(Optional.of(10.0));
    // skillTo.setUserProfileId("testing");
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created =
        helper.put(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "Invalid cost value")));
  }

  @Test
  public void testUpdateWithBothCostAndRange() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setCost(Optional.of(5.0));
    skillTo.setMinCost(Optional.of(15.0));
    skillTo.setMaxCost(Optional.of(10.0));
    Response<SkillTo> created =
        helper.put(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "Invalid cost value")));
  }

  @Test
  public void testRead() {
    Response<UserProfileTo> profile = createUserProfile();
    SkillDetailTo skillDetailTo = createSkillDetail();
    SkillTo skillTo = new SkillTo();
    // skillTo.setUserProfileId(profile.getEntity().getId());
    skillTo.setMinCost(Optional.of(1.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId(skillDetailTo.getId());
    skillTo.setTimeToComplete(Optional.of(10));
    Response<SkillTo> created =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    Response<SkillTo> read = helper.get(url + "skill/{skillId}", new ParameterizedTypeReference<Response<SkillTo>>() {},
        port, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getMaxCost().get(), is(skillTo.getMaxCost().get()));
    assertThat(read.getEntity().getMinCost().get(), is(skillTo.getMinCost().get()));
    assertThat(read.getEntity().getTimeToComplete(), is(skillTo.getTimeToComplete()));
  }

  @Test
  public void testReadWithEmptyId() {
    Response<SkillTo> read = helper
        .get(url + "skill/{skillId}", new ParameterizedTypeReference<Response<SkillTo>>() {}, port, " ").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_URL, messages.getErrorMessage(ErrorCode.INVALID_URL))));
  }

  @Test
  public void testReadWithInvalidId() {
    Response<SkillTo> read =
        helper.get(url + "skill/{skillId}", new ParameterizedTypeReference<Response<SkillTo>>() {}, port, "testing")
            .getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_SKILL_ID, messages.getErrorMessage(ErrorCode.INVALID_SKILL_ID))));
  }

  @Test
  public void testDelete() {
    Response<UserProfileTo> profile = createUserProfile();
    SkillDetailTo skillDetailTo = createSkillDetail();
    SkillTo skillTo = new SkillTo();
    // skillTo.setUserProfileId(profile.getEntity().getId());
    skillTo.setMinCost(Optional.of(1.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId(skillDetailTo.getId());
    skillTo.setTimeToComplete(Optional.of(10));
    Response<SkillTo> created =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    Response<SkillTo> read = helper.get(url + "skill/{skillId}", new ParameterizedTypeReference<Response<SkillTo>>() {},
        port, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    Response<Boolean> deleted = helper.delete(url + "skill/{skillId}",
        new ParameterizedTypeReference<Response<Boolean>>() {}, port, created.getEntity().getId()).getBody();
    assertThat(deleted.getEntity(), notNullValue());
    assertThat(deleted.getEntity(), is(true));
    read = helper.get(url + "skill/{skillId}", new ParameterizedTypeReference<Response<SkillTo>>() {}, port,
        created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_SKILL_ID, messages.getErrorMessage(ErrorCode.INVALID_SKILL_ID))));
  }

  @Test
  public void testDeleteWithEmptyId() {
    Response<SkillTo> read = helper
        .delete(url + "skill/{skillId}", new ParameterizedTypeReference<Response<SkillTo>>() {}, port, " ").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_URL, messages.getErrorMessage(ErrorCode.INVALID_URL))));
  }

  @Test
  public void testDeleteWithInvalidId() {
    Response<SkillTo> read =
        helper.delete(url + "skill/{skillId}", new ParameterizedTypeReference<Response<SkillTo>>() {}, port, "testing")
            .getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_SKILL_ID, messages.getErrorMessage(ErrorCode.INVALID_SKILL_ID))));
  }

  @Test
  public void testList() {
    Response<UserProfileTo> profile = createUserProfile();
    SkillDetailTo skillDetailTo = createSkillDetail();
    SkillTo skillTo = new SkillTo();
    // skillTo.setUserProfileId(profile.getEntity().getId());
    skillTo.setMinCost(Optional.of(1.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId(skillDetailTo.getId());
    skillTo.setTimeToComplete(Optional.of(10));
    Response<SkillTo> created =
        helper.post(url + "skill", skillTo, new ParameterizedTypeReference<Response<SkillTo>>() {}, port).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<List<SkillTo>> list = helper.get(url + "skill?profileId={profileId}",
        new ParameterizedTypeReference<Response<List<SkillTo>>>() {}, port, null).getBody();// profile.getEntity().getId()).getBody();
    assertThat(list.getEntity(), notNullValue());
    assertThat(list.getEntity().size(), is(1));
  }

  @Test
  public void testListWithEmptyId() {
    Response<List<SkillTo>> list =
        helper.get(url + "skill", new ParameterizedTypeReference<Response<List<SkillTo>>>() {}, port).getBody();
    assertThat(list.getEntity(), nullValue());
    assertThat(list.getEntity(), nullValue());
    assertThat(list.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_URL, messages.getErrorMessage(ErrorCode.INVALID_URL))));
  }

  @Test
  public void testListWithEmptyProfileId() {
    Response<List<SkillTo>> list = helper.get(url + "skill?profileId={profileId}",
        new ParameterizedTypeReference<Response<List<SkillTo>>>() {}, port, "").getBody();
    assertThat(list.getEntity(), nullValue());
    assertThat(list.getEntity(), nullValue());
    assertThat(list.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_PROFILE_ID, messages.getErrorMessage(ErrorCode.INVALID_PROFILE_ID))));
  }

  @Test
  public void testListWithInvalidProfileId() {
    Response<List<SkillTo>> list = helper.get(url + "skill?profileId={profileId}",
        new ParameterizedTypeReference<Response<List<SkillTo>>>() {}, port, "testing").getBody();
    assertThat(list.getEntity(), nullValue());
    assertThat(list.getEntity(), nullValue());
    assertThat(list.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_PROFILE_ID, messages.getErrorMessage(ErrorCode.INVALID_PROFILE_ID))));
  }
}

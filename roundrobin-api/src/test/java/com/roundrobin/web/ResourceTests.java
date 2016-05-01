package com.roundrobin.web;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.roundrobin.RoundRobin;
import com.roundrobin.api.Response;
import com.roundrobin.api.SkillGroupTo;
import com.roundrobin.common.ClientMessages;
import com.roundrobin.common.TestHttpHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(RoundRobin.class)
@WebIntegrationTest(randomPort = true)
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
        .post(url + "skill-group", skillGroupTo, new ParameterizedTypeReference<Response<SkillGroupTo>>() {}, port)
        .getBody();
  }
}

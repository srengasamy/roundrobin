package com.roundrobin.test.helper;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.roundrobin.core.helper.HttpHelper;

@Component
@Scope("singleton")
public class TestHttpHelper extends HttpHelper {

  public TestHttpHelper() {
    super(new TestRestTemplate().getRestTemplate());
  }
}

package com.roundrobin.helper;

import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.roundrobin.helper.HttpHelper;

@Component
@Scope("singleton")
public class TestHttpHelper extends HttpHelper {

  public TestHttpHelper() {
    super(new TestRestTemplate());
  }
}

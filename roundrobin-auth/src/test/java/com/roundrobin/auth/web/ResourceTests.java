package com.roundrobin.auth.web;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.roundrobin.auth.RoundRobin;
import com.roundrobin.common.AbstractResourceTests;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(RoundRobin.class)
@WebIntegrationTest(value = "server.port:8080")
@Ignore
public class ResourceTests extends AbstractResourceTests {
}

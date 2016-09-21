package com.roundrobin.auth.web;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.roundrobin.auth.RoundRobin;
import com.roundrobin.test.common.AbstractResourceTests;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RoundRobin.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@Ignore
public class ResourceTests extends AbstractResourceTests {
}

package com.lofidewanto.demo.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.lofidewanto.demo.server.DemoGwtSpringbootApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoGwtSpringbootApplication.class)
@WebAppConfiguration
public class DemoGwtSpringbootApplicationTests {

	@Test
	public void contextLoads() {
	}

}

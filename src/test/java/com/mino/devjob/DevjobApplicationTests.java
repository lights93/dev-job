package com.mino.devjob;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
	properties = {"spring.config.location=classpath:application-oauth.properties"}
)
class DevjobApplicationTests {

	@Test
	void contextLoads() {
	}

}

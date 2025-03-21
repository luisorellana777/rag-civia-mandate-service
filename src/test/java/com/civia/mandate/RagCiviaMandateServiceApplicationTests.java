package com.civia.mandate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RagCiviaMandateServiceApplicationTests {

	static {
		System.setProperty("DB_CLUSTER", "KEY");
		System.setProperty("DB_KEY", "KEY");
		System.setProperty("DB_USER", "KEY");
		System.setProperty("DB_NAME", "KEY");
		System.setProperty("GEMINI-KEY", "KEY");
	}
	@Test
	void contextLoads() {
	}

}

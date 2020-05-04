package com.test.spring.activiti.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.test.spring.activiti.integration",
		exclude = IntegrationAutoConfiguration.class)
public class SpringActivitiApp {

	public static void main(String[] args) {
		SpringApplication.run(SpringActivitiApp.class, args);
	}

}

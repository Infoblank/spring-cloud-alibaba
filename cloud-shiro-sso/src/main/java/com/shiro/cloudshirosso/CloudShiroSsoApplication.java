package com.shiro.cloudshirosso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //激活jpa审计
public class CloudShiroSsoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudShiroSsoApplication.class, args);
	}

}

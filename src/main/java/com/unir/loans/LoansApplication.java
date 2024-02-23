package com.unir.loans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class LoansApplication {

	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() { return new RestTemplate();}

	public static void main(String[] args) {

		String profile = System.getenv("PROFILE");
		System.setProperty("spring.profiles.active", profile != null ? profile : "default");
		SpringApplication.run(LoansApplication.class, args);
	}

}

package com.prgrms.bdbks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan(basePackages = {"com.prgrms.bdbks.config.jwt"})
@SpringBootApplication
public class BlackDogBucksApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlackDogBucksApplication.class, args);
	}

}

package com.prgrms.bdbks.config.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.prgrms.bdbks.domain.item.entity.OptionPrice;

@Configuration
public class JdbcConfig {

	@Bean
	public OptionPrice optionPrice() {
		return new OptionPrice();
	}
	
}

package com.prgrms.bdbks.config.web;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.prgrms.bdbks.config.jwt.JwtConfigure;

@ConfigurationPropertiesScan(value = {"com.prgrms.bdbks.config"}, basePackageClasses = {JwtConfigure.class})
@EnableConfigurationProperties
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOriginPatterns("http://localhost:8080/", "http://localhost:3000", "/**")
			.allowedMethods("GET", "POST", "DELETE", "PATCH", "PUT", "OPTION")
			.allowCredentials(true);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/docs/**").addResourceLocations("classpath:/static/docs/");
	}

}

package com.prgrms.bdbks.domain.user.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@Getter
@ConfigurationProperties(prefix = "jwt")
@ConstructorBinding
public class JwtConfigure {

	private final String header;

	private final String secret;

	private final long tokenValidityInSeconds;
}

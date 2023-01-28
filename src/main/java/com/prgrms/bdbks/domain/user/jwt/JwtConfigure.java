package com.prgrms.bdbks.domain.user.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@ConstructorBinding
@RequiredArgsConstructor
public class JwtConfigure {

	private final String authoritiesKey;

	private final String header;

	private final String secret;

	private final long tokenValidityInSeconds;

	public String getAuthoritiesKey() {
		return authoritiesKey;
	}

	public String getHeader() {
		return header;
	}

	public String getSecret() {
		return secret;
	}

	public long getTokenValidityInSeconds(long seconds) {
		return tokenValidityInSeconds * seconds;
	}

	public long getTokenValidityInSeconds() {
		return tokenValidityInSeconds * 1000;
	}
}

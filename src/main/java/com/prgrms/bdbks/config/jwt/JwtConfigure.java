package com.prgrms.bdbks.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.AllArgsConstructor;
import lombok.Getter;

@ConfigurationProperties(prefix = "jwt")
@ConstructorBinding
@Getter
@AllArgsConstructor
public class JwtConfigure {

	private final String authoritiesKey;

	private final String header;

	private final String secret;

	private final long tokenValidityInSeconds;

	public long getTokenValidityInSeconds(long seconds) {
		return tokenValidityInSeconds * seconds;
	}

}
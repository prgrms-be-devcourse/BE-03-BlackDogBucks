package com.prgrms.bdbks.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.Getter;

@ConfigurationProperties(prefix = "jwt")
@ConstructorBinding
@Getter
public class JwtConfigure {

	private final String authoritiesKey;

	private final String header;

	private final String secret;

	private final long tokenValidityInSeconds;

	public long getTokenValidityInSeconds(long seconds) {
		return tokenValidityInSeconds * seconds;
	}

	public JwtConfigure(String authoritiesKey, String header, String secret, long tokenValidityInSeconds) {
		this.authoritiesKey = authoritiesKey;
		this.header = header;
		this.secret = secret;
		this.tokenValidityInSeconds = tokenValidityInSeconds;
	}
}
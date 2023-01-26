package com.prgrms.bdbks.domain.user.jwt;

import static com.google.common.base.Preconditions.*;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.prgrms.bdbks.common.exception.AuthorityNotFoundException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TokenProvider {
	private static final String AUTHORITIES_KEY = "auth";
	private final Key key;
	private final long tokenValidityInSeconds;

	public TokenProvider(@Value("${jwt.secret}") String secretKey,
		@Value("${token-validity-in-seconds}") long tokenValidityInSeconds) {

		checkNotNull(secretKey, "비밀키는 null일 수 없습니다.");
		checkArgument(tokenValidityInSeconds > 0, "유효시간은 0보다 커야 합니다.");

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);

		this.tokenValidityInSeconds = tokenValidityInSeconds * 1000;
	}

	public String generateToken(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		Date validity = new Date(new Date().getTime() + tokenValidityInSeconds);

		return Jwts.builder()
			.setSubject(authentication.getName())
			.claim(AUTHORITIES_KEY, authorities)
			.signWith(key, SignatureAlgorithm.HS512)
			.setExpiration(validity)
			.compact();
	}

	public Authentication getAuthentication(String accessToken) {
		Claims claims = Jwts
			.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(accessToken)
			.getBody();

		if (claims.get(AUTHORITIES_KEY) == null) {

			throw new AuthorityNotFoundException("클레임에 권한정보가 존재하지 않습니다..");

		} else {
			Collection<? extends GrantedAuthority> authorities =
				Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());

			User principal = new User(claims.getSubject(), "", authorities);
			return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
		}
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;

		} catch (SecurityException | MalformedJwtException e) {
			log.info("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			log.info("JWT 토큰이 빈 값입니다.");
		}
		return false;
	}
}

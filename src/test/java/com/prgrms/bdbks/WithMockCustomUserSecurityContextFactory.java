package com.prgrms.bdbks;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
import com.prgrms.bdbks.domain.user.dto.TokenResponse;
import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.dto.UserLoginRequest;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.jwt.CustomUserDetails;
import com.prgrms.bdbks.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

	public static String mockUserToken;

	private final UserService userService;

	@Override
	public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
		final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

		UserCreateRequest createRequest = UserObjectProvider.createMockUserRequest(annotation.username());
		User savedUser = userService.register(createRequest);

		UserLoginRequest loginRequest = new UserLoginRequest(createRequest.getLoginId(), createRequest.getPassword());

		TokenResponse tokenResponse = userService.login(loginRequest);

		mockUserToken = tokenResponse.getToken();

		CustomUserDetails customUserDetails = new CustomUserDetails(savedUser);

		final UsernamePasswordAuthenticationToken authenticationToken
			= new UsernamePasswordAuthenticationToken(customUserDetails, null,
			List.of(new SimpleGrantedAuthority(annotation.role())));
		securityContext.setAuthentication(authenticationToken);

		return securityContext;
	}
}

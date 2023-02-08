package com.prgrms.bdbks.domain.user.api;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.bdbks.domain.user.converter.UserMapper;
import com.prgrms.bdbks.domain.user.dto.StoreUserChangeRequest;
import com.prgrms.bdbks.domain.user.dto.UserFindResponse;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.jwt.CustomUserDetails;
import com.prgrms.bdbks.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/users")
@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	private final UserMapper userMapper;

	/**
	 * <pre>
	 *     유저 정보 loginId로 조회
	 * </pre>
	 * @param loginId - 조회할 회원의 loginId
	 * @return status : ok , body : UserFindResponse
	 */
	@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'STORE_MANAGER')")
	@GetMapping(value = "/{loginId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserFindResponse> findUser(@PathVariable String loginId) {
		Optional<User> user = this.userService.findUser(loginId);
		return user.map(value -> ResponseEntity.ok(userMapper.entityToFindResponse(value)))
			.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	/**
	 * <pre>
	 *     본인 정보 조회
	 * </pre>
	 * @param user
	 * @return status : ok , body : String
	 */
	@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'STORE_MANAGER')")
	@GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserFindResponse> getMe(@AuthenticationPrincipal CustomUserDetails user) {
		return ResponseEntity.ok(userMapper.entityToFindResponse(user.getUser()));
	}

	/**
	 * <pre>
	 *     유저 권한을 매장 관리자로 변경
	 *     			OR
	 *     매장 관리자의 매장 정보 변경
	 * </pre>
	 * @param storeUserChangeRequest - 변경할 회원정보
	 * @return status : ok , body : String
	 */
	@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'STORE_MANAGER')")
	@PatchMapping(value = "/store", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> changeUserStoreInformation(
		@Valid @RequestBody StoreUserChangeRequest storeUserChangeRequest) {
		userService.changeStoreUser(storeUserChangeRequest);
		return ResponseEntity.ok("User's Store Information Modified");
	}
}
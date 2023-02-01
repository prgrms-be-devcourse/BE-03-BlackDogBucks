package com.prgrms.bdbks.domain.user.service;

import java.util.Optional;

import com.prgrms.bdbks.domain.user.dto.TokenResponse;
import com.prgrms.bdbks.domain.user.dto.UserAuthChangeRequest;
import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.dto.UserLoginRequest;
import com.prgrms.bdbks.domain.user.entity.User;

public interface UserService {

	User register(UserCreateRequest userCreateRequest);

	Optional<User> findUser(String loginId);

	TokenResponse login(UserLoginRequest userLoginRequest);

	User findUserById(Long id);

	boolean hasStore(Long id, String storeId);

	void changeUserAuthority(UserAuthChangeRequest userAuthChangeRequest);
}

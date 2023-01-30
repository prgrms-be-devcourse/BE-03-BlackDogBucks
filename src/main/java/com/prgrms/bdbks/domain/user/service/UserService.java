package com.prgrms.bdbks.domain.user.service;

import java.util.Optional;

import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.entity.User;

public interface UserService {

	User register(UserCreateRequest userCreateRequest);

	Optional<User> findUser(String loginId);

	Optional<User> login(String loginId, String password);

	User findUserById(Long id);
}

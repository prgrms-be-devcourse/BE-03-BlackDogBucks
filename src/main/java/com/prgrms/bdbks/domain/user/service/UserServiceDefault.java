package com.prgrms.bdbks.domain.user.service;

import com.prgrms.bdbks.domain.user.converter.UserMapper;
import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.repository.UserRepository;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceDefault implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;

	public UserServiceDefault(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userMapper = userMapper;
	}

	@Override
	public User register(UserCreateRequest userCreateRequest) {
		User user = userMapper.createRequestToEntity(userCreateRequest);
		user.changePassword(this.passwordEncoder.encode(user.getPassword()));
		return this.userRepository.save(user);
	}

	@Override
	public Optional<User> findUser(String loginId) {
		return this.userRepository.findByLoginId(loginId);
	}

	@Override
	public Optional<User> login(String loginId, String password) {
		Optional<User> user = this.findUser(loginId);
		if (user.isPresent()) {
			String encodedPassword = user.get().getPassword();
			if (this.passwordEncoder.matches(password, encodedPassword)) {
				return this.userRepository.findByLoginIdAndPassword(loginId, encodedPassword);
			}
			return Optional.empty();
		}
		return Optional.empty();
	}
}
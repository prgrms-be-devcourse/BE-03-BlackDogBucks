package com.prgrms.bdbks.domain.user.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prgrms.bdbks.common.exception.DuplicateInsertException;
import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.user.converter.UserMapper;
import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.entity.UserAdapter;
import com.prgrms.bdbks.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final UserMapper userMapper;

	@Override
	@Transactional
	public User register(UserCreateRequest userCreateRequest) {
		User user = userMapper.createRequestToEntity(userCreateRequest);
		user.changePassword(this.passwordEncoder.encode(user.getPassword()));
		if (!this.userRepository.existsByLoginId(user.getLoginId())) {
			return new UserAdapter(this.userRepository.save(user));
		} else {
			throw new DuplicateInsertException(String.format("이미 등록된 사용자 아이디 입니다. (%s)", user.getLoginId()));
		}
	}

	@Override
	public Optional<User> findUser(String loginId) {
		Optional<User> user = this.userRepository.findByLoginId(loginId);
		return user.map(UserAdapter::new);
	}

	@Override
	@Transactional
	public Optional<User> login(String loginId, String password) {
		Optional<User> user = this.userRepository.findByLoginId(loginId);
		if (user.isPresent()) {
			String encodedPassword = user.get().getPassword();
			if (this.passwordEncoder.matches(password, encodedPassword)) {
				return this.userRepository.findByLoginIdAndPassword(loginId, encodedPassword).map(UserAdapter::new);
			}
			return Optional.empty();
		}
		return Optional.empty();
	}

	@Override
	@Transactional
	public User findUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, userId));
	}
}
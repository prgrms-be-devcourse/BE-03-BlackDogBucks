package com.prgrms.bdbks.domain.user.service;

import static com.prgrms.bdbks.domain.user.role.Role.*;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prgrms.bdbks.common.exception.DuplicateInsertException;
import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.user.converter.UserMapper;
import com.prgrms.bdbks.domain.user.dto.TokenResponse;
import com.prgrms.bdbks.domain.user.dto.UserAuthChangeRequest;
import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.dto.UserLoginRequest;
import com.prgrms.bdbks.domain.user.entity.Authority;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.entity.UserAdapter;
import com.prgrms.bdbks.domain.user.entity.UserAuthority;
import com.prgrms.bdbks.domain.user.jwt.TokenProvider;
import com.prgrms.bdbks.domain.user.repository.AuthorityRepository;
import com.prgrms.bdbks.domain.user.repository.UserAuthorityRepository;
import com.prgrms.bdbks.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final UserMapper userMapper;

	private final UserAuthorityRepository userAuthorityRepository;

	private final AuthorityRepository authorityRepository;

	private final TokenProvider tokenProvider;

	@Override
	@Transactional
	public User register(UserCreateRequest userCreateRequest) {

		User user = userMapper.createRequestToEntity(userCreateRequest);

		if (userRepository.existsByLoginId(user.getLoginId())) {
			throw new DuplicateInsertException(User.class, user.getLoginId());
		}

		user.changePassword(this.passwordEncoder.encode(user.getPassword()));

		Authority authority = authorityRepository.findById(ROLE_USER)
			.orElse(authorityRepository.save(new Authority(ROLE_USER)));

		UserAuthority userAuthority = UserAuthority.create(user, authority);

		userAuthorityRepository.save(userAuthority);

		return userRepository.save(user);
	}

	@Override
	public Optional<User> findUser(String loginId) {
		Optional<User> user = this.userRepository.findByLoginId(loginId);
		return user.map(UserAdapter::new);
	}

	@Override
	@Transactional
	public TokenResponse login(UserLoginRequest userLoginRequest) {
		User user = this.userRepository.findByLoginId(userLoginRequest.getLoginId())
			.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

		user.checkPassword(passwordEncoder, userLoginRequest.getPassword());

		String token = tokenProvider.generateToken(user);

		return new TokenResponse(token);
	}

	@Override
	@Transactional
	public User findUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, userId));
	}

	@Override
	public boolean hasStore(Long userId, String storeId) {
		User user = findUserById(userId);
		user.validateStore(storeId);
		return true;
	}

	@Override
	@Transactional
	public void changeUserAuthority(UserAuthChangeRequest userAuthChangeRequest) {

		User user = this.userRepository.findByLoginId(userAuthChangeRequest.getLoginId())
			.orElseThrow(() -> new EntityNotFoundException(User.class, userAuthChangeRequest.getLoginId()));

		Authority authority = authorityRepository.findById(userAuthChangeRequest.getRole())
			.orElseThrow(() -> new EntityNotFoundException(Authority.class, userAuthChangeRequest.getRole()));

		UserAuthority userAuthority = UserAuthority.create(user, authority);

		userAuthorityRepository.save(userAuthority);
	}
}
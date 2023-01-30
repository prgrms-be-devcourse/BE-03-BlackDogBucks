package com.prgrms.bdbks.domain.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.prgrms.bdbks.domain.user.jwt.CustomUserDetails;
import com.prgrms.bdbks.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new CustomUserDetails(
			userRepository.findByLoginIdWithAuthorities(username)
				.orElseThrow(() -> new UsernameNotFoundException("해당하는 Id의 사용자를 찾을 수 없습니다.")));
	}
}

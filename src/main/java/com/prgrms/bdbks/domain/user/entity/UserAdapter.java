package com.prgrms.bdbks.domain.user.entity;

import com.prgrms.bdbks.domain.user.role.Role;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAdapter extends User {

	String loginId;

	String nickname;

	String password;

	String email;

	Role role;

	public UserAdapter(User user) {

		super(user.getId(),
			user.getLoginId(),
			user.getPassword(),
			user.getNickname(),
			user.getBirthDate(),
			user.getPhone(),
			user.getPhone(),
			user.getRole());

		this.loginId = user.getLoginId();
		this.nickname = user.getNickname();
		this.password = user.getPassword();
		this.email = user.getEmail();
		this.role = user.getRole();
	}
}

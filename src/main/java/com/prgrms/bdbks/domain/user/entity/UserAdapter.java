package com.prgrms.bdbks.domain.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAdapter extends User {

	String loginId;

	String nickname;

	String password;

	String email;

	public UserAdapter(User user) {

		super(user.getId(),
			user.getLoginId(),
			user.getPassword(),
			user.getNickname(),
			user.getBirthDate(),
			user.getPhone(),
			user.getPhone());

		this.loginId = user.getLoginId();
		this.nickname = user.getNickname();
		this.password = user.getPassword();
		this.email = user.getEmail();
	}
}

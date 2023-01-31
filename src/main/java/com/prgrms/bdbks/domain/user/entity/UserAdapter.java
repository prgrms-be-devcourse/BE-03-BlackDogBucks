package com.prgrms.bdbks.domain.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAdapter extends User {

	private String loginId;

	private String nickname;

	private String password;

	private String email;

	public UserAdapter(User user) {

		super(user.getId(),
			user.getLoginId(),
			user.getPassword(),
			user.getNickname(),
			user.getBirthDate(),
			user.getPhone(),
			user.getEmail());

		this.loginId = user.getLoginId();
		this.nickname = user.getNickname();
		this.password = user.getPassword();
		this.email = user.getEmail();
	}
}

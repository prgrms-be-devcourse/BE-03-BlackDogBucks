package com.prgrms.bdbks.domain.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.prgrms.bdbks.domain.user.role.Role;

import lombok.Getter;

@Getter
public class UserAuthChangeRequest {

	@NotBlank(message = "로그인 ID는 필수 입력사항입니다.")
	private String loginId;

	@NotNull(message = "유저의 역할값은 필수 입력사항입니다.")
	private Role role;
}

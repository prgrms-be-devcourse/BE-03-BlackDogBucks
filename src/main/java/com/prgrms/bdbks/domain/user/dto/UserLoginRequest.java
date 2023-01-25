package com.prgrms.bdbks.domain.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {

	@NotBlank(message = "아이디는 필수 입력사항입니다.")
	@Size(min = 6, max = 20, message = "아이디의 길이는 6 ~ 20 글자까지 가능합니다.")
	private String loginId;

	@NotBlank(message = "비밀번호는 필수 입력사항입니다.")
	@Size(min = 8, max = 20, message = "비밀번호의 길이는 8 ~ 20 글자까지 가능합니다.")
	private String password;
}

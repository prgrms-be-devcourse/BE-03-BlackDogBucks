package com.prgrms.bdbks.domain.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class UserLogoutRequest {

	@NotBlank(message = "아이디는 필수 입력사항입니다.")
	@Size(min = 6, max = 20, message = "아이디의 길이는 6 ~ 20 글자까지 가능합니다.")
	private String loginId;
}

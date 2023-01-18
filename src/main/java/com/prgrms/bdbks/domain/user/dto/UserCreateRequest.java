package com.prgrms.bdbks.domain.user.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class UserCreateRequest {

	@NotBlank(message = "아이디는 필수 입력사항입니다.")
	@Size(min = 6, max = 20, message = "아이디의 길이는 6 ~ 20 글자까지 가능합니다.")
	private String loginId;

	@NotBlank(message = "비밀번호는 필수 입력사항입니다.")
	@Size(min = 8, max = 20, message = "비밀번호의 길이는 8 ~ 20 글자까지 가능합니다.")
	private String password;

	@NotBlank(message = "닉네임은 필수 입력사항입니다.")
	@Size(min = 4, max = 20, message = "닉네임의 길이는 4 ~ 20 글자까지 가능합니다.")
	private String nickname;

	@NotNull(message = "생년월일은 필수 입력사항입니다.")
	private LocalDateTime birthDate;

	@NotBlank(message = "핸드폰 번호는 필수 입력사항입니다.")
	private String phone;

	@NotNull(message = "이메일은 필수 입력사항입니다.")
	@Email(message = "이메일 형식을 확인해주세요.")
	private String email;
}

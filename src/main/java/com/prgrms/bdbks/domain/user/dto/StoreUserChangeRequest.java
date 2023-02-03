package com.prgrms.bdbks.domain.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.prgrms.bdbks.domain.user.role.Role;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class StoreUserChangeRequest {

	@NotBlank(message = "로그인 ID는 필수 입력사항입니다.")
	private String loginId;

	@NotNull(message = "변경할 권한은 필수 입력사항입니다.")
	private Role role;

	@NotBlank(message = "가게 ID는 필수 입력사항입니다.")
	private String storeId;
}

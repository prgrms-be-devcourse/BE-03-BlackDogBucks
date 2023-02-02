package com.prgrms.bdbks.domain.user.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserFindResponse {

	private String loginId;

	private String nickname;

	private LocalDate birthDate;

	private String phone;

	private String email;

}

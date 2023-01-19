package com.prgrms.bdbks.domain.user.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserFindResponse {

	private String loginId;

	private String nickname;

	private LocalDateTime birthDate;

	private String phone;

	private String email;

	private String role;
}

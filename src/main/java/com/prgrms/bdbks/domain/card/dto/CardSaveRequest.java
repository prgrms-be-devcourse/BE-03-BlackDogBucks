package com.prgrms.bdbks.domain.card.dto;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CardSaveRequest {

	@NotBlank(message = "카드 이름은 필수 입력사항입니다.")
	private String name;
}
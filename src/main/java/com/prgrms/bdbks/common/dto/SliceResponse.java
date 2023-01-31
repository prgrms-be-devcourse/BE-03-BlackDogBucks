package com.prgrms.bdbks.common.dto;

import java.util.List;

import org.springframework.data.domain.Slice;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SliceResponse<T> {

	private List<T> data;

	private Boolean hasNext;

	private int size;

	public SliceResponse(Slice<T> slice) {
		this.data = slice.getContent();
		this.hasNext = slice.hasNext();
		this.size = slice.getNumberOfElements();
	}

}

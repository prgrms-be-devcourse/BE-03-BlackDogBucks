package com.prgrms.bdbks.domain.order.exception;

import com.prgrms.bdbks.common.exception.BusinessException;

public class AlreadyProgressOrderException extends BusinessException {

	public AlreadyProgressOrderException(String message) {
		super(message);
	}

}

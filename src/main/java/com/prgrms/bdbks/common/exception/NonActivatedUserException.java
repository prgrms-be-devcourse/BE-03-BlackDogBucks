package com.prgrms.bdbks.common.exception;

public class NonActivatedUserException extends BusinessException {
	public NonActivatedUserException() {
	}

	public NonActivatedUserException(String message) {
		super("활성화되어 있지 않은 사용자입니다. :" + message);
	}

	public NonActivatedUserException(String message, Throwable cause) {
		super(message, cause);
	}
}

package com.prgrms.bdbks.common.exception;

public class DuplicateException extends BusinessException {
	public DuplicateException() {
	}

	public DuplicateException(String message) {
		super(message);
	}

	public DuplicateException(String message, Throwable cause) {
		super(message, cause);
	}

}

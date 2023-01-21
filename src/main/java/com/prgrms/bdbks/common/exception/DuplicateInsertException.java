package com.prgrms.bdbks.common.exception;

public class DuplicateInsertException extends DuplicateException {

	public DuplicateInsertException(String message) {
		super(message);
	}

	public DuplicateInsertException(String message, Throwable cause) {
		super(message, cause);
	}

}

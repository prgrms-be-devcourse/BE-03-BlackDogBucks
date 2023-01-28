package com.prgrms.bdbks.common.exception;

public class AuthorityNotFoundException extends BusinessException {
	protected AuthorityNotFoundException() {
		super();
	}

	public AuthorityNotFoundException(String message) {
		super(message);
	}
}

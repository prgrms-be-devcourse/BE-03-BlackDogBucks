package com.prgrms.bdbks.common.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtValidateException extends AuthenticationException {

	public JwtValidateException(String message) {
		super(message);
	}

}

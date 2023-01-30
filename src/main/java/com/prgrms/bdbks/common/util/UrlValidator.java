package com.prgrms.bdbks.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrlValidator {

	private static final org.apache.commons.validator.routines.UrlValidator validator =
		new org.apache.commons.validator.routines.UrlValidator();

	public static boolean isValid(String url) {
		return validator.isValid(url);
	}

}

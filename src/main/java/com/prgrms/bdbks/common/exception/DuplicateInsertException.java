package com.prgrms.bdbks.common.exception;

import java.util.Arrays;

public class DuplicateInsertException extends DuplicateException {

	public DuplicateInsertException(String message) {
		super(message);
	}

	public DuplicateInsertException(Class<?> entity, Object property) {
		super(String.format("[%s] 중복된 값 입니다. by property : %s", entity.getSimpleName(), property.toString()));
	}

	public DuplicateInsertException(Class<?> entity, Object... properties) {
		super(String.format("[%s] 중복된 값 입니다. by property : %s", entity.getSimpleName(),
			String.join(", ", Arrays.stream(properties).map(Object::toString).toArray(String[]::new))));
	}

}

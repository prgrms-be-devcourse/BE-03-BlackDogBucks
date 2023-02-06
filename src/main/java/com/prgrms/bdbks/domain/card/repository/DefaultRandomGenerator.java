package com.prgrms.bdbks.domain.card.repository;

import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.IntStream;

public class DefaultRandomGenerator implements RandomNumberGenerator {

	private final Random random = new SecureRandom();

	@Override
	public int randomNumber() {

		return IntStream.range(0, 4)
			.map(i -> random.nextInt(8) + 1)
			.reduce(0, (left, right) -> left * 10 + right);
	}

}

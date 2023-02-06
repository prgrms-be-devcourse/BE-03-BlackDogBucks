package com.prgrms.bdbks.domain.card.repository;

import java.util.Random;

public class DefaultRandomGenerator implements RandomNumberGenerator {

	@Override
	public int getRandom() {
		int num = 0;
		Random random = new Random();

		for (int i = 0; i < 4; i++) {
			int randomNumber = random.nextInt(9);
			num = num * 10 + randomNumber;
		}

		return num;
	}
}

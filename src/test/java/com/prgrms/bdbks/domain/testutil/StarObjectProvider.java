package com.prgrms.bdbks.domain.testutil;

import com.prgrms.bdbks.domain.star.entity.Star;
import com.prgrms.bdbks.domain.user.entity.User;

public class StarObjectProvider {

	public static Star createStar(User user, short count) {

		return Star.builder()
			.user(user)
			.count(count)
			.build();
	}

}
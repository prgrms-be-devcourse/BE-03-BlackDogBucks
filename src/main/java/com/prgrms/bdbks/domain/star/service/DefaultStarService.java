package com.prgrms.bdbks.domain.star.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.star.entity.Star;
import com.prgrms.bdbks.domain.star.repository.StarRepository;
import com.prgrms.bdbks.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultStarService implements StarService {

	private static final short ZERO = 0;

	private final StarRepository starRepository;

	@Override
	@Transactional
	public Long create(User user) {
		Star star = Star.builder()
			.user(user)
			.count(ZERO)
			.build();

		starRepository.save(star);
		return star.getId();
	}

	@Override
	public Star findByUserId(Long userId) {
		return starRepository.findByUserId(userId)
			.orElseThrow(() -> new EntityNotFoundException(Star.class, userId));
	}

	@Override
	@Transactional
	public void delete(Long userId) {
		starRepository.deleteByUserId(userId);
	}

	@Override
	@Transactional
	public void updateCount(Long userId, int totalCount) {
		findByUserId(userId).updateCount(totalCount);
	}

	//TODO 거래 취소, 반품 시 별은 원상복구(원래 상태로 감소)
}
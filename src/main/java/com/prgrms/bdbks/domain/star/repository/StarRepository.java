package com.prgrms.bdbks.domain.star.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.bdbks.domain.star.entity.Star;

public interface StarRepository extends JpaRepository<Star, Long> {
	Optional<Star> findByUserId(Long userId);

	void deleteByUserId(Long userId);

}

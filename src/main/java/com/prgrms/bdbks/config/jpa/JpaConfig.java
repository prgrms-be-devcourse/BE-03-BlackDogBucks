package com.prgrms.bdbks.config.jpa;

import javax.persistence.EntityManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.prgrms.bdbks.domain.card.repository.DefaultRandomGenerator;
import com.prgrms.bdbks.domain.card.repository.RandomNumberGenerator;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Configuration
// @EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware") SpringSecurityAuditorAware 구현 완료시 적용해야한다.
@EnableJpaAuditing
public class JpaConfig {

	@Bean
	public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
		return new JPAQueryFactory(entityManager);
	}

	@Bean
	public RandomNumberGenerator randomGenerator() {
		return new DefaultRandomGenerator();
	}

}

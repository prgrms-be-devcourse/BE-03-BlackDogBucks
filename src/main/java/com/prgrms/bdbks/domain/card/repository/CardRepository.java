package com.prgrms.bdbks.domain.card.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.prgrms.bdbks.domain.card.entity.Card;

public interface CardRepository extends JpaRepository<Card, String> {
	List<Card> findByUserId(@Param("userId") Long id);
}
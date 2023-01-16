package com.prgrms.bdbks.domain.card.entity;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.prgrms.bdbks.common.domain.AbstractTimeColumn;

import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cards")
@NoArgsConstructor(access = PROTECTED)
public class Card extends AbstractTimeColumn {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "card_id")
	private Long id;

	@Column(nullable = false)
	private int amount;

	@OneToOne(fetch = LAZY)
	private User user;

	@Builder
	protected Card(int amount, User user) {
		this.amount = amount;
		this.user = user;
	}
}

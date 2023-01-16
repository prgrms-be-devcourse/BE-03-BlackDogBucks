package com.prgrms.bdbks.domain.star.entity;

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
@Table(name = "stars")
@NoArgsConstructor(access = PROTECTED)
public class Star extends AbstractTimeColumn {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "star_id")
	private Long id;

	@Column(nullable = false)
	private short count;

	@OneToOne(fetch = LAZY)
	private User user;

	@Builder
	protected Star(short count, User user) {
		this.count = count;
		this.user = user;
	}
}

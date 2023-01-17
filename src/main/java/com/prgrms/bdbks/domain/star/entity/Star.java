package com.prgrms.bdbks.domain.star.entity;

import static com.google.common.base.Preconditions.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.prgrms.bdbks.common.domain.AbstractTimeColumn;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stars")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Star extends AbstractTimeColumn {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "star_id")
	private Long id;

	@Column(nullable = false)
	private short count;

	@Builder
	protected Star(short count) {
		validateCount(count);
		this.count = count;
	}

	private void validateCount(int count) {
		checkArgument(count >= 0, "별은 0개부터 소지할 수 있습니다.");
	}
}

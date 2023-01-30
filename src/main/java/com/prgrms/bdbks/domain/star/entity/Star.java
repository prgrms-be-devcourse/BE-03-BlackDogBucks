package com.prgrms.bdbks.domain.star.entity;

import static com.google.common.base.Preconditions.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.prgrms.bdbks.common.domain.AbstractTimeColumn;
import com.prgrms.bdbks.domain.user.entity.User;

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

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false)
	private int count;

	@Builder
	protected Star(User user, int count) {
		validateCount(count);
		validateUser(user);
		this.user = user;
		this.count = count;
	}

	private void validateCount(int count) {
		checkArgument(count >= 0, "별은 0개부터 소지할 수 있습니다.");
	}

	private void validateUser(User user) {
		checkNotNull(user, "유저 정보를 입력해주세요");
	}

	public void increaseCount() {
		this.count += 1;
	}

	public void decreaseCount() {
		this.count -= 1;
	}

	public int exchangeCoupon() {
		int couponCount = count / 12;
		this.count %= 12;
		return couponCount;
	}
}
package com.prgrms.bdbks.domain.user.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.prgrms.bdbks.common.domain.AbstractTimeColumn;
import com.prgrms.bdbks.domain.store.entity.Store;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_authorities")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAuthority extends AbstractTimeColumn {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_authority_id")
	private Long userAuthorityId;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = true)
	@JoinColumn(name = "authority_name")
	private Authority authority;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stores_id")
	private Store store;
}

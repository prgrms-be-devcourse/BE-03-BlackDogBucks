package com.prgrms.bdbks.domain.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.prgrms.bdbks.common.domain.AbstractTimeColumn;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "authorities")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority extends AbstractTimeColumn {

	@Id
	@Column(name = "authority_name", length = 10)
	private String authorityName;
}

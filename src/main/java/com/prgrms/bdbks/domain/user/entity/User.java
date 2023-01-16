package com.prgrms.bdbks.domain.user.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prgrms.bdbks.common.domain.AbstractTimeColumn;
import com.prgrms.bdbks.domain.user.authority.Authority;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AbstractTimeColumn {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(min = 6, max = 20)
	@Column(name = "login_id", nullable = false, unique = true)
	private String loginId;

	@Size(min = 8, max = 20)
	@Column(nullable = false)
	@JsonIgnore
	private String password;

	@Size(min = 4, max = 20)
	@Column(nullable = false)
	private String nickName;

	@Column(nullable = false)
	private LocalDateTime birthDate;

	@Size(min = 11, max = 11)
	@Column(nullable = false, unique = true)
	private String phone;

	@Email
	@Column(nullable = false, unique = true)
	private String email;

	@Enumerated(EnumType.STRING)
	private Authority authority;

	@Builder
	public User(Long id, String loginId, String password, String nickName, LocalDateTime birthDate, String phone,
		String email, Authority authority) {
		this.id = id;
		this.loginId = loginId;
		this.password = password;
		this.nickName = nickName;
		this.birthDate = birthDate;
		this.phone = phone;
		this.email = email;
		this.authority = authority;
	}
}

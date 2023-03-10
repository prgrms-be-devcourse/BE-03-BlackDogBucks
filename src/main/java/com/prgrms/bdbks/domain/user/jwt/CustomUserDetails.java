package com.prgrms.bdbks.domain.user.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.prgrms.bdbks.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class CustomUserDetails implements UserDetails {

	@Getter
	private final User user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getAuthorities();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getLoginId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return user.isActivated();
	}

	@Override
	public boolean isAccountNonLocked() {
		return user.isActivated();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return user.isActivated();
	}

	@Override
	public boolean isEnabled() {
		return user.isActivated();
	}

}

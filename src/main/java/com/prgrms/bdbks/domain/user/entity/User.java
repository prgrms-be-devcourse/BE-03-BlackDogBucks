package com.prgrms.bdbks.domain.user.entity;

import static com.google.common.base.Preconditions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.prgrms.bdbks.common.domain.AbstractTimeColumn;
import com.prgrms.bdbks.common.exception.AuthorityNotFoundException;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AbstractTimeColumn {

	private static final String NUMBER_REGEX = "[0-9]+";
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "login_id", length = 20, nullable = false, unique = true)
	private String loginId;

	@Column(length = 60, nullable = false)
	private String password;

	@Column(length = 20, nullable = false)
	private String nickname;

	@Column(nullable = false)
	private LocalDate birthDate;

	@Column(length = 11, nullable = false, unique = true)
	private String phone;

	@Column(nullable = false, unique = true)
	private String email;

	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<UserAuthority> userAuthorities = new ArrayList<>();

	@Builder
	protected User(Long id, String loginId, String password, String nickname, LocalDate birthDate, String phone,
		String email) {
		validateLoginId(loginId);
		validatePassword(password);
		validateNickname(nickname);
		validateBirthDate(birthDate);
		validatePhone(phone);
		validateEmail(email);

		this.id = id;
		this.loginId = loginId;
		this.password = password;
		this.nickname = nickname;
		this.birthDate = birthDate;
		this.phone = phone;
		this.email = email;
	}

	public List<GrantedAuthority> getAuthorities() {
		return this.userAuthorities.stream().map(
			userAuth -> new SimpleGrantedAuthority(userAuth.getAuthority().getAuthorityName().name())
		).collect(Collectors.toList());
	}

	public void validateStore(String storeId) {
		userAuthorities.stream()
			.filter(userAuthority -> Objects.equals(userAuthority.getStore().getId(), storeId))
			.findAny()
			.orElseThrow(() -> new AuthorityNotFoundException("해당 유저에게 권한이 없습니다."));
	}

	private void validateLoginId(String loginId) {
		checkArgument(StringUtils.hasText(loginId), "아이디를 입력해주세요.");
		checkArgument(6 <= loginId.length() && loginId.length() <= 20, "아이디의 길이를 확인해주세요.");
	}

	private void validatePassword(String password) {
		checkArgument(StringUtils.hasText(password), "비밀번호를 입력해주세요.");
		checkArgument(8 <= password.length() && password.length() <= 60, "비밀번호의 길이를 확인해주세요.");
	}

	private void validateNickname(String nickname) {
		checkArgument(StringUtils.hasText(nickname), "닉네임을 입력해주세요.");
		checkArgument(2 <= nickname.length() && nickname.length() <= 20, "닉네임의 길이를 확인해주세요.");
	}

	private void validateBirthDate(LocalDate birthDate) {
		checkNotNull(birthDate, "생일을 입력해주세요.");
		checkArgument(birthDate.isBefore(LocalDate.now()), "생년월일을 확인해주세요.");
	}

	private void validatePhone(String phone) {
		checkArgument(StringUtils.hasText(phone), "핸드폰 번호를 입력해주세요.");
		checkArgument(phone.length() == 11, "핸드폰 번호를 확인해주세요.");
		checkArgument(phone.matches(NUMBER_REGEX), "숫자를 입력해주세요.");
	}

	private void validateEmail(String email) {
		checkArgument(StringUtils.hasText(email), "이메일을 입력해주세요.");
		checkArgument(email.matches(EMAIL_REGEX));
	}

	public void changePassword(String password) {
		validatePassword(password);
		this.password = password;
	}

	public void checkPassword(PasswordEncoder passwordEncoder, String password) {
		if (!passwordEncoder.matches(password, this.password)) {
			throw new BadCredentialsException("아이디와 비밀번호를 확인해주세요.");
		}
	}

	public void addUserAuthority(UserAuthority userAuthority) {
		if (!this.getUserAuthorities().contains(userAuthority)) {
			this.getUserAuthorities().add(userAuthority);
		}

		userAuthority.changeUser(this);
	}

}

package com.prgrms.bdbks.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.bdbks.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByLoginId(String loginId);

	boolean existsByLoginId(String loginId);

	@Query(value = "SELECT u FROM User u WHERE u.loginId = :loginId AND u.password = :password")
	Optional<User> findByLoginIdAndPassword(@Param("loginId") String loginId, @Param("password") String password);

	@Query(value = "select u from User u "
		+ "join fetch u.userAuthorities ua "
		+ "join fetch ua.authority a "
		+ "where u.loginId = :loginId")
	Optional<User> findByLoginIdWithAuthorities(@Param("loginId") String loginId);
}

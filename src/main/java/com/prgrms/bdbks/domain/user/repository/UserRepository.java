package com.prgrms.bdbks.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.bdbks.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByLoginId(String loginId);

	boolean existsByLoginId(String loginId);

	@Query(value = "SELECT u FROM User u WHERE u.loginId = :loginId AND u.password = :password")
	Optional<User> findByLoginIdAndPassword(@Param("loginId") String loginId, @Param("password") String password);

	@EntityGraph(
		type = EntityGraph.EntityGraphType.FETCH,
		attributePaths = "userAuthorities.store")
	@Query(value = "select u from User u where u.loginId = :loginId")
	Optional<User> findByLoginIdWithAuthorities(@Param("loginId") String loginId);

}

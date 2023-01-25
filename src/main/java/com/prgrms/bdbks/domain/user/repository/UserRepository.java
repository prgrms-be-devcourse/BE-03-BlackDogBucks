package com.prgrms.bdbks.domain.user.repository;

import com.prgrms.bdbks.domain.user.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByLoginId(String loginId);

	@Query(value = "SELECT u FROM User u WHERE u.loginId = :loginId AND u.password = :password")
	Optional<User> findByLoginIdAndPassword(@Param("loginId") String loginId, @Param("password") String password);
}
package com.prgrms.bdbks.domain.user.repository;

import com.prgrms.bdbks.domain.user.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository
	extends JpaRepository<User, String> {
	Optional<User> findByLoginId(String var1);

	@Query(value = "SELECT u FROM User u WHERE u.loginId = ?1 AND u.password = ?2")
	Optional<User> findByLoginIdAndPassword(String var1, String var2);
}
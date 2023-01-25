package com.prgrms.bdbks.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.bdbks.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}

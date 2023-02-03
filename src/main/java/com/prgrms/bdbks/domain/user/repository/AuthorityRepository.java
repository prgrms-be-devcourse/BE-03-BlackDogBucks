package com.prgrms.bdbks.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.bdbks.domain.user.entity.Authority;
import com.prgrms.bdbks.domain.user.role.Role;

public interface AuthorityRepository extends JpaRepository<Authority, Role> {
}

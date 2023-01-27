package com.prgrms.bdbks.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.bdbks.domain.order.entity.CustomOption;

public interface CustomOptionRepository extends JpaRepository<CustomOption, Long> {
}

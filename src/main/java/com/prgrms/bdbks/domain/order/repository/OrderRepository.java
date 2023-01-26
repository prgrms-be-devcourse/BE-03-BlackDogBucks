package com.prgrms.bdbks.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.bdbks.domain.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, String>, OrderSupport {

}

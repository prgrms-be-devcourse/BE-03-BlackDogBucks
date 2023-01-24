package com.prgrms.bdbks.domain.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.bdbks.domain.item.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}

package com.prgrms.bdbks.domain.item.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {

	Optional<ItemCategory> findByItemTypeAndName(@Param("itemType") ItemType itemType,
		@Param("name") String name);

	List<ItemCategory> findByItemType(@Param("itemType") ItemType itemType);

	Optional<ItemCategory> findByName(@Param("name") String name);

}

package com.prgrms.bdbks.domain.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemType;

public interface ItemRepository extends JpaRepository<Item, Long> {

	@Query("select i from Item i join fetch i.category c where c.itemType = :itemType and c.name = :categoryName ")
	List<Item> findAllByItemTypeAndCategoryName(@Param("itemType") ItemType itemType,
		@Param("categoryName") String categoryName);

}

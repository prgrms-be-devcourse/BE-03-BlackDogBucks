package com.prgrms.bdbks.domain.item.converter;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.prgrms.bdbks.domain.item.dto.DefaultOptionCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ItemMapperTest {

	@Test
	public void test() { // map struct 동작하나 보려고 임시 테스트 수정할게용
		ItemMapper mapper = Mappers.getMapper(ItemMapper.class);

		DefaultOptionCreateRequest defaultOptionCreateRequest = new DefaultOptionCreateRequest(0, 0, 0, 0, null, null,
			null);
		ItemCreateRequest itemCreateRequest = new ItemCreateRequest(ItemType.BEVERAGE, "콜드블루", "바크콜", "non", 3000,
			"https://www.naver.com/", "존맛", defaultOptionCreateRequest);
		Item item = mapper.itemCreateRequestToItem(itemCreateRequest, new ItemCategory("카", "d", ItemType.BEVERAGE));

		log.info(item.toString());
		log.info(item.getDefaultOption().toString());
	}

}
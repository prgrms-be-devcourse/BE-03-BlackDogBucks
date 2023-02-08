package com.prgrms.bdbks.domain.store.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestConstructor;

import com.prgrms.bdbks.CustomDataJpaTest;
import com.prgrms.bdbks.common.util.Location;
import com.prgrms.bdbks.config.jpa.JpaConfig;
import com.prgrms.bdbks.domain.store.entity.Store;

import lombok.RequiredArgsConstructor;

@Import(JpaConfig.class)
@CustomDataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StoreRepositoryTest {

	private final StoreRepository storeRepository;

	@Test
	@DisplayName("저장 - 두개 매장 등록에 성공 한다.")
	@Rollback(false)
	void insert_test() throws Exception {
		String pointWKT = String.format("POINT (%s %s)", 37.4843861241449, 127.014197798445);

		Store store = Store.builder()
			.id("200157085")
			.name("스타벅스 남부터미널2점")
			.lotNumberAddress("서울특별시 양천구 목동 916")
			.roadNameAddress("서울특별시 양천구 목동동로 257, (목동, 하이페리온)")
			.position((Point)new WKTReader().read(pointWKT))
			.build();

		String pointWKT2 = String.format("POINT (%s %s)", 37.5829644184897, 127.00388696535);
		Store store2 = Store.builder()
			.id("205857795")
			.name("서울특별시 종로구 동숭동 30")
			.lotNumberAddress("서울특별시 종로구 동숭동 30")
			.roadNameAddress("서울특별시 종로구 동숭길 110")
			.position((Point)new WKTReader().read(pointWKT2))
			.build();

		storeRepository.save(store);
		storeRepository.save(store2);
		assertEquals(2L, storeRepository.count());
	}

	@Test
	@DisplayName("조회 - 좌표간 거리 환산에 성공 한다.")
	void distance_test() {
		Location location = new Location(37.493657, 127.013772);
		List<Integer> distances = storeRepository.findDistance(location);

		assertTrue(distances.stream().allMatch(Objects::nonNull));
	}

}

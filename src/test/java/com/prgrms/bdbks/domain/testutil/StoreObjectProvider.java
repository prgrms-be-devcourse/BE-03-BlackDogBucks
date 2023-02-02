package com.prgrms.bdbks.domain.testutil;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

import com.prgrms.bdbks.domain.store.entity.Store;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreObjectProvider {

	public static final String STORE_ID = "20585779";

	public static Store creatStore(String storeId) {
		return Store.builder()
			.id(storeId)
			.name("노원점")
			.lotNumberAddress("lotNumberAddress")
			.roadNameAddress("강남구 이고싶다.")
			.position(new GeometryFactory().createPoint(new Coordinate(36.5, 138.8)))
			.build();
	}
}

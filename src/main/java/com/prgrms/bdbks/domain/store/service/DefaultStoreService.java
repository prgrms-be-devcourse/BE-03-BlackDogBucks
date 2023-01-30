package com.prgrms.bdbks.domain.store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.common.exception.PointParseException;
import com.prgrms.bdbks.domain.store.converter.StoreMapper;
import com.prgrms.bdbks.domain.store.dto.StoreCreateRequest;
import com.prgrms.bdbks.domain.store.dto.StoreResponse;
import com.prgrms.bdbks.domain.store.entity.Store;
import com.prgrms.bdbks.domain.store.repository.StoreRepository;
import com.prgrms.bdbks.global.util.Location;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultStoreService implements StoreService {

	private static final int distance = 10000;
	private final StoreRepository storeRepository;

	@Override
	public Store findById(String storeId) {
		return storeRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException(Store.class, storeId));
	}

	@Override
	public StoreResponse.StoreInformation findStoreById(String storeId) {
		Optional<Store> store = storeRepository.findById(storeId);
		if (store.isPresent()) {
			return new StoreResponse.StoreInformation(store.get());
		} else {
			throw new EntityNotFoundException(Store.class, storeId);
		}
	}

	@Override
	public String createStore(StoreCreateRequest storeCreateRequest) {
		String pointWKT = String.format("POINT(%s %s)", storeCreateRequest.getLongitude(),
			storeCreateRequest.getLatitude());

		try {
			Point point = (Point)new WKTReader().read(pointWKT);

			Store store = Store.builder()
				.id(storeCreateRequest.getId())
				.name(storeCreateRequest.getName())
				.lotNumberAddress(storeCreateRequest.getLotNumberAddress())
				.roadNameAddress(storeCreateRequest.getRoadNameAddress())
				.position(point)
				.build();

			return storeRepository.save(store).getId();
		} catch (ParseException e) {
			throw new PointParseException(String.format("위경도 파싱에 실패했습니다. %s,%s",
				storeCreateRequest.getLongitude(),
				storeCreateRequest.getLatitude()));
		}
	}

	@Override
	public List<StoreResponse.StoreInformation> findAllByDisStrictName(String district) {
		List<Store> storeList = storeRepository.findTop10StoresByLotNumberAddress(district);

		List<StoreResponse.StoreInformation> storeResponseList = new ArrayList<>();
		for (Store store : storeList) {
			storeResponseList.add(new StoreResponse.StoreInformation(store));
		}
		return storeResponseList;
	}

	@Override
	public List<StoreResponse.StoreInformation> findAllByPoint(double latitude, double longitude) {
		Location location = new Location(latitude, longitude);
		List<Store> storeList = storeRepository.findAllByDistance(location, distance);

		List<StoreResponse.StoreInformation> storeResponseList = new ArrayList<>();
		for (Store store : storeList) {
			storeResponseList.add(new StoreResponse.StoreInformation(store));
		}
		return storeResponseList;
	}

	@Override
	public boolean existsById(String storeId) {
		return storeRepository.existsById(storeId);
	}
}

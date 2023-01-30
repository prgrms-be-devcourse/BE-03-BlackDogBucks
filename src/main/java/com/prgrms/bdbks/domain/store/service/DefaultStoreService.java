package com.prgrms.bdbks.domain.store.service;

import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	private static final int DISTANCE = 10000;
	private final StoreRepository storeRepository;
	private final StoreMapper storeMapper;

	@Override
	public Store findById(String storeId) {
		return storeRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException(Store.class, storeId));
	}

	@Override
	@Transactional
	public StoreResponse.Information findStoreById(String storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new EntityNotFoundException(Store.class, storeId));

		return new StoreResponse.Information(store);
	}

	@Override
	@Transactional
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
	@Transactional
	public List<StoreResponse.Information> findAllByDisStrictName(String district) {
		List<Store> storeList = storeRepository.findTop10StoresByLotNumberAddress(district);

		return storeList.stream()
			.map(StoreResponse.Information::new)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<StoreResponse.Information> findAllByPoint(double latitude, double longitude) {
		Location location = new Location(latitude, longitude);
		List<Store> storeList = storeRepository.findAllByDistance(location, DISTANCE);

		return storeList.stream()
			.map(StoreResponse.Information::new)
			.collect(Collectors.toList());
	}

	@Override
	public boolean existsById(String storeId) {
		return storeRepository.existsById(storeId);
	}
}

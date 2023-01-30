package com.prgrms.bdbks.domain.store.service;

import java.util.List;

import com.prgrms.bdbks.domain.store.dto.StoreCreateRequest;
import com.prgrms.bdbks.domain.store.dto.StoreResponse;
import com.prgrms.bdbks.domain.store.entity.Store;

public interface StoreService {

	Store findById(String storeId);

	StoreResponse.Information findStoreById(String storeId);

	String createStore(StoreCreateRequest storeCreateRequest);

	List<StoreResponse.Information> findAllByDisStrictName(String district);

	List<StoreResponse.Information> findAllByPoint(double latitude, double longitude);

	boolean existsById(String storeId);

}

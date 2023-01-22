package com.prgrms.bdbks.domain.item.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.domain.item.dto.DefaultOptionCreateRequest;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;
import com.prgrms.bdbks.domain.item.repository.DefaultOptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultOptionService implements ItemOptionService {

	private final DefaultOptionRepository defaultOptionRepository;

	@Transactional
	@Override
	public DefaultOption create(DefaultOptionCreateRequest request) {

		DefaultOption defaultOption = DefaultOption.builder()
			.espressoShotCount(request.getEspressoShotCount())
			.hazelnutSyrupCount(request.getHazelnutSyrupCount())
			.classicSyrupCount(request.getClassicSyrupCount())
			.vanillaSyrupCount(request.getVanillaSyrupCount())
			.milkAmount(request.getMilkAmount())
			.espressoType(request.getEspressoType())
			.milkType(request.getMilkType())
			.build();

		return defaultOptionRepository.save(defaultOption);
	}

}
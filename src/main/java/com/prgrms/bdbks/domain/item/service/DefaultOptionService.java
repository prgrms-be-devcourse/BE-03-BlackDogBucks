package com.prgrms.bdbks.domain.item.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.domain.item.converter.ItemMapper;
import com.prgrms.bdbks.domain.item.dto.DefaultOptionCreateRequest;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;
import com.prgrms.bdbks.domain.item.repository.DefaultOptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultOptionService implements ItemOptionService {

	private final DefaultOptionRepository defaultOptionRepository;

	private final ItemMapper itemMapper;

	@Transactional
	@Override
	public DefaultOption create(DefaultOptionCreateRequest request) {

		DefaultOption defaultOption = itemMapper.defaultOptionCreateRequestToEntity(request);

		return defaultOptionRepository.save(defaultOption);
	}

}
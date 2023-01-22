package com.prgrms.bdbks.domain.item.service;

import com.prgrms.bdbks.domain.item.dto.DefaultOptionCreateRequest;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;

public interface ItemOptionService {

	DefaultOption create(DefaultOptionCreateRequest request);

}

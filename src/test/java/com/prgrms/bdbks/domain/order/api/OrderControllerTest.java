package com.prgrms.bdbks.domain.order.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.domain.store.service.StoreService;
import com.prgrms.bdbks.domain.user.service.DefaultUserService;

import lombok.RequiredArgsConstructor;

@Transactional
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class OrderControllerTest {

	private static final String BASE_REQUEST_URI = "/api/v1/orders";

	private final MockMvc mockMvc;

	@MockBean
	private StoreService storeService;

	@MockBean
	private DefaultUserService defaultUserService;

	@DisplayName("조회 - 존재하지않는 id 조회시 404 notFound 를 반환한다. - 실패")
	@Test
	void findOrderById_fail() throws Exception {
		//given
		String orderId = "20230117BKDKS1234";

		// when
		mockMvc.perform(get(BASE_REQUEST_URI + "/{orderId}", orderId)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isNotFound());
	}
}
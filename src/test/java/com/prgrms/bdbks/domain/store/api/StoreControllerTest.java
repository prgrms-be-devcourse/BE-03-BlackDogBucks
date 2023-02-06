package com.prgrms.bdbks.domain.store.api;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.bdbks.config.security.SecurityConfig;
import com.prgrms.bdbks.domain.store.dto.StoreCreateRequest;
import com.prgrms.bdbks.domain.store.dto.StoreResponse;
import com.prgrms.bdbks.domain.store.service.StoreService;

import lombok.RequiredArgsConstructor;

@Transactional
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@Import(SecurityConfig.class)
class StoreControllerTest {

	@Autowired
	private StoreController storeController;

	@MockBean
	private StoreService storeService;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(storeController).build();
	}

	@Test
	@DisplayName("생성 - 매장 생성에 성공한다.")
	@Rollback(value = false)
	void create_store_success() throws Exception {

		StoreCreateRequest storeCreateRequest = StoreCreateRequest.builder()
			.id("200157085")
			.name("스타벅스 남부터미널2점")
			.lotNumberAddress("서울특별시 양천구 목동 916")
			.roadNameAddress("서울특별시 양천구 목동동로 257, (목동, 하이페리온)")
			.latitude(37.4843861241449)
			.longitude(127.014197798445)
			.build();
		when(storeService.createStore(storeCreateRequest)).thenReturn("200157085");

		mockMvc.perform(post("/api/v1/stores")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(storeCreateRequest)))
			.andExpect(status().isCreated());
	}

	@DisplayName("조회 - 매장 아이디로 조회하는데 성공한다.")
	@Test
	@WithMockCustomUser
	void find_store_success() throws Exception {

		String storeId = "200157085";
		StoreResponse.Information storeInfo = new StoreResponse.Information();
		when(storeService.findStoreById(storeId)).thenReturn(storeInfo);

		mockMvc.perform(get("/api/v1/stores/{storeId}", storeId))
			.andExpect(status().isOk());

		verify(storeService, times(1)).findStoreById(storeId);
	}

	@DisplayName("조회 - 구 이름으로 조회하는데 성공한다.")
	@Test
	void find_districtStores_success() throws Exception {

		String district = "종로구";
		List<StoreResponse.Information> stores = Arrays.asList(new StoreResponse.Information(),
			new StoreResponse.Information());
		when(storeService.findAllByDisStrictName(district)).thenReturn(stores);

		mockMvc.perform(get("/api/v1/stores?district={district}", district))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)));

		verify(storeService, times(1)).findAllByDisStrictName(district);
	}

	@Test
	@DisplayName("조회 - 위경도 기준으로 가까운 매장을 조회하는데 성공한다.")
	void find_pointStores_success() throws Exception {

		double latitude = 37.4843861241449;
		double longitude = 127.014197798445;

		List<StoreResponse.Information> stores = Arrays.asList(new StoreResponse.Information(),
			new StoreResponse.Information());
		when(storeService.findAllByPoint(latitude, longitude)).thenReturn(stores);

		mockMvc.perform(get(String.format("/api/v1/stores/location?latitude=%s&longitude=%s", latitude, longitude))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}
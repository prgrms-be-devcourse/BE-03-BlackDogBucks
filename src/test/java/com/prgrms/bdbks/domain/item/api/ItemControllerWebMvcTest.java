package com.prgrms.bdbks.domain.item.api;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.bdbks.domain.item.dto.DefaultOptionCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.item.service.ItemService;
import com.prgrms.bdbks.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@WithMockUser
@AutoConfigureMockMvc
@WebMvcTest(controllers = ItemController.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class ItemControllerWebMvcTest {

	private static final String BASE_REQUEST_URI = "/api/v1/items";

	@MockBean
	private ItemService itemService;

	@MockBean
	private UserService defaultUserService;

	private final MockMvc mockMvc;

	private final ObjectMapper objectMapper;

	@DisplayName("?????? - Item ??? ???????????? ????????? Resource ??? URI ??? ???????????? - ??????.")
	@Test
	void createItem_success() throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String categoryName = "???????????????";
		String name = "?????? ???????????????";
		String englishName = "Caffe Americano";
		int price = 4500;
		String image = "http://hkbks.com/url";
		String description = "?????? ?????????????????? ????????? ?????? ?????? ??????????????? ???????????? ????????? ?????????????????? ?????? ???????????? ??? ?????? ??? ?????? ??????";

		Integer espressoShotCount = 2;
		Integer vanillaSyrupCount = 0;
		Integer classicSyrupCount = 0;
		Integer hazelnutSyrupCount = 0;

		DefaultOptionCreateRequest defaultOptionCreateRequest = new DefaultOptionCreateRequest(espressoShotCount,
			vanillaSyrupCount, classicSyrupCount, hazelnutSyrupCount, null, null, null);

		ItemCreateRequest request = new ItemCreateRequest(
			beverage, categoryName, name, englishName, price, image, description, defaultOptionCreateRequest
		);
		Long returnId = 1L;

		given(itemService.createItem(request))
			.willReturn(returnId);

		//when
		mockMvc.perform(post(BASE_REQUEST_URI)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsString(request)
				)
			)
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", containsString(BASE_REQUEST_URI + "/" + returnId)))
			.andDo(print());

		verify(itemService).createItem(request);
	}

	@DisplayName("?????? - ItemCreateRequest??? itemType??? null?????? ????????? ???????????? BadRequest??? ????????????. - ??????.")
	@Test
	void createItem_itemTypeValidation_fail() throws Exception {
		//given
		String categoryName = "???????????????";
		String name = "?????? ???????????????";
		String englishName = "Caffe Americano";
		int price = 4500;
		String image = "http://hkbks.com/url";
		String description = "?????? ?????????????????? ????????? ?????? ?????? ??????????????? ???????????? ????????? ?????????????????? ?????? ???????????? ??? ?????? ??? ?????? ??????";

		Integer espressoShotCount = 2;
		Integer vanillaSyrupCount = 0;
		Integer classicSyrupCount = 0;
		Integer hazelnutSyrupCount = 0;

		DefaultOptionCreateRequest defaultOptionCreateRequest = new DefaultOptionCreateRequest(espressoShotCount,
			vanillaSyrupCount, classicSyrupCount, hazelnutSyrupCount, null, null, null);

		ItemCreateRequest request = new ItemCreateRequest(
			null, categoryName, name, englishName, price, image, description, defaultOptionCreateRequest
		);

		//when
		mockMvc.perform(post(BASE_REQUEST_URI)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsString(request)
				)
			)
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@DisplayName("?????? - ItemCreateRequest??? categoryName??? black?????? ????????? ???????????? BadRequest??? ????????????. - ??????.")
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  ", "  \n"})
	void createItem_categoryNameValidation_fail(String categoryName) throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String name = "?????? ???????????????";
		String englishName = "Caffe Americano";
		int price = 4500;
		String image = "http://hkbks.com/url";
		String description = "?????? ?????????????????? ????????? ?????? ?????? ??????????????? ???????????? ????????? ?????????????????? ?????? ???????????? ??? ?????? ??? ?????? ??????";

		Integer espressoShotCount = 2;
		Integer vanillaSyrupCount = 0;
		Integer classicSyrupCount = 0;
		Integer hazelnutSyrupCount = 0;

		DefaultOptionCreateRequest defaultOptionCreateRequest = new DefaultOptionCreateRequest(espressoShotCount,
			vanillaSyrupCount, classicSyrupCount, hazelnutSyrupCount, null, null, null);

		ItemCreateRequest request = new ItemCreateRequest(
			beverage, categoryName, name, englishName, price, image, description, defaultOptionCreateRequest
		);

		//when
		mockMvc.perform(post(BASE_REQUEST_URI)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsString(request)
				)
			)
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@DisplayName("?????? - ItemCreateRequest??? name??? black?????? ????????? ???????????? BadRequest??? ????????????. - ??????.")
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  ", "  \n"})
	void createItem_nameValidation_fail(String name) throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String categoryName = "???????????????";
		String englishName = "Caffe Americano";
		int price = 4500;
		String image = "http://hkbks.com/url";
		String description = "?????? ?????????????????? ????????? ?????? ?????? ??????????????? ???????????? ????????? ?????????????????? ?????? ???????????? ??? ?????? ??? ?????? ??????";

		Integer espressoShotCount = 2;
		Integer vanillaSyrupCount = 0;
		Integer classicSyrupCount = 0;
		Integer hazelnutSyrupCount = 0;

		DefaultOptionCreateRequest defaultOptionCreateRequest = new DefaultOptionCreateRequest(espressoShotCount,
			vanillaSyrupCount, classicSyrupCount, hazelnutSyrupCount, null, null, null);

		ItemCreateRequest request = new ItemCreateRequest(
			beverage, categoryName, name, englishName, price, image, description, defaultOptionCreateRequest
		);

		//when
		mockMvc.perform(post(BASE_REQUEST_URI)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsString(request)
				)
			)
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@DisplayName("?????? - ItemCreateRequest??? englishName??? black?????? ????????? ???????????? BadRequest??? ????????????. - ??????.")
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  ", "  \n"})
	void createItem_englishNameValidation_fail(String englishName) throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String categoryName = "???????????????";
		String name = "?????? ???????????????";
		int price = 4500;
		String image = "http://hkbks.com/url";
		String description = "?????? ?????????????????? ????????? ?????? ?????? ??????????????? ???????????? ????????? ?????????????????? ?????? ???????????? ??? ?????? ??? ?????? ??????";

		Integer espressoShotCount = 2;
		Integer vanillaSyrupCount = 0;
		Integer classicSyrupCount = 0;
		Integer hazelnutSyrupCount = 0;

		DefaultOptionCreateRequest defaultOptionCreateRequest = new DefaultOptionCreateRequest(espressoShotCount,
			vanillaSyrupCount, classicSyrupCount, hazelnutSyrupCount, null, null, null);

		ItemCreateRequest request = new ItemCreateRequest(
			beverage, categoryName, name, englishName, price, image, description, defaultOptionCreateRequest
		);

		//when
		mockMvc.perform(post(BASE_REQUEST_URI)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsString(request)
				)
			)
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@DisplayName("?????? - ItemCreateRequest??? image??? url ????????? ??????????????? ????????? ???????????? BadRequest??? ????????????. - ??????.")
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  ", "  \n", "img ?????????."})
	void createItem_imageValidation_fail(String image) throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String categoryName = "???????????????";
		String name = "?????? ???????????????";
		String englishName = "Caffe Americano";
		int price = 4500;
		String description = "?????? ?????????????????? ????????? ?????? ?????? ??????????????? ???????????? ????????? ?????????????????? ?????? ???????????? ??? ?????? ??? ?????? ??????";

		Integer espressoShotCount = 2;
		Integer vanillaSyrupCount = 0;
		Integer classicSyrupCount = 0;
		Integer hazelnutSyrupCount = 0;

		DefaultOptionCreateRequest defaultOptionCreateRequest = new DefaultOptionCreateRequest(espressoShotCount,
			vanillaSyrupCount, classicSyrupCount, hazelnutSyrupCount, null, null, null);

		ItemCreateRequest request = new ItemCreateRequest(
			beverage, categoryName, name, englishName, price, image, description, defaultOptionCreateRequest
		);

		//when
		mockMvc.perform(post(BASE_REQUEST_URI)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsString(request)
				)
			)
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@DisplayName("?????? - ItemCreateRequest??? description??? black?????? ????????? ???????????? BadRequest??? ????????????. - ??????.")
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  ", "  \n"})
	void createItem_descriptionValidation_fail(String description) throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String categoryName = "???????????????";
		String name = "?????? ???????????????";
		String englishName = "Caffe Americano";
		int price = 4500;
		String image = "http://hkbks.com/url";

		Integer espressoShotCount = 2;
		Integer vanillaSyrupCount = 0;
		Integer classicSyrupCount = 0;
		Integer hazelnutSyrupCount = 0;

		DefaultOptionCreateRequest defaultOptionCreateRequest = new DefaultOptionCreateRequest(espressoShotCount,
			vanillaSyrupCount, classicSyrupCount, hazelnutSyrupCount, null, null, null);

		ItemCreateRequest request = new ItemCreateRequest(
			beverage, categoryName, name, englishName, price, image, description, defaultOptionCreateRequest
		);

		//when
		mockMvc.perform(post(BASE_REQUEST_URI)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsString(request)
				)
			)
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@DisplayName("?????? - ItemCreateRequest??? defaultOptionRequest??? null?????? ????????? ???????????? BadRequest??? ????????????. - ??????.")
	@Test
	void createItem_defaultOptionRequestValidation_fail() throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String categoryName = "???????????????";
		String name = "?????? ???????????????";
		String englishName = "Caffe Americano";
		int price = 4500;
		String image = "http://hkbks.com/url";
		String description = "?????? ?????????????????? ????????? ?????? ?????? ??????????????? ???????????? ????????? ?????????????????? ?????? ???????????? ??? ?????? ??? ?????? ??????";

		ItemCreateRequest request = new ItemCreateRequest(
			null, categoryName, name, englishName, price, image, description, null
		);

		//when
		mockMvc.perform(post(BASE_REQUEST_URI)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsString(request)
				)
			)
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@DisplayName("?????? - ItemCreateRequest??? price??? 0 ???????????? ????????? ???????????? BadRequest??? ????????????. - ??????.")
	@ParameterizedTest
	@NullSource
	@ValueSource(ints = {-1, -43, -123})
	void createItem_priceValidation_fail(Integer price) throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String categoryName = "???????????????";
		String name = "?????? ???????????????";
		String englishName = "Caffe Americano";
		String image = "http://hkbks.com/url";
		String description = "?????? ?????????????????? ????????? ?????? ?????? ??????????????? ???????????? ????????? ?????????????????? ?????? ???????????? ??? ?????? ??? ?????? ??????";

		Integer espressoShotCount = 2;
		Integer vanillaSyrupCount = 0;
		Integer classicSyrupCount = 0;
		Integer hazelnutSyrupCount = 0;

		DefaultOptionCreateRequest defaultOptionCreateRequest = new DefaultOptionCreateRequest(espressoShotCount,
			vanillaSyrupCount, classicSyrupCount, hazelnutSyrupCount, null, null, null);

		ItemCreateRequest request = new ItemCreateRequest(
			beverage, categoryName, name, englishName, price, image, description, defaultOptionCreateRequest
		);

		//when
		mockMvc.perform(post(BASE_REQUEST_URI)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsString(request)
				)
			)
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

}
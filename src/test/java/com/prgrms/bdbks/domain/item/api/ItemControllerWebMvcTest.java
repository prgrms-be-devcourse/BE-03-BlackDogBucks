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
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.bdbks.domain.item.dto.DefaultOptionCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.item.service.ItemCategoryService;
import com.prgrms.bdbks.domain.item.service.ItemFacadeService;

import lombok.RequiredArgsConstructor;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class ItemControllerWebMvcTest {

	private static final String BASE_REQUEST_URI = "/api/v1/items";

	@MockBean
	private final ItemFacadeService itemFacadeService;

	private final MockMvc mockMvc;

	private final ObjectMapper objectMapper;

	@MockBean
	private final ItemCategoryService itemCategoryService;
	
	@DisplayName("생성 - Item 을 생성하고 생성된 Resource 의 URI 를 리턴한다 - 성공.")
	@Test
	void createItem_success() throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String categoryName = "에스프레소";
		String name = "카페 아메리카노";
		String englishName = "Caffe Americano";
		int price = 4500;
		String image = "http://hkbks.com/url";
		String description = "진한 에스프레소와 뜨거운 물을 섞어 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽게 잘 느낄 수 있는 커피";

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

		given(itemFacadeService.createItem(request))
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

		verify(itemFacadeService).createItem(request);
	}

	@DisplayName("생성 - ItemCreateRequest의 itemType이 null이면 응답에 실패하여 BadRequest를 반환한다. - 실패.")
	@Test
	void createItem_itemTypeValidation_fail() throws Exception {
		//given
		String categoryName = "에스프레소";
		String name = "카페 아메리카노";
		String englishName = "Caffe Americano";
		int price = 4500;
		String image = "http://hkbks.com/url";
		String description = "진한 에스프레소와 뜨거운 물을 섞어 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽게 잘 느낄 수 있는 커피";

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

	@DisplayName("생성 - ItemCreateRequest의 categoryName이 black이면 응답에 실패하여 BadRequest를 반환한다. - 실패.")
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  ", "  \n"})
	void createItem_categoryNameValidation_fail(String categoryName) throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String name = "카페 아메리카노";
		String englishName = "Caffe Americano";
		int price = 4500;
		String image = "http://hkbks.com/url";
		String description = "진한 에스프레소와 뜨거운 물을 섞어 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽게 잘 느낄 수 있는 커피";

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

	@DisplayName("생성 - ItemCreateRequest의 name이 black이면 응답에 실패하여 BadRequest를 반환한다. - 실패.")
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  ", "  \n"})
	void createItem_nameValidation_fail(String name) throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String categoryName = "에스프레소";
		String englishName = "Caffe Americano";
		int price = 4500;
		String image = "http://hkbks.com/url";
		String description = "진한 에스프레소와 뜨거운 물을 섞어 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽게 잘 느낄 수 있는 커피";

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

	@DisplayName("생성 - ItemCreateRequest의 englishName이 black이면 응답에 실패하여 BadRequest를 반환한다. - 실패.")
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  ", "  \n"})
	void createItem_englishNameValidation_fail(String englishName) throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String categoryName = "에스프레소";
		String name = "카페 아메리카노";
		int price = 4500;
		String image = "http://hkbks.com/url";
		String description = "진한 에스프레소와 뜨거운 물을 섞어 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽게 잘 느낄 수 있는 커피";

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

	@DisplayName("생성 - ItemCreateRequest의 image이 url 형식에 맞지않으면 응답에 실패하여 BadRequest를 반환한다. - 실패.")
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  ", "  \n", "img 입니다."})
	void createItem_imageValidation_fail(String image) throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String categoryName = "에스프레소";
		String name = "카페 아메리카노";
		String englishName = "Caffe Americano";
		int price = 4500;
		String description = "진한 에스프레소와 뜨거운 물을 섞어 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽게 잘 느낄 수 있는 커피";

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

	@DisplayName("생성 - ItemCreateRequest의 description이 black이면 응답에 실패하여 BadRequest를 반환한다. - 실패.")
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  ", "  \n"})
	void createItem_descriptionValidation_fail(String description) throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String categoryName = "에스프레소";
		String name = "카페 아메리카노";
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

	@DisplayName("생성 - ItemCreateRequest의 defaultOptionRequest이 null이면 응답에 실패하여 BadRequest를 반환한다. - 실패.")
	@Test
	void createItem_defaultOptionRequestValidation_fail() throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String categoryName = "에스프레소";
		String name = "카페 아메리카노";
		String englishName = "Caffe Americano";
		int price = 4500;
		String image = "http://hkbks.com/url";
		String description = "진한 에스프레소와 뜨거운 물을 섞어 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽게 잘 느낄 수 있는 커피";

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

	@DisplayName("생성 - ItemCreateRequest의 price이 0 미만이면 응답에 실패하여 BadRequest를 반환한다. - 실패.")
	@ParameterizedTest
	@NullSource
	@ValueSource(ints = {-1, -43, -123})
	void createItem_priceValidation_fail(Integer price) throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String categoryName = "에스프레소";
		String name = "카페 아메리카노";
		String englishName = "Caffe Americano";
		String image = "http://hkbks.com/url";
		String description = "진한 에스프레소와 뜨거운 물을 섞어 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽게 잘 느낄 수 있는 커피";

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
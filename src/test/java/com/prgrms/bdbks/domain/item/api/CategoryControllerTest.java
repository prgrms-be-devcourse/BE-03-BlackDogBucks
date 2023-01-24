package com.prgrms.bdbks.domain.item.api;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.item.repository.ItemCategoryRepository;
import com.prgrms.bdbks.domain.testutil.ItemObjectProvider;

import lombok.RequiredArgsConstructor;

@Transactional
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class CategoryControllerTest {

	private static final String BASE_REQUEST_URI = "/api/v1/categories";

	private final ObjectMapper objectMapper;

	private final MockMvc mockMvc;

	private final ItemCategoryRepository itemCategoryRepository;

	@DisplayName("조회 - kinds 파라미터로 ItemCategory 목록을 조회한다. - 성공")
	@Test
	void findAllByType_success() throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		List<ItemCategory> beverages = ItemObjectProvider.createItemCategoriesByType(3, beverage,
			List.of("리저브 에스프레소", "콜드 브루", "에스프레소"), List.of("Reserve Espresso", "Cold Brew", "Espresso"));
		itemCategoryRepository.saveAll(beverages);

		ItemType food = ItemType.FOOD;
		List<ItemCategory> foods = ItemObjectProvider.createItemCategoriesByType(3, food, List.of("브레드", "스낵", "아이스크림"),
			List.of("bread", "snack", "Ice Cream"));
		itemCategoryRepository.saveAll(foods);

		// when
		mockMvc.perform(get(BASE_REQUEST_URI)
				// .with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.param("kinds", ItemType.BEVERAGE.name())
				.characterEncoding(StandardCharsets.UTF_8)
			)

			.andExpect(status().isOk())
			.andExpect(jsonPath("$.categories.length()").value(3))
			.andExpect(jsonPath("$.categories..['type']").value(hasItem(beverage.name())))
			.andExpect(jsonPath("$.categories[*].type").value(hasItem(beverage.name())))

			.andDo(print())
			.andDo(document("category-list",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestParameters(
					parameterWithName("kinds").description("아이템 타입")
				),
				responseFields(
					fieldWithPath("categories").type(JsonFieldType.ARRAY).description("카테고리 아이템 목록"),
					fieldWithPath("categories[].name").type(JsonFieldType.STRING).description("카테고리 이름"),
					fieldWithPath("categories[].categoryId").type(JsonFieldType.NUMBER).description("카테고리 id"),
					fieldWithPath("categories[].englishName").type(JsonFieldType.STRING).description("카테고리 영어 이름"),
					fieldWithPath("categories[].type").type(JsonFieldType.STRING).description("카테고리 종류")
				)
			));
	}

}
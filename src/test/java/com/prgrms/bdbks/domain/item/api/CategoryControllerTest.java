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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.WithMockCustomUser;
import com.prgrms.bdbks.WithMockCustomUserSecurityContextFactory;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.item.repository.ItemCategoryRepository;
import com.prgrms.bdbks.domain.store.service.StoreService;
import com.prgrms.bdbks.domain.testutil.ItemObjectProvider;
import com.prgrms.bdbks.domain.user.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Transactional
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class CategoryControllerTest {

	private static final String BASE_REQUEST_URI = "/api/v1/categories";

	private final MockMvc mockMvc;

	private final ItemCategoryRepository itemCategoryRepository;

	@MockBean
	private StoreService storeService;

	@DisplayName("?????? - kinds ??????????????? ItemCategory ????????? ????????????. - ??????")
	@Test
	@WithMockCustomUser
	void findAllByType_success() throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		List<ItemCategory> beverages = ItemObjectProvider.createItemCategoriesByType(3, beverage,
			List.of("????????? ???????????????", "?????? ??????", "???????????????"), List.of("Reserve Espresso", "Cold Brew", "Espresso"));
		itemCategoryRepository.saveAll(beverages);

		ItemType food = ItemType.FOOD;
		List<ItemCategory> foods = ItemObjectProvider.createItemCategoriesByType(3, food, List.of("?????????", "??????", "???????????????"),
			List.of("bread", "snack", "Ice Cream"));
		itemCategoryRepository.saveAll(foods);

		// when
		mockMvc.perform(get(BASE_REQUEST_URI)
				.header(HttpHeaders.AUTHORIZATION,
					JwtAuthenticationFilter.AUTHENTICATION_TYPE_PREFIX
						+ WithMockCustomUserSecurityContextFactory.mockUserToken)
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
					parameterWithName("kinds").description("????????? ??????")
				),
				responseFields(
					fieldWithPath("categories").type(JsonFieldType.ARRAY).description("???????????? ????????? ??????"),
					fieldWithPath("categories[].name").type(JsonFieldType.STRING).description("???????????? ??????"),
					fieldWithPath("categories[].categoryId").type(JsonFieldType.NUMBER).description("???????????? id"),
					fieldWithPath("categories[].englishName").type(JsonFieldType.STRING).description("???????????? ?????? ??????"),
					fieldWithPath("categories[].type").type(JsonFieldType.STRING).description("???????????? ??????")
				)
			));
	}

}
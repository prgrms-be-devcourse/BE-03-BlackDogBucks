package com.prgrms.bdbks.domain.item.api;

import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.bdbks.WithMockCustomUser;
import com.prgrms.bdbks.WithMockCustomUserSecurityContextFactory;
import com.prgrms.bdbks.domain.item.dto.DefaultOptionCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.entity.BeverageOption;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.item.repository.DefaultOptionRepository;
import com.prgrms.bdbks.domain.item.repository.ItemCategoryRepository;
import com.prgrms.bdbks.domain.item.repository.ItemRepository;
import com.prgrms.bdbks.domain.store.service.StoreService;
import com.prgrms.bdbks.domain.user.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@WithMockCustomUser
@Transactional
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class ItemControllerTest {

	private static final String BASE_REQUEST_URI = "/api/v1/items";

	private final ObjectMapper objectMapper;

	private final MockMvc mockMvc;

	private final ItemRepository itemRepository;

	private final ItemCategoryRepository itemCategoryRepository;

	private final DefaultOptionRepository defaultOptionRepository;

	@MockBean
	private StoreService storeService;

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

		ItemCategory itemCategory = createItemCategory(categoryName, "espresso", beverage);

		itemCategoryRepository.save(itemCategory);

		//when
		mockMvc.perform(post(BASE_REQUEST_URI)
				.header(HttpHeaders.AUTHORIZATION,
					JwtAuthenticationFilter.AUTHENTICATION_TYPE_PREFIX
						+ WithMockCustomUserSecurityContextFactory.mockUserToken)

				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsString(request)
				)
			)
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", containsString(BASE_REQUEST_URI)))
			.andDo(print())
			.andDo(document("item-create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("itemType").type(JsonFieldType.STRING)
						.description("????????? ??????"),
					fieldWithPath("categoryName").type(JsonFieldType.STRING).description("???????????? ??????"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("????????? ??????"),
					fieldWithPath("englishName").type(JsonFieldType.STRING).description("????????? ????????????"),
					fieldWithPath("price").type(JsonFieldType.NUMBER).description("????????? ??????"),
					fieldWithPath("image").type(JsonFieldType.STRING).description("????????? URL"),
					fieldWithPath("description").type(JsonFieldType.STRING).description("????????? ??????"),
					fieldWithPath("defaultOptionRequest").type(JsonFieldType.OBJECT).description("????????? ??????"),
					fieldWithPath("defaultOptionRequest.espressoShotCount").type(JsonFieldType.NUMBER)
						.description("??????????????? ??? ????????? ???"),
					fieldWithPath("defaultOptionRequest.vanillaSyrupCount").type(JsonFieldType.NUMBER)
						.description("????????? ?????? ??? ????????? ???"),
					fieldWithPath("defaultOptionRequest.classicSyrupCount").type(JsonFieldType.NUMBER)
						.description("????????? ?????? ??? ????????? ???"),
					fieldWithPath("defaultOptionRequest.hazelnutSyrupCount").type(JsonFieldType.NUMBER)
						.description("???????????? ?????? ??? ????????? ???"),
					fieldWithPath("defaultOptionRequest.milkType").type(JsonFieldType.NULL).description("?????? ??????"),
					fieldWithPath("defaultOptionRequest.espressoType").type(JsonFieldType.NULL).description("??????????????? ??????"),
					fieldWithPath("defaultOptionRequest.milkAmount").type(JsonFieldType.NULL).description("?????? ???")
				)
			));

	}

	@DisplayName("?????? - ItemType??? CategoryName??? ????????? ?????? ??????????????? item list??? ????????????. - ??????.")
	@Test
	void findAllBy_success() throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String espressoCategoryName = "???????????????";

		String coldBrewCategoryName = "?????? ??????";

		Integer espressoShotCount = 2;
		Integer vanillaSyrupCount = 0;
		Integer classicSyrupCount = 0;
		Integer hazelnutSyrupCount = 0;

		ItemCategory espresso = createItemCategory(espressoCategoryName, "espresso", beverage);

		itemCategoryRepository.save(espresso);

		ItemCategory coldBrew = createItemCategory(coldBrewCategoryName, "cold brew", beverage);

		itemCategoryRepository.save(coldBrew);

		DefaultOption defaultOption = createDefaultOption(espressoShotCount, vanillaSyrupCount, classicSyrupCount,
			hazelnutSyrupCount, null, null, null);

		defaultOptionRepository.save(defaultOption);

		itemRepository.saveAll(createItems(List.of("???????????? ?????? ??????", "?????? ???????????????", "?????? ??????"),
			List.of("Starbucks Dolce Latte", "Caffe Americano", "Caffe Latte"),
			List.of(5900, 4500, 5000),
			List.of("http://hkbks.com/url1", "http://hkbks.com/url2,", "http://hkbks.com/url3"),
			espresso,
			defaultOption
		));

		itemRepository.saveAll(createItems(List.of("?????? ??????", "?????? ?????? ??????", "????????? ?????? ?????? ??????", "?????? ?????? ?????? ??????"),
			List.of("Cold Brew", "Dolce Cold Brew", "Vanilla Cream Cold brew", "Cold Brew with Oat Milk"),
			List.of(4900, 6000, 5800, 5800),
			List.of("http://hkbks.com/url1", "http://hkbks.com/url2,", "http://hkbks.com/url3",
				"http://hkbks.com/url4"),
			coldBrew,
			defaultOption
		));

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		params.add("kinds", beverage.name());
		params.add("category", espressoCategoryName);

		//when
		mockMvc.perform(get(BASE_REQUEST_URI)
				.header(HttpHeaders.AUTHORIZATION,
					JwtAuthenticationFilter.AUTHENTICATION_TYPE_PREFIX
						+ WithMockCustomUserSecurityContextFactory.mockUserToken)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.params(params)
				.characterEncoding(StandardCharsets.UTF_8)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.categoryName").value(espressoCategoryName))
			.andExpect(jsonPath("$.items..['type']").value(hasItem(beverage.name())))
			.andExpect(jsonPath("$.items..['categoryName']").value(hasItem(espresso.getName())))
			.andExpect(jsonPath("$.items[*].itemId").exists())
			.andExpect(jsonPath("$.items[*].image").exists())
			.andExpect(jsonPath("$.items[*].name").exists())
			.andExpect(jsonPath("$.items[*].englishName").exists())
			.andExpect(jsonPath("$.items[*].price").exists())
			.andExpect(jsonPath("$.items[*].isNew").exists())
			.andExpect(jsonPath("$.items[*].isBest").exists())
			.andDo(print())
			.andDo(document("item-list",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestParameters(
					parameterWithName("kinds").description("????????? ??????"),
					parameterWithName("category").description("???????????? ??????")
				),
				responseFields(
					fieldWithPath("categoryName").type(JsonFieldType.STRING).description("???????????? ??????"),
					fieldWithPath("items[].itemId").type(JsonFieldType.NUMBER).description("????????? Id"),
					fieldWithPath("items[].image").type(JsonFieldType.STRING).description("????????? ?????????"),
					fieldWithPath("items[].name").type(JsonFieldType.STRING).description("????????? ??????"),
					fieldWithPath("items[].englishName").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
					fieldWithPath("items[].price").type(JsonFieldType.NUMBER).description("????????? ??????"),
					fieldWithPath("items[].isNew").type(JsonFieldType.BOOLEAN).description("??? ????????? ??????"),
					fieldWithPath("items[].isBest").type(JsonFieldType.BOOLEAN).description("?????? ?????? ?????? ??????"),
					fieldWithPath("items[].type").type(JsonFieldType.STRING).description("???????????? ??????"),
					fieldWithPath("items[].categoryName").type(JsonFieldType.STRING).description("???????????? ??????")

				)
			));

	}

	@DisplayName("?????? - ?????? ??????????????? item??? ????????? empty List??? ????????????. - ??????.")
	@Test
	void findAllBy_empty_success() throws Exception {
		//given
		String categoryName = "?????????";
		ItemType beverage = ItemType.BEVERAGE;

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		params.add("kinds", beverage.name());
		params.add("category", categoryName);

		//when
		mockMvc.perform(get(BASE_REQUEST_URI)
				.header(HttpHeaders.AUTHORIZATION,
					JwtAuthenticationFilter.AUTHENTICATION_TYPE_PREFIX
						+ WithMockCustomUserSecurityContextFactory.mockUserToken)
				.contentType(MediaType.APPLICATION_JSON)
				.params(params)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.categoryName").value(categoryName))
			.andExpect(jsonPath("$.items").isEmpty())
			.andDo(print());
	}

	@DisplayName("?????? - ItemId??? Item??? ?????????????????? ??????????????? ????????????. null??? ????????? ???????????? ????????? - ??????.")
	@Test
	void findItemDetail_success() throws Exception {
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
		Integer classicSyrupCount = null;
		Integer hazelnutSyrupCount = 0;

		ItemCategory itemCategory = createItemCategory(categoryName, "espresso", beverage);

		itemCategoryRepository.save(itemCategory);

		DefaultOption defaultOption = createDefaultOption(espressoShotCount, vanillaSyrupCount, classicSyrupCount,
			hazelnutSyrupCount, null, BeverageOption.Coffee.ESPRESSO, null);

		defaultOptionRepository.save(defaultOption);

		Item newItem = itemRepository.save(
			createItem(name, itemCategory, englishName, price, image, description, defaultOption));

		//when
		mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_REQUEST_URI + "/{itemId}", newItem.getId())
				.header(HttpHeaders.AUTHORIZATION,
					JwtAuthenticationFilter.AUTHENTICATION_TYPE_PREFIX
						+ WithMockCustomUserSecurityContextFactory.mockUserToken)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.itemId").value(newItem.getId()))
			.andExpect(jsonPath("$.image").value(image))
			.andExpect(jsonPath("$.name").value(name))
			.andExpect(jsonPath("$.englishName").value(englishName))
			.andExpect(jsonPath("$.price").value(price))
			.andExpect(jsonPath("$.isNew").value(false))
			.andExpect(jsonPath("$.isBest").value(false))
			.andExpect(jsonPath("$.option.coffee.defaultType").value(BeverageOption.Coffee.ESPRESSO.getKorName()))
			.andExpect(jsonPath("$.option.coffee.defaultEspressoCount").value(2))
			.andExpect(jsonPath("$.option.coffee..['options']")
				.value(containsInAnyOrder(Arrays.stream(BeverageOption.Coffee.values())
					.map(BeverageOption.Coffee::getKorName)
					.collect(Collectors.toList())
				)))
			.andExpect(jsonPath("$.option.syrup[*].defaultCount")
				.value(hasItem(0)))
			.andExpect(jsonPath("$.option.syrup[*].syrupName")
				.value(containsInAnyOrder("????????? ??????", "???????????? ??????")))
			.andExpect(jsonPath("$.option.size[*].name")
				.value(containsInAnyOrder(Arrays.stream(BeverageOption.Size.values())
					.map(BeverageOption.Size::getEnglishName)
					.toArray(String[]::new))))
			.andExpect(jsonPath("$.option.size[*].amount").
				value(
					containsInAnyOrder(Arrays.stream(BeverageOption.Size.values())
						.map(BeverageOption.Size::getAmount)
						.toArray(String[]::new))))
			.andExpect(jsonPath("$.option.cupType[*]")
				.value(containsInAnyOrder(Arrays.stream(BeverageOption.CupType.values())
					.map(BeverageOption.CupType::getKorName)
					.toArray(String[]::new))))
			.andDo(document("item-detail",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("itemId").description("????????? Id")
				),
				responseFields(
					fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("????????? Id"),
					fieldWithPath("image").type(JsonFieldType.STRING).description("????????? ?????????"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("????????? ??????"),
					fieldWithPath("englishName").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
					fieldWithPath("price").type(JsonFieldType.NUMBER).description("????????? ??????"),
					fieldWithPath("isNew").type(JsonFieldType.BOOLEAN).description("??? ????????? ??????"),
					fieldWithPath("isBest").type(JsonFieldType.BOOLEAN).description("?????? ?????? ?????? ??????"),
					fieldWithPath("option.coffee.defaultType").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
					fieldWithPath("option.coffee.defaultEspressoCount").type(JsonFieldType.NUMBER)
						.description("????????? ?????? ??? ??????"),
					fieldWithPath("option.coffee.options[]").type(JsonFieldType.ARRAY).description("?????? ??????"),
					fieldWithPath("option.syrup[]").type(JsonFieldType.ARRAY).description("?????? ??????"),
					fieldWithPath("option.syrup[].syrupName").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
					fieldWithPath("option.syrup[].defaultCount").type(JsonFieldType.NUMBER).description("????????? ?????? ?????? ??????"),
					fieldWithPath("option.size[]").type(JsonFieldType.ARRAY).description("??? ??????"),
					fieldWithPath("option.size[].name").type(JsonFieldType.STRING).description("??? ?????? ??????"),
					fieldWithPath("option.size[].amount").type(JsonFieldType.STRING).description("??? ?????? ?????? ???"),
					fieldWithPath("option.cupType[]").type(JsonFieldType.ARRAY).description("?????? ?????? ??????")

				)
			));

	}

	@DisplayName("?????? - ItemId??? ????????? ??? Item??? ????????? 404??? ????????????. - ??????")
	@Test
	void findItemDetail_fail() throws Exception {
		//given
		long itemId = 0L;

		//when
		mockMvc.perform(get(BASE_REQUEST_URI + "/{itemId}", itemId)
				.header(HttpHeaders.AUTHORIZATION,
					JwtAuthenticationFilter.AUTHENTICATION_TYPE_PREFIX
						+ WithMockCustomUserSecurityContextFactory.mockUserToken)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
			)
			.andExpect(status().isNotFound())
			.andDo(print());
	}

}
package com.prgrms.bdbks.domain.item.api;

import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import lombok.RequiredArgsConstructor;

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

		ItemCategory itemCategory = createItemCategory(categoryName, "espresso", beverage);

		itemCategoryRepository.save(itemCategory);

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
			.andExpect(header().string("Location", containsString(BASE_REQUEST_URI)))
			.andDo(print())
			.andDo(document("item-create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("itemType").type(JsonFieldType.STRING)
						.description("아이템 타입"),
					fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리 이름"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("아이템 이름"),
					fieldWithPath("englishName").type(JsonFieldType.STRING).description("아이템 영어이름"),
					fieldWithPath("price").type(JsonFieldType.NUMBER).description("아이템 가격"),
					fieldWithPath("image").type(JsonFieldType.STRING).description("이미지 URL"),
					fieldWithPath("description").type(JsonFieldType.STRING).description("아이템 설명"),
					fieldWithPath("defaultOptionRequest").type(JsonFieldType.OBJECT).description("디폴트 옵션"),
					fieldWithPath("defaultOptionRequest.espressoShotCount").type(JsonFieldType.NUMBER)
						.description("에스프레소 샷 카운트 수"),
					fieldWithPath("defaultOptionRequest.vanillaSyrupCount").type(JsonFieldType.NUMBER)
						.description("바닐라 시럽 샷 카운트 수"),
					fieldWithPath("defaultOptionRequest.classicSyrupCount").type(JsonFieldType.NUMBER)
						.description("클래식 시럽 샷 카운트 수"),
					fieldWithPath("defaultOptionRequest.hazelnutSyrupCount").type(JsonFieldType.NUMBER)
						.description("헤이즐넛 시럽 샷 카운트 수"),
					fieldWithPath("defaultOptionRequest.milkType").type(JsonFieldType.NULL).description("우유 타입"),
					fieldWithPath("defaultOptionRequest.espressoType").type(JsonFieldType.NULL).description("에스프레소 타입"),
					fieldWithPath("defaultOptionRequest.milkAmount").type(JsonFieldType.NULL).description("우유 양")
				)
			));

	}

	@DisplayName("조회 - ItemType과 CategoryName을 받아서 해당 카테고리의 item list를 반환한다. - 성공.")
	@Test
	void findAllBy_success() throws Exception {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String espressoCategoryName = "에스프레소";

		String coldBrewCategoryName = "콜드 브루";

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

		itemRepository.saveAll(createItems(List.of("스타벅스 돌체 라떼", "카페 아메리카노", "카페 라떼"),
			List.of("Starbucks Dolce Latte", "Caffe Americano", "Caffe Latte"),
			List.of(5900, 4500, 5000),
			List.of("http://hkbks.com/url1", "http://hkbks.com/url2,", "http://hkbks.com/url3"),
			espresso,
			defaultOption
		));

		itemRepository.saveAll(createItems(List.of("콜드 브루", "돌체 콜드 브루", "바닐라 크림 콜드 브루", "콜드 브루 오트 라떼"),
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
					parameterWithName("kinds").description("아이템 타입"),
					parameterWithName("category").description("카테고리 이름")
				),
				responseFields(
					fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리 이름"),
					fieldWithPath("items[].itemId").type(JsonFieldType.NUMBER).description("아이템 Id"),
					fieldWithPath("items[].image").type(JsonFieldType.STRING).description("아이템 이미지"),
					fieldWithPath("items[].name").type(JsonFieldType.STRING).description("아이템 이름"),
					fieldWithPath("items[].englishName").type(JsonFieldType.STRING).description("아이템 영어 이름"),
					fieldWithPath("items[].price").type(JsonFieldType.NUMBER).description("아이템 가격"),
					fieldWithPath("items[].isNew").type(JsonFieldType.BOOLEAN).description("새 아이템 여부"),
					fieldWithPath("items[].isBest").type(JsonFieldType.BOOLEAN).description("인기 많은 상품 여부"),
					fieldWithPath("items[].type").type(JsonFieldType.STRING).description("카테고리 종류"),
					fieldWithPath("items[].categoryName").type(JsonFieldType.STRING).description("카테고리 이름")

				)
			));

	}

	@DisplayName("조회 - 해당 카테고리의 item이 없다면 empty List를 반환한다. - 성공.")
	@Test
	void findAllBy_empty_success() throws Exception {
		//given
		String categoryName = "미지근";
		ItemType beverage = ItemType.BEVERAGE;

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		params.add("kinds", beverage.name());
		params.add("category", categoryName);

		//when
		mockMvc.perform(get(BASE_REQUEST_URI)
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

	@DisplayName("조회 - ItemId로 Item을 상세조회하고 옵션목록을 보여준다. null인 옵션은 보여주지 않는다 - 성공.")
	@Test
	void findItemDetail_success() throws Exception {
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
				.value(containsInAnyOrder("바닐라 시럽", "헤이즐넛 시럽")))
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
					parameterWithName("itemId").description("아이템 Id")
				),
				responseFields(
					fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("아이템 Id"),
					fieldWithPath("image").type(JsonFieldType.STRING).description("아이템 이미지"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("아이템 이름"),
					fieldWithPath("englishName").type(JsonFieldType.STRING).description("아이템 영어 이름"),
					fieldWithPath("price").type(JsonFieldType.NUMBER).description("아이템 가격"),
					fieldWithPath("isNew").type(JsonFieldType.BOOLEAN).description("새 아이템 여부"),
					fieldWithPath("isBest").type(JsonFieldType.BOOLEAN).description("인기 많은 상품 여부"),
					fieldWithPath("option.coffee.defaultType").type(JsonFieldType.STRING).description("디폴트 커피 타입"),
					fieldWithPath("option.coffee.defaultEspressoCount").type(JsonFieldType.NUMBER)
						.description("디폴트 커피 샷 개수"),
					fieldWithPath("option.coffee.options[]").type(JsonFieldType.ARRAY).description("커피 옵션"),
					fieldWithPath("option.syrup[]").type(JsonFieldType.ARRAY).description("시럽 옵션"),
					fieldWithPath("option.syrup[].syrupName").type(JsonFieldType.STRING).description("시럽 옵션 이름"),
					fieldWithPath("option.syrup[].defaultCount").type(JsonFieldType.NUMBER).description("디폴트 시럽 옵션 개수"),
					fieldWithPath("option.size[]").type(JsonFieldType.ARRAY).description("컵 옵션"),
					fieldWithPath("option.size[].name").type(JsonFieldType.STRING).description("컵 옵션 이름"),
					fieldWithPath("option.size[].amount").type(JsonFieldType.STRING).description("컵 옵션 음료 양"),
					fieldWithPath("option.cupType[]").type(JsonFieldType.ARRAY).description("시럽 옵션 이름")

				)
			));

	}

	@DisplayName("조회 - ItemId로 조회할 때 Item이 없다면 404를 응답한다. - 실패")
	@Test
	void findItemDetail_fail() throws Exception {
		//given
		long itemId = 0L;

		//when
		mockMvc.perform(get(BASE_REQUEST_URI + "/{itemId}", itemId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
			)
			.andExpect(status().isNotFound())
			.andDo(print());
	}

}
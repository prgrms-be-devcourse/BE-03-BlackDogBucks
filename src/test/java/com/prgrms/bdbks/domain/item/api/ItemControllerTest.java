package com.prgrms.bdbks.domain.item.api;

import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

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
import com.prgrms.bdbks.domain.item.dto.DefaultOptionCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.item.repository.ItemCategoryRepository;

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

	private final ItemCategoryRepository itemCategoryRepository;

	@DisplayName("생성 - Item 을 생성하고 생성된 Resource 의 URI 를 리턴한다 - 성공.")
	@Test
	void create_item_success() throws Exception {
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

}
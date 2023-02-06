package com.prgrms.bdbks.domain.item.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import com.prgrms.bdbks.domain.item.service.ItemCategoryService;

import lombok.RequiredArgsConstructor;

@WithMockUser
@AutoConfigureMockMvc
@WebMvcTest(controllers = CategoryController.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class CategoryControllerWebMvcTest {

	private static final String BASE_REQUEST_URI = "/api/v1/categories";

	@MockBean
	private final ItemCategoryService categoryService;

	private final MockMvc mockMvc;

	@DisplayName("조회 - 잘못된 kinds 파라미터로 ItemCategory 목록을 조회한다. - 실패")
	@ParameterizedTest
	@ValueSource(strings = {"음료", "응가", "veberage", "cood"})
	void findAllByType_fail(String itemType) throws Exception {
		//when
		mockMvc.perform(get(BASE_REQUEST_URI)
				// .with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.param("kinds", itemType)
				.characterEncoding(StandardCharsets.UTF_8)
			)
			.andExpect(status().isBadRequest())
			.andDo(print());
	}
}
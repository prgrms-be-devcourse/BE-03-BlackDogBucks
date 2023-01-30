package com.prgrms.bdbks.domain.card.api;

import static com.prgrms.bdbks.domain.testutil.CardObjectProvider.*;
import static com.prgrms.bdbks.domain.testutil.UserObjectProvider.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.bdbks.domain.card.dto.CardSaveRequest;
import com.prgrms.bdbks.domain.card.dto.CardSaveResponse;
import com.prgrms.bdbks.domain.card.entity.Card;
import com.prgrms.bdbks.domain.card.repository.CardRepository;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class CardControllerTest {

	private static final String BASE_REQUEST_URI = "/api/v1/cards";

	private final HttpSession httpSession;

	private final UserRepository userRepository;

	private final CardRepository cardRepository;

	private static final String SESSION_USER = "user";

	private final ObjectMapper objectMapper;

	private final MockMvc mockMvc;

	private User user;

	private Card card;

	@BeforeEach
	void setUp() {
		user = createUser();
		userRepository.save(user);
		card = createCard(user);
		cardRepository.save(card);
		httpSession.setAttribute(SESSION_USER, user);
	}

	@DisplayName("create - 사용자의 충전카드를 등록한다. - 성공")
	@Test
	void create_ValidParameters_Success() throws Exception {

		String name = "normal";

		CardSaveRequest cardSaveRequest = new CardSaveRequest(name);

		String jsonRequest = objectMapper.writeValueAsString(cardSaveRequest);

		mockMvc.perform(post(BASE_REQUEST_URI)
				.sessionAttr("user", user)
				.contentType(APPLICATION_JSON)
				.content(jsonRequest)
				.accept(APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", containsString(BASE_REQUEST_URI)))
			.andExpect(redirectedUrlPattern("http://localhost:8080/api/v1/cards/*"))

			.andDo(print())
			.andDo(document("card-create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("name").type(JsonFieldType.STRING).description("카드 이름")
				)
			));

	}

	@DisplayName("getCard - 사용자의 충전카드를 단건 조회한다. - 성공")
	@Test
	void getCard_ValidParameters_Success() throws Exception {
		String cardId = card.getChargeCardId();

		CardSaveResponse cardSaveResponse = new CardSaveResponse(cardId);

		String jsonResponse = objectMapper.writeValueAsString(cardSaveResponse);

		mockMvc.perform(get(BASE_REQUEST_URI + "/{cardId}", cardId)
				.sessionAttr("user", user)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(APPLICATION_JSON))
			.andExpect(content().json(jsonResponse))
			.andExpect(jsonPath("$.chargeCardId").value(cardId))
			.andExpect(jsonPath("$.name").value(card.getName()))
			.andExpect(jsonPath("$.amount").value(card.getAmount()))

			.andDo(print())
			.andDo(document("card-findOne",
				responseFields(
					fieldWithPath("chargeCardId").type(JsonFieldType.STRING).description("카드 Id"),
					fieldWithPath("name").type(JsonFieldType.STRING)
						.description("카드 이름"),
					fieldWithPath("amount").type(JsonFieldType.NUMBER).description("카드 금액")
				)
			));
	}
}
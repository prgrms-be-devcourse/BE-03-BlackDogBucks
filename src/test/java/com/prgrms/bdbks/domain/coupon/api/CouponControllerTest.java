package com.prgrms.bdbks.domain.coupon.api;

import static com.prgrms.bdbks.domain.testutil.CouponObjectProvider.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.WithMockCustomUser;
import com.prgrms.bdbks.WithMockCustomUserSecurityContextFactory;
import com.prgrms.bdbks.domain.coupon.entity.Coupon;
import com.prgrms.bdbks.domain.coupon.repository.CouponRepository;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.jwt.JwtAuthenticationFilter;
import com.prgrms.bdbks.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Transactional
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class CouponControllerTest {

	private static final String BASE_REQUEST_URI = "/api/v1/coupons";

	private final CouponRepository couponRepository;

	private final MockMvc mockMvc;

	private final UserService userService;

	private List<Coupon> coupons;

	@BeforeEach
	void setUp() {
		User user = userService.findUser("blackDog").get();

		coupons = createCoupon(user.getId());
		couponRepository.saveAll(coupons);

	}

	@DisplayName("findUnusedCoupon - 사용하지 않은 사용자의 쿠폰을 조회한다. - 성공")
	@Test
	@WithMockCustomUser
	void findUnusedCoupon_ValidUser_Success() throws Exception {

		mockMvc.perform(get(BASE_REQUEST_URI + "/detail")
				.param("used", "false")
				.header(HttpHeaders.AUTHORIZATION, JwtAuthenticationFilter.AUTHENTICATION_TYPE_PREFIX
					+ WithMockCustomUserSecurityContextFactory.mockUserToken)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.coupons").exists())
			.andExpect(jsonPath("$.coupons[0].id").value(coupons.get(0).getId()))
			.andExpect(jsonPath("$.coupons[0].userId").value(coupons.get(0).getUserId()))
			.andExpect(jsonPath("$.coupons[0].name").value(coupons.get(0).getName()))
			.andExpect(jsonPath("$.coupons[0].price").value(coupons.get(0).getPrice()))
			.andExpect(jsonPath("$.coupons[0].expireDate").exists())
			.andExpect(jsonPath("$.coupons[0].used").value(coupons.get(0).isUsed()))
			.andDo(print())
			.andDo(document("coupon-findUnused",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestParameters(
					parameterWithName("used").description("조회할 쿠폰 사용 여부")
				),

				responseFields(
					fieldWithPath("coupons.[].id").type(JsonFieldType.NUMBER).description("쿠폰 Id"),
					fieldWithPath("coupons.[].userId").type(JsonFieldType.NUMBER)
						.description("쿠폰 소유자 Id"),
					fieldWithPath("coupons.[].name").type(JsonFieldType.STRING).description("쿠폰 이름"),
					fieldWithPath("coupons.[].price").type(JsonFieldType.NUMBER).description("쿠폰 할인 가격"),
					fieldWithPath("coupons.[].expireDate").type(JsonFieldType.STRING)
						.description("쿠폰 만료일"),
					fieldWithPath("coupons.[].used").type(JsonFieldType.BOOLEAN).description("쿠폰 사용 여부")
				)
			));
	}

	@DisplayName("findAll - 사용자의 모든 쿠폰을 조회한다. - 성공")
	@Test
	@WithMockCustomUser
	void findAll_ValidUser_Success() throws Exception {
		mockMvc.perform(get(BASE_REQUEST_URI)
				.header(HttpHeaders.AUTHORIZATION, JwtAuthenticationFilter.AUTHENTICATION_TYPE_PREFIX
					+ WithMockCustomUserSecurityContextFactory.mockUserToken)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.coupons").exists())
			.andExpect(jsonPath("$.coupons[0].id").value(coupons.get(0).getId()))
			.andExpect(jsonPath("$.coupons[0].userId").value(coupons.get(0).getUserId()))
			.andExpect(jsonPath("$.coupons[0].name").value(coupons.get(0).getName()))
			.andExpect(jsonPath("$.coupons[0].price").value(coupons.get(0).getPrice()))
			.andExpect(jsonPath("$.coupons[0].expireDate").exists())
			.andExpect(jsonPath("$.coupons[0].used").value(coupons.get(0).isUsed()))
			.andDo(print())
			.andDo(document("coupon-findAll",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("coupons.[].id").type(JsonFieldType.NUMBER).description("쿠폰 Id"),
					fieldWithPath("coupons.[].userId").type(JsonFieldType.NUMBER)
						.description("쿠폰 소유자 Id"),
					fieldWithPath("coupons.[].name").type(JsonFieldType.STRING).description("쿠폰 이름"),
					fieldWithPath("coupons.[].price").type(JsonFieldType.NUMBER).description("쿠폰 할인 가격"),
					fieldWithPath("coupons.[].expireDate").type(JsonFieldType.STRING)
						.description("쿠폰 만료일"),
					fieldWithPath("coupons.[].used").type(JsonFieldType.BOOLEAN).description("쿠폰 사용 여부")
				)
			));
	}

}
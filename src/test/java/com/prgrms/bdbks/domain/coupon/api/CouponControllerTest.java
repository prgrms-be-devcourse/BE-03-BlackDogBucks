package com.prgrms.bdbks.domain.coupon.api;

import static com.prgrms.bdbks.domain.testutil.CouponObjectProvider.*;
import static com.prgrms.bdbks.domain.testutil.UserObjectProvider.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

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

import com.prgrms.bdbks.domain.coupon.entity.Coupon;
import com.prgrms.bdbks.domain.coupon.repository.CouponRepository;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class CouponControllerTest {

	private final HttpSession httpSession;

	private final UserRepository userRepository;

	private final CouponRepository couponRepository;

	private static final String SESSION_USER = "user";

	private final MockMvc mockMvc;

	private User user;

	private List<Coupon> coupons;

	@BeforeEach
	void setUp() {
		user = createUser();
		userRepository.save(user);
		coupons = createCoupon(user.getId());
		couponRepository.saveAll(coupons);
		httpSession.setAttribute(SESSION_USER, user);
	}

	@DisplayName("findUnusedCoupon - 사용하지 않은 사용자의 쿠폰을 조회한다. - 성공")
	@Test
	void findUnusedCoupon_ValidUser_Success() throws Exception {

		mockMvc.perform(get("/api/v1/coupons/detail")
				.param("used", "false")
				.sessionAttr("user", user)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.couponSearchResponses").exists())
			.andExpect(jsonPath("$.couponSearchResponses[0].couponId").value(coupons.get(0).getCouponId()))
			.andExpect(jsonPath("$.couponSearchResponses[0].userId").value(coupons.get(0).getUserId()))
			.andExpect(jsonPath("$.couponSearchResponses[0].name").value(coupons.get(0).getName()))
			.andExpect(jsonPath("$.couponSearchResponses[0].price").value(coupons.get(0).getPrice()))
			.andExpect(jsonPath("$.couponSearchResponses[0].expireDate").exists())
			.andExpect(jsonPath("$.couponSearchResponses[0].used").value(coupons.get(0).isUsed()))
			.andDo(print())
			.andDo(document("coupon-findUnused",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestParameters(
					parameterWithName("used").description("조회할 쿠폰 사용 여부")
				),

				responseFields(
					fieldWithPath("couponSearchResponses.[].couponId").type(JsonFieldType.NUMBER).description("쿠폰 Id"),
					fieldWithPath("couponSearchResponses.[].userId").type(JsonFieldType.NUMBER)
						.description("쿠폰 소유자 Id"),
					fieldWithPath("couponSearchResponses.[].name").type(JsonFieldType.STRING).description("쿠폰 이름"),
					fieldWithPath("couponSearchResponses.[].price").type(JsonFieldType.NUMBER).description("쿠폰 할인 가격"),
					fieldWithPath("couponSearchResponses.[].expireDate").type(JsonFieldType.ARRAY)
						.description("쿠폰 만료일"),
					fieldWithPath("couponSearchResponses.[].used").type(JsonFieldType.BOOLEAN).description("쿠폰 사용 여부")
				)
			));
	}

	@DisplayName("findAll - 사용자의 모든 쿠폰을 조회한다. - 성공")
	@Test
	void findAll_ValidUser_Success() throws Exception {
		mockMvc.perform(get("/api/v1/coupons")
				.sessionAttr("user", user)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.couponSearchResponses").exists())
			.andExpect(jsonPath("$.couponSearchResponses[0].couponId").value(coupons.get(0).getCouponId()))
			.andExpect(jsonPath("$.couponSearchResponses[0].userId").value(coupons.get(0).getUserId()))
			.andExpect(jsonPath("$.couponSearchResponses[0].name").value(coupons.get(0).getName()))
			.andExpect(jsonPath("$.couponSearchResponses[0].price").value(coupons.get(0).getPrice()))
			.andExpect(jsonPath("$.couponSearchResponses[0].expireDate").exists())
			.andExpect(jsonPath("$.couponSearchResponses[0].used").value(coupons.get(0).isUsed()))
			.andDo(print())
			.andDo(document("coupon-findAll",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("couponSearchResponses.[].couponId").type(JsonFieldType.NUMBER).description("쿠폰 Id"),
					fieldWithPath("couponSearchResponses.[].userId").type(JsonFieldType.NUMBER)
						.description("쿠폰 소유자 Id"),
					fieldWithPath("couponSearchResponses.[].name").type(JsonFieldType.STRING).description("쿠폰 이름"),
					fieldWithPath("couponSearchResponses.[].price").type(JsonFieldType.NUMBER).description("쿠폰 할인 가격"),
					fieldWithPath("couponSearchResponses.[].expireDate").type(JsonFieldType.ARRAY)
						.description("쿠폰 만료일"),
					fieldWithPath("couponSearchResponses.[].used").type(JsonFieldType.BOOLEAN).description("쿠폰 사용 여부")
				)
			));
	}
}
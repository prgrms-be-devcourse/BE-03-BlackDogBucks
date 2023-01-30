package com.prgrms.bdbks.domain.user.api;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@SpringBootTest(properties = {"spring.profiles.active=test"})
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class UserControllerIntegrationTest {

	private final MockMvc mockMvc;
	private final AuthController userController;
	private final UserRepository userRepository;

	private String token;

	@BeforeEach
	void setup() {
		UserCreateRequest userCreateRequest = new UserCreateRequest("user123", "password", "nickname",
			LocalDate.now().minusYears(10L),
			"01012341234", "user@naver.com");
		ResponseEntity<?> responseEntity = userController.signup(userCreateRequest);
		token = Objects.requireNonNull(responseEntity.getHeaders().get("Authorization")).get(0);
	}

	@Test
	void test() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/me")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", token)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void test2() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/me")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", token + "123sad")
			)
			.andDo(print())
			.andExpect(status().isUnauthorized());
		// .andExpect(status().isOk());
	}

}

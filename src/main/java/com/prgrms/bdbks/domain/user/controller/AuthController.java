package com.prgrms.bdbks.domain.user.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.bdbks.domain.user.converter.UserMapper;
import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.dto.UserFindResponse;
import com.prgrms.bdbks.domain.user.dto.UserLoginRequest;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.service.DefaultUserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
	private final DefaultUserService defaultUserService;

	@PostMapping(value = {"/signup"})
	public ResponseEntity<String> signup(@RequestBody @Valid UserCreateRequest userCreateRequest) {
		if (defaultUserService.findUser(userCreateRequest.getLoginId()).isPresent()) {
			return new ResponseEntity<>("Sign Up Failed", HttpStatus.BAD_REQUEST);
		} else {
			this.defaultUserService.register(userCreateRequest);
			return new ResponseEntity<>("Sign Up Completed", HttpStatus.CREATED);
		}
	}

	@PostMapping(value = {"/login"})
	public ResponseEntity<String> login(@RequestBody @Valid UserLoginRequest request, HttpSession session) {
		Optional<User> user = this.defaultUserService.login(request.getLoginId(), request.getPassword());
		if (user.isPresent()) {
			session.setAttribute("user", user.get());
			return new ResponseEntity<>("Login Succeeded", HttpStatus.OK);
		}
		return new ResponseEntity<>("Login Failed", HttpStatus.UNAUTHORIZED);
	}

	@PostMapping(value = {"/logout"})
	public ResponseEntity<Void> logout(HttpSession session) {
		User user = (User)session.getAttribute("user");
		if (user != null) {
			session.removeAttribute("user");
			session.invalidate();
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}

}
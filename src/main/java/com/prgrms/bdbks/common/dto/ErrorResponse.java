package com.prgrms.bdbks.common.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

	private int status;

	private String message;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime timestamp;

	private List<FieldError> errors;

	private String path;

	public ErrorResponse(int status, String message, LocalDateTime timestamp,
		List<FieldError> errors,
		String path) {
		this.status = status;
		this.message = message;
		this.timestamp = timestamp;
		this.errors = errors;
		this.path = path;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class FieldError {

		private String fieldName;

		private String inputValue;

		private String reason;

		public static FieldError of(String filedName, Object value, String reason) {
			return new FieldError(filedName, value.toString(), reason);
		}
	}

	public static ErrorResponse of(HttpStatus status, String message, String path,
		List<FieldError> errors) {
		return new ErrorResponse(status.value(), message, LocalDateTime.now(), errors, path);
	}

	public static ErrorResponse badRequest(String message, String path, List<FieldError> errors) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message,
			LocalDateTime.now(), errors, path);
	}

	public static ErrorResponse badRequest(String message, String path) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message,
			LocalDateTime.now(), null, path);
	}

	public static ErrorResponse notFound(String message, String path, List<FieldError> errors) {
		return new ErrorResponse(HttpStatus.NOT_FOUND.value(), message, LocalDateTime.now(), errors, path);
	}

	public static ErrorResponse unAuthorized(String message, String path, List<FieldError> errors) {
		return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), message, LocalDateTime.now(), errors, path);
	}

	public static ErrorResponse forbidden(String message, String path, List<FieldError> errors) {
		return new ErrorResponse(HttpStatus.FORBIDDEN.value(), message, LocalDateTime.now(), errors, path);
	}

}


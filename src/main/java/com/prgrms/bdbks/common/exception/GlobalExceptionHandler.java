package com.prgrms.bdbks.common.exception;

import static org.springframework.http.HttpStatus.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.prgrms.bdbks.common.dto.ErrorResponse;
import com.prgrms.bdbks.common.dto.ErrorResponse.FieldError;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = EntityNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
		EntityNotFoundException e, HttpServletRequest request) {

		return new ResponseEntity<>(
			ErrorResponse.notFound(e.getMessage(), request.getRequestURI(), null),
			HttpStatus.NOT_FOUND
		);
	}

	@ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
		MethodArgumentTypeMismatchException e, HttpServletRequest request) {

		ErrorResponse errorResponse = ErrorResponse.badRequest(e.getParameter().getParameterName(),
			request.getRequestURI(),
			List.of(new FieldError(e.getName(), Objects.requireNonNull(e.getValue()).toString(), e.getMessage())));

		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(value = NullPointerException.class)
	public ResponseEntity<ErrorResponse> handleNullPointException(
		NullPointerException e, HttpServletRequest request) {

		return ResponseEntity.badRequest()
			.body(ErrorResponse.badRequest(e.getMessage(), request.getRequestURI(), null));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<ErrorResponse> handleConstraintViolationException(
		HttpServletRequest request, ConstraintViolationException e) {

		return ResponseEntity.badRequest()
			.body(ErrorResponse.badRequest(e.getMessage(), request.getRequestURI()
				, makeFieldErrorsFromConstraintViolations(e.getConstraintViolations())));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(
		HttpServletRequest request, SQLIntegrityConstraintViolationException e) {

		return ResponseEntity.badRequest()
			.body(ErrorResponse.badRequest(e.getMessage(), request.getRequestURI()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(
		HttpServletRequest request, IllegalArgumentException e) {

		return ResponseEntity.badRequest()
			.body(ErrorResponse.badRequest(e.getMessage(), request.getRequestURI()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		HttpServletRequest request, MethodArgumentNotValidException e) {

		return ResponseEntity.badRequest().body(ErrorResponse.badRequest(
			e.getMessage(), request.getRequestURI(),
			makeFieldErrorsFromBindingResult(e.getBindingResult())
		));
	}

	@ExceptionHandler(InvalidFormatException.class)
	protected ResponseEntity<ErrorResponse> handleInvalidFormatException(
		HttpServletRequest request, InvalidFormatException e) {

		return ResponseEntity.badRequest()
			.body(ErrorResponse.badRequest(e.getMessage(), request.getRequestURI()));
	}

	@ExceptionHandler(AuthorityNotFoundException.class)
	protected ResponseEntity<ErrorResponse> handleAuthorityNotFoundException(
		HttpServletRequest request, AuthorityNotFoundException e) {

		return ResponseEntity.status(FORBIDDEN)
			.body(ErrorResponse.badRequest(e.getMessage(), request.getRequestURI()));
	}

	private List<FieldError> makeFieldErrorsFromBindingResult(BindingResult bindingResult) {
		List<FieldError> fieldErrors = new ArrayList<>();

		for (org.springframework.validation.FieldError fieldError : bindingResult.getFieldErrors()) {
			FieldError error = FieldError.of(fieldError.getField(), Objects.requireNonNull(
				fieldError.getRejectedValue()), fieldError.getDefaultMessage());
			fieldErrors.add(error);
		}

		return fieldErrors;
	}

	private List<FieldError> makeFieldErrorsFromConstraintViolations(
		Set<ConstraintViolation<?>> constraintViolations) {

		return constraintViolations.stream()
			.map(violation -> FieldError.of(getFieldFromPath(violation.getPropertyPath()),
				violation.getInvalidValue(), violation.getMessage()))
			.collect(Collectors.toList());
	}

	private String getFieldFromPath(Path fieldPath) {
		PathImpl pathImpl = (PathImpl)fieldPath;
		return pathImpl.getLeafNode().toString();
	}

}

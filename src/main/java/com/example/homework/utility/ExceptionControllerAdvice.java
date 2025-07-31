package com.example.homework.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.homework.dto.ApiResponse;
import com.example.homework.exception.ValidationException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

	private Logger LOG = LogManager.getLogger(ExceptionControllerAdvice.class);

	@Autowired
	private Environment environment;

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {

		LOG.error("Validation error: ", ex);

		// Format the validation errors into a readable message
		String validationErrors = ex.getConstraintViolations().stream().map(violation -> {

			String errorMessage = environment.getProperty(violation.getMessage());

			errorMessage = errorMessage != null ? errorMessage : violation.getMessage();

			return errorMessage;

		}).reduce((msg1, msg2) -> msg1 + "; " + msg2).orElse("Validation failed");

		ApiResponse<Void> response = ApiResponse.error(validationErrors);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleExceptions(Exception exception) {

		LOG.error("Exception: ", exception);

		String errorMessage = environment.getProperty(exception.getMessage());

		errorMessage = errorMessage != null ? errorMessage : exception.getMessage();

		ApiResponse<Void> response = ApiResponse.error("Something went wrong. " + errorMessage);

		if (exception instanceof ValidationException) {

			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

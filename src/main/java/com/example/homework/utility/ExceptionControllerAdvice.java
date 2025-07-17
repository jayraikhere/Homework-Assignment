package com.example.homework.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

	private Logger LOG = LogManager.getLogger(ExceptionControllerAdvice.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> handleExceptions(Exception exception) {

		LOG.error("Exception: ", exception);

		ErrorMessage error = new ErrorMessage();

		error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

		error.setErrorMessage("Something went wrong. " + exception.getMessage());

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

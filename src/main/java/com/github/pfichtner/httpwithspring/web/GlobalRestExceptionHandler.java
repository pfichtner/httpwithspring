package com.github.pfichtner.httpwithspring.web;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.Map;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
class GlobalRestExceptionHandler {

	@ExceptionHandler({ ConversionFailedException.class, MethodArgumentTypeMismatchException.class })
	@ResponseStatus(BAD_REQUEST)
	public Map<String, String> handleInvalidUuid(Exception ex) {
		return Map.of("error", "Invalid UUID in path parameter");
	}
}
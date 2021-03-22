package com.test.worstfilmgoldenraspberry.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.test.worstfilmgoldenraspberry.exeception.BadRequestExeception;
import com.test.worstfilmgoldenraspberry.exeception.BadRequestExeceptionDetails;

@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(BadRequestExeception.class)
	public ResponseEntity<BadRequestExeceptionDetails> handlerBadRequestException(BadRequestExeception badRequestExeception) {
		return new ResponseEntity<>(
				BadRequestExeceptionDetails.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.title("Bad Request Exception, Check the Documentation")
				.details(badRequestExeception.getMessage())
				.developerMessage(badRequestExeception.getClass().getName())
				.build(), HttpStatus.BAD_REQUEST);
	}
}

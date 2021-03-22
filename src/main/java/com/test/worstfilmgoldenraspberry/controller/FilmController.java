package com.test.worstfilmgoldenraspberry.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.worstfilmgoldenraspberry.dto.ProducerDTO;
import com.test.worstfilmgoldenraspberry.service.FilmServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("worstfilms")
public class FilmController {

	private final FilmServiceImpl filmService;
	
	@GetMapping(path = "/getProducerWhoWonMoreThanOnceWithLongerIntervalAndOneWhoWonFaster")
	public ResponseEntity<ProducerDTO> getProducerWhoWonMoreThanOnceWithLongerIntervalAndOneWhoWonFaster() {
		log.info("Triggered endpoint getProducerWhoWonMoreThanOnceWithLongerIntervalAndOneWhoWonFaster");
		return ResponseEntity.ok(filmService.getProducerWhoWonMoreThanOnceWithLongerIntervalAndOneWhoWonFaster());
	}
	
}

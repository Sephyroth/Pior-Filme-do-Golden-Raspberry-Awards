package com.test.worstfilmgoldenraspberry.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import com.test.worstfilmgoldenraspberry.dto.ProducerDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmControllerTest {

	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	@DisplayName("Look for the movie that has the shortest interval and the one that wins the fastest award")
	void search_ReturnProducerWhoWonMoreThanOnceWithLongerIntervalAndOneWhoWonFaster_whenSuccessful() {
		ProducerDTO result = restTemplate.exchange("/worstfilms/getProducerWhoWonMoreThanOnceWithLongerIntervalAndOneWhoWonFaster", HttpMethod.GET, null,
				new ParameterizedTypeReference<ProducerDTO>() {}).getBody();
		Assertions.assertThat(result.toString()).hasToString(getResult());
	}
	
	private String getResult() {
		return "ProducerDTO(min=[Producer(name=Joel Silver, interval=1, previousWin=1991, followingWin=1990), Producer(name=Leo, interval=1, previousWin=2013, followingWin=2000)], max=[Producer(name=Leo, interval=13, previousWin=2000, followingWin=2013), Producer(name=Matthew Vaughn, interval=13, previousWin=2002, followingWin=2015)])";
	}
	
}

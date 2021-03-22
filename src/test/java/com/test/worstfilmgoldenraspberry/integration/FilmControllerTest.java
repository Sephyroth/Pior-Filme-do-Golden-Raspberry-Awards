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

import com.test.worstfilmgoldenraspberry.domain.Producer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;
	
	@LocalServerPort
	private int port;
	
	@Test
	@DisplayName("Search for the film that has the shortest interval")
	void search_ReturnProducerThatWonMoreThanOnceFaster_whenSuccessful() {
		Producer producer = restTemplate.exchange("/worstfilms/getProducerThatWonMoreThanOnceFaster", HttpMethod.GET, null,
				new ParameterizedTypeReference<Producer>() {}).getBody();
		
		Assertions.assertThat(producer.getName()).isEqualTo("Joel Silver");
	}
	
	@Test
	@DisplayName("Search for the film that has the shortest interval")
	void search_ReturnProducerThatWonMoreThanOnceWithLongerInterval_whenSuccessful() {
		Producer producer = restTemplate.exchange("/worstfilms/getProducerThatWonMoreThanOnceWithLongerInterval", HttpMethod.GET, null,
				new ParameterizedTypeReference<Producer>() {}).getBody();
		
		Assertions.assertThat(producer.getName()).isEqualTo("Matthew Vaughn");
	}
}

package com.test.worstfilmgoldenraspberry.service;

import java.util.List;

import com.test.worstfilmgoldenraspberry.domain.Film;
import com.test.worstfilmgoldenraspberry.domain.Producer;
import com.test.worstfilmgoldenraspberry.domain.Studio;
import com.test.worstfilmgoldenraspberry.dto.ProducerDTO;

public interface FilmService {

	List<Film> getAllWinningFilms();
	
	Film createFilm(String[] metadata);
	
	List<Studio> getStudios(String sutudios);
	
	List<Producer> getProducers(String producers);
	
	List<Producer> getProducerThatWonMoreThanOnceFaster();
	
	List<Film> getFilmsThatProducersHaveWonMoreThanOnce();
	
	List<Producer> getProducerThatWonMoreThanOnceWithLongerInterval();
	
	ProducerDTO getProducerWhoWonMoreThanOnceWithLongerIntervalAndOneWhoWonFaster();
}

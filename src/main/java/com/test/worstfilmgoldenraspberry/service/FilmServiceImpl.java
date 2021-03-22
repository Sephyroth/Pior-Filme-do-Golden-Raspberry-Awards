package com.test.worstfilmgoldenraspberry.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.test.worstfilmgoldenraspberry.domain.Film;
import com.test.worstfilmgoldenraspberry.domain.Producer;
import com.test.worstfilmgoldenraspberry.domain.Studio;
import com.test.worstfilmgoldenraspberry.dto.ProducerDTO;
import com.test.worstfilmgoldenraspberry.exeception.BadRequestExeception;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

	private List<Film> films;
	private static final int ADD_ONE_AWARD = 1;
	
	private void readFilmsFromCSV() {
		films = new ArrayList<>();
		Path path = Paths.get("dbFile/movielist.csv");
		
		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
//			ignores the first item because it is the header
			 String line = br.readLine();
			 line = br.readLine();
			 while (line != null){
				String[] attributes = line.split(";");
				Film film = createFilm(attributes);
				films.add(film);
				line = br.readLine();
			}
			 log.info("All movies loaded into memory : {}", films);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	@Override
	public Film createFilm(String[] metadata) {
		String year = metadata[0];
		String title = metadata[1];
		List<Studio> studios = getStudios(metadata[2]);
		List<Producer> producers = getProducers(metadata[3]);
		boolean winner = metadata.length > 4 && metadata[4] != null && metadata[4].equalsIgnoreCase("yes");
		
		return new Film(title, year, winner, studios, producers);
	}
	
	public List<Studio> getStudios(String sutudios) {
		return Arrays.asList(sutudios.split(",")).stream().map(Studio::new).collect(Collectors.toList());
	}
	
	public List<Producer> getProducers(String producers) {
		return Arrays.asList(producers.split(",|\\band\\b|\\be\\b")).stream().map(Producer::new).collect(Collectors.toList());
	}
	
	public List<Film> getAllWinningFilms() {
		return films.stream().filter(Film::isWinner).collect(Collectors.toList());
	}
	
	public List<Film> getFilmsThatProducersHaveWonMoreThanOnce() { 
	  Set<Film> filmsWonMoreThanOnce = new LinkedHashSet<>();
	  List<Film> winningFilms = getAllWinningFilms();
	  
	  Set<Producer> producers = new HashSet<>();
	  winningFilms.stream().forEach(film -> producers.addAll(film.getProducers()));
	  
	  for (Producer producer : producers) {
		  List<Film> amountOfFilmsByProducer = winningFilms.stream().filter(i -> i.getProducers().contains(producer))
				  .collect(Collectors.toList());
		  
//		  checks if the film has won more than one award
		  if (amountOfFilmsByProducer.size() > 1) {
			  filmsWonMoreThanOnce.addAll(amountOfFilmsByProducer);
		}
	  }
	  
	  return new ArrayList<>(filmsWonMoreThanOnce);
	}
	
	public List<Producer> getProducerThatWonMoreThanOnceWithLongerInterval() {
		readFilmsFromCSV();
		Set<Producer> producers = new HashSet<>();
		List<Film> filmsWonMoreThanOnce = getFilmsThatProducersHaveWonMoreThanOnce();
        filmsWonMoreThanOnce.stream().forEach(n -> producers.addAll(n.getProducers()));

		for (Producer producer : producers) {
			List<Film> amountOfFilmsByProducer = filmsWonMoreThanOnce.stream().filter(i -> i.getProducers().contains(producer))
					.collect(Collectors.toList());
			
			if (amountOfFilmsByProducer.size() > 1) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
				List<LocalDate> yearOfAwards = amountOfFilmsByProducer.stream().map(film -> LocalDate.parse("01.01."+film.getYear(), formatter))
						.sorted()
						.collect(Collectors.toList());
				
				if (yearOfAwards.size() > 1) {
					List<Integer> interval = new ArrayList<>();
					for (int k = 1; k < yearOfAwards.size(); k++) {
						interval.add(Period.between(yearOfAwards.get(k-1),yearOfAwards.get(k)).getYears());
					}
					
					int indexOfInterval = IntStream.range(0, interval.size())
					  .reduce((a,b)-> interval.get(a) < interval.get(b)? b: a).getAsInt();
					
					producer.setPreviousWin(yearOfAwards.get(indexOfInterval).getYear());
					producer.setFollowingWin(yearOfAwards.get(indexOfInterval+1).getYear());					
					producer.setInterval(interval.stream().max(Integer::compare).orElseThrow(NoSuchElementException::new));
				}
			}
		}
		
//		look for the longest interval
		int shortestInterval = producers.stream().max(Comparator.comparing(Producer::getInterval))
				.orElseThrow(() -> new BadRequestExeception("Film Not Found")).getInterval();
		
//		search for all producers that have the longest interval
		return producers.stream().filter(i -> i.getInterval() == shortestInterval)
				.sorted((year1, year2) -> Integer.compare(year1.getPreviousWin(), year2.getPreviousWin())).collect(Collectors.toList());
	}
	
	public List<Producer> getProducerThatWonMoreThanOnceFaster() {
		readFilmsFromCSV();
		Set<Producer> producers = new HashSet<>();
		List<Film> filmsWonMoreThanOnce = getFilmsThatProducersHaveWonMoreThanOnce();
		filmsWonMoreThanOnce.stream().forEach(n -> producers.addAll(n.getProducers()));
		
		for (Producer producer : producers) {
			List<Film> amountOfFilmsByProducer = filmsWonMoreThanOnce.stream().filter(i -> i.getProducers().contains(producer))
					.collect(Collectors.toList());
			
			if (amountOfFilmsByProducer.size() > 1) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
				List<LocalDate> yearOfAwards = amountOfFilmsByProducer.stream().map(film -> LocalDate.parse("01.01."+film.getYear(), formatter))
						.sorted((year1, year2) -> Integer.compare(year2.getYear(), year1.getYear()))
						.collect(Collectors.toList());
				
				if (yearOfAwards.size() > 1) {
					List<Integer> interval = new ArrayList<>();
					
					for (int k = 1; k < yearOfAwards.size(); k++) {
//						add one more to remove films that won only one award
						interval.add(Period.between(yearOfAwards.get(k),yearOfAwards.get(k-1)).getYears() + ADD_ONE_AWARD);
					}
					
					int indexOfInterval = IntStream.range(0, interval.size())
							  .reduce((a,b)-> interval.get(a) < interval.get(b)? b: a).getAsInt();
							
					producer.setPreviousWin(yearOfAwards.get(indexOfInterval).getYear());
					producer.setFollowingWin(yearOfAwards.get(indexOfInterval+1).getYear());					
					producer.setInterval(interval.stream().min(Integer::compare).orElseThrow(NoSuchElementException::new));
				}
			}
		}
		
//		filter by the films that won only one award and look for the shortest interval
		int shortestInterval = producers.stream().filter(p -> p.getInterval() > 0).min(Comparator.comparing(Producer::getInterval))
				.orElseThrow(() -> new BadRequestExeception("Film Not Found")).getInterval();
		
//		search for all producers that have the shortest interval
		List<Producer> min = producers.stream().filter(i -> i.getInterval() == shortestInterval)
				.sorted((year1, year2) -> Integer.compare(year1.getPreviousWin(), year2.getPreviousWin())).collect(Collectors.toList());
		
//		removes a award to fix the calculation
		min.stream().forEach(p -> p.setInterval(p.getInterval() - ADD_ONE_AWARD));
		return min;
	}
	
	@Override
	public ProducerDTO getProducerWhoWonMoreThanOnceWithLongerIntervalAndOneWhoWonFaster() {
		ProducerDTO result = new ProducerDTO();
		result.setMin(getProducerThatWonMoreThanOnceFaster());
		result.setMax(getProducerThatWonMoreThanOnceWithLongerInterval());
		return result;
	}
}

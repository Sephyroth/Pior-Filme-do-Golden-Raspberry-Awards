package com.test.worstfilmgoldenraspberry.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {

	private String title;
	private String year;
	private boolean winner;
	private List<Studio> studios;
	private List<Producer> producers;
	
}

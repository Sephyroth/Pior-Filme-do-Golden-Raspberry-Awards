package com.test.worstfilmgoldenraspberry.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Producer {

	private String name;
	private int interval;
	private int previousWin;
	private int followingWin;
	
	public Producer(String name) {
//		remove white space
		setName(name.trim());
	}
}

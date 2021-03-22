package com.test.worstfilmgoldenraspberry.dto;

import java.util.List;

import com.test.worstfilmgoldenraspberry.domain.Producer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProducerDTO {

	private List<Producer> min;
	private List<Producer> max;
}

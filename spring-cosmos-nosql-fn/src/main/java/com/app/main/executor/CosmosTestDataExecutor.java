package com.app.main.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.app.main.domain.CosmosTestData;
import com.app.main.dto.CosmosTestDataDto;
import com.app.main.repository.CosmosTestDataRepository;

@Component(value = "getCosmosTestData")
public class CosmosTestDataExecutor implements Function<String, List<CosmosTestDataDto>> {

	private final CosmosTestDataRepository cosmosTestDataRepository;

	public CosmosTestDataExecutor(CosmosTestDataRepository cosmosTestDataRepository) {

		this.cosmosTestDataRepository = cosmosTestDataRepository;

	}

	@Override
	public List<CosmosTestDataDto> apply(String category) {
		List<CosmosTestDataDto> cosmosTestDataDtoList = new ArrayList<>();
		final List<CosmosTestData> cosmosTestDataResult = cosmosTestDataRepository.findByCategory(category);
		if (cosmosTestDataResult != null && cosmosTestDataResult.size() > 0) {
			for (CosmosTestData cosmosTestData : cosmosTestDataResult) {
				CosmosTestDataDto cosmosTestDataDto = new CosmosTestDataDto();
				cosmosTestDataDto.setId(cosmosTestData.getId());
				cosmosTestDataDto.setCategory(cosmosTestData.getCategory());
				cosmosTestDataDto.setComplete(cosmosTestData.isComplete());
				cosmosTestDataDto.setDescription(cosmosTestData.getDescription());
				cosmosTestDataDto.setName(cosmosTestData.getName());
				cosmosTestDataDtoList.add(cosmosTestDataDto);
			}

		}

		return cosmosTestDataDtoList;
	}

}

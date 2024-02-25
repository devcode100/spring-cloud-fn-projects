package com.todo.main.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.todo.main.domain.StoreData;
import com.todo.main.dto.StoreDataDto;
import com.todo.main.repository.StoreRepository;

import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.instrumentation.annotations.WithSpan;

@Component
public class FetchStoreData implements Function<String, List<StoreDataDto>> {
	
	Logger logger = LoggerFactory.getLogger(FetchStoreData.class);

	private final StoreRepository storeRepository;

	public FetchStoreData(StoreRepository storeRepository) {

		this.storeRepository = storeRepository;

	}

	@Override
	@WithSpan(value = "fetchStoreDataDBCall", kind = SpanKind.CLIENT)
	public List<StoreDataDto> apply(String category) {
		logger.info("Into fetchStoreDataDBCall {}");
		List<StoreDataDto> storeDataDtos = new ArrayList<>();
		final List<StoreData> storeDatas = storeRepository.findByCategory(category);
		if (storeDatas != null && storeDatas.size() > 0) {
			for (StoreData storeData : storeDatas) {
				StoreDataDto cosmosTestDataDto = new StoreDataDto();
				cosmosTestDataDto.setId(storeData.getId());
				cosmosTestDataDto.setCategory(storeData.getCategory());
				cosmosTestDataDto.setComplete(storeData.isComplete());
				cosmosTestDataDto.setDescription(storeData.getDescription());
				cosmosTestDataDto.setName(storeData.getName());
				storeDataDtos.add(cosmosTestDataDto);
			}

		}

		return storeDataDtos;
	}

}

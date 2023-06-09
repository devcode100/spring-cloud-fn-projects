package com.app.main.repository;

import java.util.List;

import com.app.main.domain.CosmosTestData;
import com.azure.spring.data.cosmos.repository.CosmosRepository;

public interface CosmosTestDataRepository extends CosmosRepository<CosmosTestData, String> {

	List<CosmosTestData> findByCategory(String category);

}

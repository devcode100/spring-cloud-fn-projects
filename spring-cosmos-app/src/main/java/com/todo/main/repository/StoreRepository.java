package com.todo.main.repository;

import java.util.List;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.todo.main.domain.StoreData;

public interface StoreRepository extends CosmosRepository<StoreData, String> {

	List<StoreData> findByCategory(String category);

}

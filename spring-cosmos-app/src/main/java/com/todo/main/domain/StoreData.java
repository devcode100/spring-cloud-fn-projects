package com.todo.main.domain;

import org.springframework.data.annotation.Id;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;

@Container(containerName = "Store")
public class StoreData {

	public StoreData(String id, String category, String name, String description, boolean isComplete) {
		super();
		this.id = id;
		this.category = category;
		this.name = name;
		this.description = description;
		this.isComplete = isComplete;
	}

	@Id
	private String id;
	@PartitionKey
	private String category;
	private String name;
	private String description;
	private boolean isComplete;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	@Override
	public String toString() {
		return "CosmosTestData [id=" + id + ", category=" + category + ", name=" + name + ", description=" + description
				+ ", isComplete=" + isComplete + "]";
	}

}

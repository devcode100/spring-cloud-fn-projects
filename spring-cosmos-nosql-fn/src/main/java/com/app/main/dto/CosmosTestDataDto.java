package com.app.main.dto;

public class CosmosTestDataDto {

	private String id;
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
		return "CosmosTestDataDto [id=" + id + ", category=" + category + ", name=" + name + ", description="
				+ description + ", isComplete=" + isComplete + "]";
	}
	
}

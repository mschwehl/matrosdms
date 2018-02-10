package net.schwehla.matrosdms.domain.search;

public class SearchHistory {


	String kategory;
	
	private String name;
	
	private String description;
	
	private String queryString;
	
	

	public String getKategory() {
		return kategory;
	}

	public void setKategory(String kategory) {
		this.kategory = kategory;
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

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
		
	
	
}

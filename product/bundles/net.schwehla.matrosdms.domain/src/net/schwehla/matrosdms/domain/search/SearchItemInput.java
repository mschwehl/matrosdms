package net.schwehla.matrosdms.domain.search;

import java.util.ArrayList;
import java.util.List;

public class SearchItemInput {

	String querystring;
	List <String> seachAttributes = new ArrayList<>();
	
	public List<String> getSeachAttributes() {
		return seachAttributes;
	}

	public void setSeachAttributes(List<String> seachAttributes) {
		this.seachAttributes = seachAttributes;
	}

	public String getQuerystring() {
		return querystring;
	}

	public void setQuerystring(String querystring) {
		this.querystring = querystring;
	}
	
}

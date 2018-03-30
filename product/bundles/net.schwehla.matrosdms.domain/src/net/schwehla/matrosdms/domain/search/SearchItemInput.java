package net.schwehla.matrosdms.domain.search;

import java.util.ArrayList;
import java.util.List;

import net.schwehla.matrosdms.domain.search.parameter.MatrosQueryParameter;

public class SearchItemInput {

	
	String queryString;
	List<MatrosQueryParameter>   queryParameter = new ArrayList<>();
	
	
	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public List<MatrosQueryParameter> getQueryParameter() {
		return queryParameter;
	}
	

	
	
	// where item.name like xxx 
	
	
	// type
	// and wer=martin
	
	// art=kdw
	
	// attr=kdw[value=2000]
	
//	IExpressionNode node;
//
//	public IExpressionNode getNode() {
//		return node;
//	}
//
//	public void setNode(IExpressionNode node) {
//		this.node = node;
//	}
//	
//    
	
	

	
}

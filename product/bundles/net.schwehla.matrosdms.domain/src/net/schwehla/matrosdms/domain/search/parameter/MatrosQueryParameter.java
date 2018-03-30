package net.schwehla.matrosdms.domain.search.parameter;

import java.util.HashSet;
import java.util.Set;

import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.domain.search.parser.IExpressionNode;

public class MatrosQueryParameter implements IExpressionNode {
	
	
	EMatrosQueryType type;
	
	public EMatrosQueryType getType() {
		return type;
	}

	public void setType(EMatrosQueryType type) {
		this.type = type;
	}

	Identifier identifier;
	Set <EMatrosQueryFeature> feature = new HashSet<>();
	String constraint;
	
	public Identifier getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Identifier identifier) {
		this.identifier = identifier;
	}

	public Set<EMatrosQueryFeature> getFeature() {
		return feature;
	}

	public void setFeature(Set<EMatrosQueryFeature> feature) {
		this.feature = feature;
	}

	public String getConstraint() {
		return constraint;
	}

	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}

	@Override
	public String eval() {
		// TODO Auto-generated method stub
		return null;
	}

}

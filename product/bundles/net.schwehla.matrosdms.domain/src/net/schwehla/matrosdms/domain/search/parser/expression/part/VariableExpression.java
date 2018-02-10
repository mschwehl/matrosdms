package net.schwehla.matrosdms.domain.search.parser.expression.part;

import net.schwehla.matrosdms.domain.search.parser.IExpressionNode;

public class VariableExpression implements IExpressionNode {

	String name;

	public VariableExpression(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String eval() {
		return name;
	}
	

}

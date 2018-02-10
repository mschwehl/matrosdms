package net.schwehla.matrosdms.domain.search.parser.expression.part;

import net.schwehla.matrosdms.domain.api.E_ATTRIBUTETYPE;
import net.schwehla.matrosdms.domain.search.parser.IExpressionNode;

public class TypeExpression implements IExpressionNode {

	E_ATTRIBUTETYPE type;

	public TypeExpression(E_ATTRIBUTETYPE type) {
		this.type = type;
	}

	IExpressionNode expression;


	public IExpressionNode getExpression() {
		return expression;
	}


	public void setExpression(IExpressionNode expression) {
		this.expression = expression;
	}


	@Override
	public String eval() {
		return expression.eval();
	}
	

}

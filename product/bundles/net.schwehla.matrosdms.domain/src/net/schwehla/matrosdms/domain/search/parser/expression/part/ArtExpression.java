package net.schwehla.matrosdms.domain.search.parser.expression.part;

import net.schwehla.matrosdms.domain.search.parser.IExpressionNode;

public class ArtExpression implements IExpressionNode {


	public ArtExpression() {
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

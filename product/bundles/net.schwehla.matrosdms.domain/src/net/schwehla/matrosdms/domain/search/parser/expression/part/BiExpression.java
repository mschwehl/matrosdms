package net.schwehla.matrosdms.domain.search.parser.expression.part;

import net.schwehla.matrosdms.domain.search.parser.IExpressionNode;

public class BiExpression implements IExpressionNode {
	
	String token;
	
	public BiExpression(String token) {
		this.token = token;
	}

	IExpressionNode left;
	IExpressionNode right;
	
	@Override
	public String eval() {
		return left.eval() + " " + token + " " + right.eval();
	}

	public IExpressionNode getLeft() {
		return left;
	}

	public void setLeft(IExpressionNode left) {
		this.left = left;
	}

	public IExpressionNode getRight() {
		return right;
	}

	public void setRight(IExpressionNode right) {
		this.right = right;
	}

	
}

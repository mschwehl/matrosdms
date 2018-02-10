package net.schwehla.matrosdms.domain.search.parser.expression.part;

import net.schwehla.matrosdms.domain.search.parser.IExpressionNode;

public class ConstantExpression implements IExpressionNode {
	
	Object value;

	public ConstantExpression(String value) {
		this.value = value;
	}

	@Override
	public String eval() {
		
		StringBuilder sb = new StringBuilder();
		
		if (value instanceof String) {
			sb.append("'").append(value).append("'"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			sb.append(value.toString());
		}
		
		return sb.toString();
	}


	
}

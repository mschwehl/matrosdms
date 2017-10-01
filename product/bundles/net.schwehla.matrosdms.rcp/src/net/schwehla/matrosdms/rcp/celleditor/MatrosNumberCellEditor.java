package net.schwehla.matrosdms.rcp.celleditor;

import java.text.DecimalFormat;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

import net.schwehla.matrosdms.domain.core.attribute.InfoNumberAttribute;

public class MatrosNumberCellEditor extends TextCellEditor {
			
	

	public MatrosNumberCellEditor(Composite parent, int style){
		super(parent, style);
	}
	
		
	InfoNumberAttribute attribute;

	public InfoNumberAttribute getAttribute() {
		return attribute;
	}


	public void setAttribute(InfoNumberAttribute attribute) {
		this.attribute = attribute;
	}
	
	
	DecimalFormat df = new DecimalFormat(); 

	@Override
	protected void doSetValue(Object value) {
		
		if (value instanceof Number) {
			value = value.toString();
		}

		if (value==null) {
			return;
		}
		super.doSetValue(value);
	}

	@Override
	protected Object doGetValue() {

		String stringValue = (String)super.doGetValue();

		if (stringValue==null || "".equals(stringValue)) {
			return 0d;
		}
		
		stringValue = stringValue.trim();
		try {
		
			return  df.parse(stringValue).doubleValue();
		
		} catch (Exception e) {
			// check that the string is a number;
			return null;
		}

	}
	
	
	
}
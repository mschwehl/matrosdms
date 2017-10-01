package net.schwehla.matrosdms.domain.core.attribute;

import java.text.DateFormat;
import java.util.Date;

import net.schwehla.matrosdms.domain.core.InfoBaseElement;
import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.rcp.FixCalendar;

public abstract class AbstractInfoAttribute extends InfoBaseElement {
	
	private AttributeType type;
	
	private FixCalendar fixcalendar = new FixCalendar() ;
	
	public AttributeType getType() {
		return type;
	}
	public void setType(AttributeType type) {
		this.type = type;
	}
	public AbstractInfoAttribute(Identifier identifier, String Name) {
		super(identifier, Name);
	}
	
	
	public FixCalendar getFixcalendar() {
		return fixcalendar;
	}
	public void setFixcalendar(FixCalendar fixcalendar) {
		this.fixcalendar = fixcalendar;
	}
	public abstract String getTextDescription();

	public abstract void setValue(Object value);
	public abstract Object getValue();
	
	public String getRelevanceDescription() {
	
		StringBuffer sb = new StringBuffer("");  //$NON-NLS-1$
		
		sb.append(fixcalendar.toString());

		return sb.toString();
	}
	public abstract void init();
	
	DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

	
	public final String getTimeInfomation() {
		
	
			  
	    	   if (fixcalendar.isYearOnly()) {
	    		   return "" + fixcalendar.getYearOnlyValue();
	    	   }
	    	   
	    	   
	    	   Date from = fixcalendar.getRelevancefrom();
	    	   Date to = fixcalendar.getRelevanceto();
	    	   
	    	   if (from == null && to == null) {
	    		   return "";
	    	   }
	    	 
	    	   if (from != null && to == null) {
	    		   return "> " + df.format(from);
	    	   }

	    	   if (from == null && to != null) {
	    		   return "< " + df.format(from);       
	    	   }
	    	   
			   return df.format(from) + " " + df.format(to);
	    	   
			   
		}

	
}

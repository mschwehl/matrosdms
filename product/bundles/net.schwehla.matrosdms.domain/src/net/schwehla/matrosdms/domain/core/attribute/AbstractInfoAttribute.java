package net.schwehla.matrosdms.domain.core.attribute;

import java.text.DateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import net.schwehla.matrosdms.domain.core.InfoBaseElement;
import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.rcp.FixCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(value = {InfoTextAttribute.class, InfoNumberAttribute.class, InfoDateAttribute.class, InfoBooleanAttribute.class})
public abstract class AbstractInfoAttribute extends InfoBaseElement {
	
	private static final long serialVersionUID = 1L;

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
	
	@XmlTransient
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

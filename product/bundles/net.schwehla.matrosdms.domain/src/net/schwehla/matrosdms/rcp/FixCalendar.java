package net.schwehla.matrosdms.rcp;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import net.schwehla.matrosdms.adapter.DateAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class FixCalendar {


	@XmlJavaTypeAdapter(value = DateAdapter.class)
    private Date relevancefrom;
	
	@XmlJavaTypeAdapter(value = DateAdapter.class)
    private Date relevanceto;
    
    
    public Date getRelevancefrom() {
		return relevancefrom;
	}

	public void setRelevancefrom(Date relevancefrom) {
		this.relevancefrom = relevancefrom;
	}

	public Date getRelevanceto() {
		return relevanceto;
	}

	public void setRelevanceto(Date relevanceto) {
		this.relevanceto = relevanceto;
	}

    
	public void setRelevancefromCalendar(Calendar to) {
		
		if (to != null) {
			relevancefrom = to.getTime();
		} else {
			relevancefrom = null;
		}
		
	}
    
	public void setRelevancetoCalendar(Calendar to) {
		
		if (to != null) {
			relevanceto = to.getTime();
		} else {
			relevanceto = null;
		}
		
	}


	public boolean isEmpty() {
		return relevancefrom == null && relevanceto == null;
	}

	public boolean isYearOnly() {
		
		if (relevancefrom == null || relevanceto == null) {
			return false;
		}
		
		GregorianCalendar fromCal = new GregorianCalendar();
		fromCal.setTime(relevancefrom);
		
		GregorianCalendar toCal = new GregorianCalendar();
		toCal.setTime(relevanceto);
		
		return fromCal.get(Calendar.YEAR) == toCal.get(Calendar.YEAR)
				&&		fromCal.get(Calendar.MONTH) == Calendar.JANUARY 
				&&		fromCal.get(Calendar.DAY_OF_MONTH) == 1
				&&		toCal.get(Calendar.MONTH) == Calendar.DECEMBER 
			    &&		toCal.get(Calendar.DAY_OF_MONTH) == 31;
	}
				

	public int getYearOnlyValue() {
		
		if (!isYearOnly()) {
			throw new RuntimeException("Invalid :" + toString());
		}
		
		GregorianCalendar fromCal = new GregorianCalendar();
		fromCal.setTime(relevancefrom);
		
		return fromCal.get(Calendar.YEAR);
		
	}

	public void reset() {
		relevancefrom = null;
		relevanceto = null;
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
}

package net.schwehla.matrosdms.adapter;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<Object, Object> {
	
	     
	  
    @Override
    public Object marshal(Object dt) throws Exception {
    	
    	if (dt == null) {
    		return null;
    	}
    	
        return ((Date) dt).getTime();
    }

    @Override
        public Object unmarshal(Object s) throws Exception {
   
    	if (s == null || s.toString().trim().length() == 0) {
    		return null;
    	}
    	
    	return new Date((long)s);
    }
    
    
}
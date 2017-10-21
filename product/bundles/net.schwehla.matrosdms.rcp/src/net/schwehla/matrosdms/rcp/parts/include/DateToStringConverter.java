package net.schwehla.matrosdms.rcp.parts.include;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.databinding.conversion.Converter;

public class DateToStringConverter extends Converter {
	
	final     SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
	
	 public DateToStringConverter() {
         super(Date.class, String.class);
     }

     @Override
     public Object convert(Object from) {
         if (from instanceof Date) {
             String value = format.format(from);
             return value;
         }
         return null;
     }

}

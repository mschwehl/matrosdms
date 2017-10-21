package net.schwehla.matrosdms.rcp.parts.include;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.databinding.conversion.Converter;

public class StringToDateConverter extends Converter {

	final     SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

	  public StringToDateConverter() {
          super(String.class, Date.class);
      }

    /**
     * This implementation use the date format to parse the string and
     * return a date. Parsing exception are not throws.
     */
    @Override
    public Object convert(Object from) {
        if (from instanceof String) {
            try {
                return format.parse((String) from);
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }
        
}

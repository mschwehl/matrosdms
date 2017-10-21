package net.schwehla.matrosdms.rcp.parts.include;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class DateValidator implements IValidator {

	
	final     SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public IStatus validate(Object value) {
    	
       IStatus status = Status.OK_STATUS;
       
       String str = (String) value;
       
       if (str == null || str.trim().length() == 0) {
    	   return status;
       }
       
       if (str.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
          try {
        	 sdf.parse(str);
          }catch(ParseException | IllegalArgumentException e) {
             status = ValidationStatus.error("Wrong Date", e);
          }
          
          return status;
          
       }else
          status = ValidationStatus.error("Wrong Format");
       return status;
    }       
 
}

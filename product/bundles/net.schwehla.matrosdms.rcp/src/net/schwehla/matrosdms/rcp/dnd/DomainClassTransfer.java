package net.schwehla.matrosdms.rcp.dnd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.dnd.Transfer;

import net.schwehla.matrosdms.domain.core.InfoBaseElement;

public class DomainClassTransfer <T extends Class> extends GenericTransfer {

	/**
	 * Due to autoboxing must use wrapper class.
	 * @param clazz
	 */
	public DomainClassTransfer( T clazz ) {
		super( (Class) clazz );
	}
	
	
	@Override
	protected boolean isObjectValid(Object object, Class<?> transferClass) {
		
		if (object instanceof List) {
			
			List list = (List) object;
			
			for (int i=0 ; i < list.size() ; i++) {
				
				 if (!transferClass.isAssignableFrom(list.get(i).getClass()) ) {
					 return false;
				 }
				
			}
			
			return true;
		}
		
		return false;
		

	}
	
	private static Map  INSTANCE = new HashMap();

	// MAKE 
	public static Transfer getTransfer(Class<? extends InfoBaseElement> class1) {
		if (INSTANCE.get(class1.getCanonicalName()) == null) {
			INSTANCE.put(class1.getCanonicalName(), new DomainClassTransfer(class1) );
		}
		
		return (DomainClassTransfer) INSTANCE.get(class1.getCanonicalName());
	}
}
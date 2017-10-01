 
package net.schwehla.matrosdms.rcp.handlers;

import java.util.List;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.Selector;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import net.schwehla.matrosdms.domain.core.InfoContext;

public class EditContextHandler {
	
	

	
	@Execute
	public void execute(EModelService modelService , MApplication application	,
			EPartService partService
			, @Optional @Named(IServiceConstants.ACTIVE_SELECTION) InfoContext infoContext) {
		

		if (infoContext == null) {
			return;
		}



		 java.util.Optional <MPart> existing = isExisting(application,modelService,infoContext);
			
		  if (existing.isPresent()) {
				
				MPart part = existing.get();
				partService.showPart(part, PartState.ACTIVATE);
				
   		}  else {
   			
   			MPart itemPart = partService.createPart("net.schwehla.matrosdms.rcp.partdescriptor.contextpart");
   			
   			itemPart.setIconURI("pdf");
   			
   			itemPart.setObject(infoContext);
   			itemPart.setLabel(infoContext.getName());
   			
   			partService.showPart(itemPart, PartState.ACTIVATE);
   		}		
		

		
	}
	
	

	private java.util.Optional <MPart>  isExisting( MApplication application, EModelService modelService, InfoContext infoItem ) {
		

        List<MPart> parts = modelService.findElements(application, MPart.class, EModelService.IN_ANY_PERSPECTIVE,
                        new Selector()
                        {
                            @Override
                            public boolean select(MApplicationElement element)
                            {
                            	
                            	if (element instanceof MPart) {
                            		
                            		if (InfoContext.class.isInstance(  ((MPart) element).getObject() )) {
                            			
                            			InfoContext p = (InfoContext)   ((MPart) element).getObject() ;
        	
                            			return infoItem.getIdentifier().equals(p.getIdentifier());
                            		}
                            		
                            		
                            	}
                 
                            	return  false;
                            
                            }
                        });
        
        if (parts != null && parts.size() > 0 ) {
        	return  java.util.Optional.ofNullable(parts.get(0))  ;
        }
        
        return java.util.Optional .empty();
		
	}
		
}
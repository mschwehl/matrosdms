package net.schwehla.matrosdms.rcp.parts;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.Selector;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.rcp.parts.helper.ItemPartElementWrapper.Type;

@Creatable
public class ItemDropper {

	
	@Inject
	Logger logger;

	@Inject 
	EPartService partService;

	@Inject 
	EModelService modelService;
	
	@Inject @Active Shell shell;
	
	@Inject MApplication application;

	private InfoContext _infoContext; 

	@Inject
	@Translation
	MatrosMessage messages;
	

	
	private java.util.Optional <MPart>  isExisting(InfoItem infoItem ) {
		

        List<MPart> parts = modelService.findElements(application, MPart.class, EModelService.IN_ANY_PERSPECTIVE,
                        new Selector()
                        {
                            @Override
                            public boolean select(MApplicationElement element)
                            {
                            	
                            	if (element instanceof MPart) {
                            		
                            		if (ItemPart.class.isInstance(  ((MPart) element).getObject() )) {
                            			
                            			ItemPart p = (ItemPart)   ((MPart) element).getObject() ;
        	
                            			try {
                            				
                            				// Already open ? Than the Filenames and the context must be same
                            				
                            				if (Type.NEW.equals( p._wrapper.getType() )) {
                            					
                            					return p._wrapper.getInfoItem().getName().equals( infoItem.getName()) && 
                            							
                            							 p._wrapper.getInfoItem().getContext().equals(infoItem.getContext())
                            							
                            							;
                            							
                            					
                            				}
                            				
											return infoItem.getIdentifier().equals(p._wrapper.getInfoItem().getIdentifier());
										} catch (Exception e) {
											// Should not happen
										}
                            		
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

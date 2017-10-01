package net.schwehla.matrosdms.rcp.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;
import net.schwehla.matrosdms.rcp.swt.composite.MatrosTagGroup;

@Singleton
public class DropfieldPart {

	
	@Inject
	MApplication application;

	@Inject
	IEclipseContext context;
	
	@Inject
	@Optional
	/**
	 * OK, double-clicked on one Element, eg. Car
	 * @param modelType
	 */
	private void subscribeTopicTOPIC_TAGGRAPH_DOUBLEKLICK_ADD_ELEMENT(@UIEventTopic(MyEventConstants.TOPIC_TAGGRAPH_DOUBLEKLICK_ADD_ELEMENT) InfoKategory type) {

		if (type != null 
				&& _localDropfieldContext != null) {
			
			
			if (! type.getRoot().getIdentifier().equals( MyGlobalConstants.ROOT_ART )) {
			
			 if (! _localDropfieldContext.getDictionary().get(type.getRoot().getIdentifier()).contains(type ) ) {
					_localDropfieldContext.getDictionary().get(type.getRoot().getIdentifier()).add(type);
					
					 tagGroupWer.refresh();
					 tagGroupWas.refresh();
					 tagGroupWo.refresh();
					
					 // Notify the List to be refreshed
					 eventBroker.post(MyEventConstants.TOPIC_DROPFIELD_KATEGORY_CHANGED_SEND_TO_MAPLIST, _localDropfieldContext);

			 }
			
			}
		}
	    
	}
	
	
/*

		// Interface-Implementierungen wenn sich eine Dropfield-Liste geändert hat

		@Override
		public void listChanged(TableViewerTypeAware element) {

			// Asynchron damit die GUI nicht blokiert
			eventBroker.post(MyEventConstants.TOPIC_DROPFIELD_KATEGORY_CHANGED_SEND_TO_MAPLIST, _localDropfieldContext);
			
		}



		@Override
		public void activated(TableViewerTypeAware element) {		
			
			TableViewerTypeAware selectedElement = (TableViewerTypeAware) application.getContext().get(MyEventConstants.ACTIVE_DROPFIELD);
			if (selectedElement  != null) {
				selectedElement.clearDropBackground();
			}
			
			
			IEclipseContext appContext = application.getContext();
		
			// Merken wir uns für Drag and Drop - Coloring
			appContext.getParent().set(MyEventConstants.ACTIVE_DROPFIELD, element);
			element.setFocus();
			
			eventBroker.send(MyEventConstants.TOPIC_DROPFIELD_KATEGORY_ACTIVATED, element.getRootIdentifier());
			

			
		}
	
*/
		
		

	@Inject
	private MDirtyable _dirty;

	@Inject
	private IEventBroker eventBroker;


	@Inject
	IMatrosServiceService service;

	// Wird beim Startup angelegt
	@Inject
	@Named(MyEventConstants.ACTIVE_DROPFIELD_CONSTELLATION_CONTEXT) 
	InfoContext _localDropfieldContext;


	@PostConstruct
	public void createComposite(Composite parent) {

		// TODO: Find sash via Model an adjust heihth
    
		@SuppressWarnings("unused")
		Composite group = buildDynamicTagDropAreas(parent);

	//     SWTHelper.setSashWeights(sash, parent.getParent().getParent(), group);

	    
	    
	}

	MatrosTagGroup tagGroupWer;
	MatrosTagGroup tagGroupWas ;
	MatrosTagGroup tagGroupWo;
	/**
	 * Fügt drei Kategorien hinzu, welche
	 * 
	 * @publish TOPIC_DROPFIELD_KATEGORY_ACTIVATED
	 * 
	 * @param groupTagCategory
	 */
	private Composite buildDynamicTagDropAreas(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
	
		Composite grpTags = new Composite(parent, SWT.NONE);
//		grpTags.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		GridLayout gl_grpTags = new GridLayout(3, false);
		gl_grpTags.marginWidth = 0;
		gl_grpTags.marginHeight = 0;
		gl_grpTags.verticalSpacing = 0;
		grpTags.setLayout(gl_grpTags);

		
		tagGroupWer	= MatrosTagGroup.buildTagGroupViaEclipseContext(context, grpTags, _localDropfieldContext.getDictionary().get(MyGlobalConstants.ROOT_WER));
		tagGroupWas	= MatrosTagGroup.buildTagGroupViaEclipseContext(context, grpTags, _localDropfieldContext.getDictionary().get(MyGlobalConstants.ROOT_WAS));
		tagGroupWo 	= MatrosTagGroup.buildTagGroupViaEclipseContext(context, grpTags, _localDropfieldContext.getDictionary().get(MyGlobalConstants.ROOT_WO));
		 
		
		// XXX WARUM NUR FÜR WER ?
		tagGroupWer.addChangelistener((x) -> {
			eventBroker.post(MyEventConstants.TOPIC_DROPFIELD_KATEGORY_CHANGED_SEND_TO_MAPLIST, _localDropfieldContext);
		});
		
		tagGroupWas.addChangelistener((x) -> {
			eventBroker.post(MyEventConstants.TOPIC_DROPFIELD_KATEGORY_CHANGED_SEND_TO_MAPLIST, _localDropfieldContext);
		});
		
		tagGroupWo.addChangelistener((x) -> {
			eventBroker.post(MyEventConstants.TOPIC_DROPFIELD_KATEGORY_CHANGED_SEND_TO_MAPLIST, _localDropfieldContext);
		});
		
		return grpTags;
	
	}




	@Focus
	public void setFocus() {
		// tableViewer.getTable().setFocus();
	}

	@Persist
	public void save() {
		_dirty.setDirty(false);
	}
	


	
}

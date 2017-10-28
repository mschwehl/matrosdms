package net.schwehla.matrosdms.rcp.parts;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;
import net.schwehla.matrosdms.rcp.parts.helper.InfoKategoryListWrapper;

/**
 * Tree-View for hierarchical tags 
 * @author Martin
 *
 */

@Singleton
public class TagGraphPart {

	@Inject Logger logger;
	
	@Inject
	private IMatrosServiceService service;

	@Inject
	private IEventBroker eventBroker;

	// use field injection for the service
	@Inject
	ESelectionService selectionService;

	@Inject
	MApplication application;


	@Inject
	IEclipseContext context;

	
	StackLayout sl_compositeContentArea = new StackLayout();
	
	private Composite compositeContentArea;
	
	Map <Identifier,InfoKategoryListWrapper> wrapperMap;
	
	@Inject
	InfoKategoryListWrapper wer;
	
	@Inject
	InfoKategoryListWrapper was;

	@Inject
	InfoKategoryListWrapper wo;

	@Inject
	InfoKategoryListWrapper art;

	
//    
//
//	
//	@Inject
//	@Optional
//	private void subscribeTOPIC_REFRESH_CONTEXTLIST(@UIEventTopic(MyEventConstants.TOPIC__MODEL_FILTERED_COMPLETE_REFRESH_CONTEXTLIST) InfoContext type) {
//
//		
//		
//	}
//	
	

	
	@Inject
	@Optional
	private void subscribeTopicInboxDragStart(
			@UIEventTopic(MyEventConstants.TOPIC_DROPFIELD_KATEGORY_ACTIVATED) Identifier type) {

		if (wrapperMap != null) {
			InfoKategoryListWrapper wrapper = wrapperMap.get(type);

				sl_compositeContentArea.topControl = wrapper.getComponent();
				
				wrapper.refresh();
				
				compositeContentArea.getParent().layout(true,true);
				
				compositeContentArea.layout();
				compositeContentArea.redraw();
	
		}

	}



	@Focus
	public void setFocus() {
		// _treeviewer.getControl().setFocus();
	}


	@PostConstruct
	public void createControls(Composite parent) {
		


		parent.setLayout(new GridLayout(1, false));
		
		// http://stackoverflow.com/questions/15132257/swt-how-to-right-align-items-in-a-toolbar
		 
		 ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
		 GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).grab(true, false).applyTo(toolBar);
		 
		 ToolItem tltmNewItem = new ToolItem(toolBar, SWT.NONE);
		 tltmNewItem.setText("New Item");
		 
		 tltmNewItem.addSelectionListener( new SelectionAdapter() {
			 
			 @Override
			public void widgetDefaultSelected(SelectionEvent e) {
				 widgetSelected(e);
			}
			 
			 @Override
			public void widgetSelected(SelectionEvent e) {
					 Composite c = (Composite) sl_compositeContentArea.topControl;
					 
					 if (c != null && InfoKategoryListWrapper.class.isInstance( c.getData())) {
						 
						 InfoKategoryListWrapper casted = (InfoKategoryListWrapper) c.getData();
						 casted.createNewKategoryInTree();
						 casted.refresh();
						 
					 }
				
 
			}
		});
		
		
		compositeContentArea = new Composite(parent, SWT.NONE);
		compositeContentArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeContentArea.setLayout(sl_compositeContentArea);
		
		wrapperMap = new HashMap<>();
		
		try {
			wrapperMap.put(MyGlobalConstants.ROOT_WER , wer.init(compositeContentArea,MyGlobalConstants.ROOT_WER,false) );
			wrapperMap.put(MyGlobalConstants.ROOT_WAS , was.init(compositeContentArea,MyGlobalConstants.ROOT_WAS,false) );
			wrapperMap.put(MyGlobalConstants.ROOT_WO , wo.init(compositeContentArea,MyGlobalConstants.ROOT_WO,false) );
			wrapperMap.put(MyGlobalConstants.ROOT_ART , art.init(compositeContentArea,MyGlobalConstants.ROOT_ART,false) );
			


		} catch (Exception e) {
			logger.error(e);
		}
		compositeContentArea.layout();
		parent.redraw();
		

	}


		
}

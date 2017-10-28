 
package net.schwehla.matrosdms.rcp.parts.masterdata;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.observable.sideeffect.ISideEffect;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.databinding.viewers.IViewerObservableValue;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;

import net.schwehla.matrosdms.domain.core.InfoBaseElement;
import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;
import net.schwehla.matrosdms.rcp.dnd.DomainClassTransfer;
import net.schwehla.matrosdms.rcp.dnd.MyDropListener;
import net.schwehla.matrosdms.rcp.parts.helper.InfoKategoryListWrapper;
import net.schwehla.matrosdms.rcp.swt.squarebutton.SquareButton;
import net.schwehla.matrosdms.rcp.swt.squarebutton.SquareButtonGroup;

public class ElementPart {

	
	
	public static final int COMPONENT_WER = 1;
	public static final int COMPONENT_WAS = 2;
	public static final int COMPONENT_WO = 3;
	public static final int COMPONENT_ART = 4;
	public static final int COMPONENT_ATTRIBUTE = 5;
	public static final int COMPONENT_ORIGINALSTORE = 6;
	public static final int COMPONENT_USER = 7;
	public static final int COMPONENT_ROLLE = 8;
	public static final int COMPONENT_DBCONFIG = 9;
	public static final int COMPONENT_CLIENTCONFIG = 10;

	
	@Inject
	public ElementPart() {

	}

	
	
	Transfer[] transferTypes = { DomainClassTransfer.getTransfer(InfoKategory.class)};
	
	
	@Inject IMatrosServiceService matrosService;
	
	@Inject
	ESelectionService selectionService;
	
	
	Wrapper wer_wrapper;
	Wrapper was_wrapper;
	Wrapper wo_wrapper;
	Wrapper art_wrapper;
	
	@Inject
	private IMatrosServiceService service;

	@Inject
	private IEventBroker eventBroker;
	
	
	@Inject
	IEclipseContext context;
	
	@Inject Logger logger;
	
	SquareButtonGroup sg = new SquareButtonGroup();


	ToolItem btnCommons;
	Composite compositeContentArea ;
	
	StackLayout sl_compositeContentArea = new StackLayout();

	List<ToolItem> toolItemList = new ArrayList<>();


	Composite compositeRoot;
	
	InfoContext _localDropfieldContext;

	@PostConstruct
	public void postConstruct(Composite parent) {
		
		
		_localDropfieldContext = new InfoContext(Identifier.createNEW(), "dummy");

		

		compositeRoot = new Composite(parent, SWT.NONE);
		
		
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.horizontalSpacing = 0;
		compositeRoot.setLayout(gl_composite);

		Composite compositeButtons = new Composite(compositeRoot, SWT.NONE);
		compositeButtons.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
	
		
		buildSquareButton("wer", compositeButtons,COMPONENT_WER);
		
		buildSquareButton("was", compositeButtons,COMPONENT_WAS);

		buildSquareButton("wo", compositeButtons , COMPONENT_WO);

		buildSquareButton("art", compositeButtons , COMPONENT_ART);

		
		buildSquareButton("Attribute", compositeButtons,-1);
		
		buildSquareButton("Originalstore", compositeButtons,-1);

		buildSquareButton("User", compositeButtons,-1);

		buildSquareButton("Roles", compositeButtons,-1);

		buildSquareButton("DBConfig", compositeButtons,-1);

		buildSquareButton("Clientconfig", compositeButtons,-1);
		compositeButtons.setLayout(new GridLayout(1, false));
		

		
		compositeContentArea = new Composite(compositeRoot, SWT.NONE);
		compositeContentArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeContentArea.setLayout(sl_compositeContentArea);

		
		{
			wer_wrapper = new Wrapper();
			wer_wrapper.build(compositeContentArea, context , MyGlobalConstants.ROOT_WER);
	
			
			sl_compositeContentArea.topControl = wer_wrapper.compositeRootComposite;

		}
		

		{
			was_wrapper = new Wrapper();
			was_wrapper.build(compositeContentArea, context , MyGlobalConstants.ROOT_WAS);
		}
		
		{
			wo_wrapper = new Wrapper();
			wo_wrapper.build(compositeContentArea, context , MyGlobalConstants.ROOT_WO);
			
		}
		
		{
			art_wrapper = new Wrapper();
			art_wrapper.build(compositeContentArea, context , MyGlobalConstants.ROOT_ART);
		}
		
		
		
	//	sg.setCurrentlyToggledButton(button);
	

	}
	


	private SquareButton buildSquareButton(String name, Composite parent, Integer data) {
		
		SquareButton.SquareButtonBuilder builder = new SquareButton.SquareButtonBuilder();
	    builder .setParent(parent)
	    	.setText(name)
	    	.setCornerRadius(3).setToggleable(true)
	        .setDefaultMouseClickAndReturnKeyHandler(new SquareButton.ButtonClickHandler() {
	          @Override
	          public void clicked() {
	        	  
	     
	        	  activate( data );
	           // openTestDialog(shell);
	          }
	        });
	    
	    
	    SquareButton b = builder.build();
	    b.setData(data);
	  
		b.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
	    
	    sg.addButton(b);
	    
	    return b;
	    
	}


	
	public void activate(int component) {

		switch (component) {

		case COMPONENT_WER:

			sl_compositeContentArea.topControl = wer_wrapper.compositeRootComposite;
			compositeContentArea.layout();

			break;
			
		case COMPONENT_WAS:

			sl_compositeContentArea.topControl = was_wrapper.compositeRootComposite;
			compositeContentArea.layout();
			
			break;


		case COMPONENT_WO:

			sl_compositeContentArea.topControl = wo_wrapper.compositeRootComposite;
			compositeContentArea.layout();
			
			break;

			
		case COMPONENT_ART:

			sl_compositeContentArea.topControl = art_wrapper.compositeRootComposite;
			compositeContentArea.layout();
			
			break;

			
			

		default:
			break;
		}


		compositeContentArea.getParent().redraw();
		compositeContentArea.getParent().getParent().redraw();


	}
	
	

	class Wrapper {
		
		Composite compositeRootComposite ;
		Composite compositeAction ;
	
		
		Table table;
		Text txtOk;
		Composite compositeTree;
		Text text;
		Text txtParent;
		
		InfoKategoryListWrapper listInfoWrapper;
		

		public void build(Composite compositeContentArea, IEclipseContext context, Identifier rootIdentifier) {
			

			listInfoWrapper = ContextInjectionFactory.make(InfoKategoryListWrapper.class, context);
			
			compositeRootComposite = new Composite(compositeContentArea, SWT.NONE);
			compositeAction = new Composite(compositeRootComposite, SWT.NONE);	
			compositeAction.setLayout(new GridLayout(2, false));
			compositeAction.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));

		
			compositeRootComposite.setLayout(new GridLayout(1, false));
			{
				compositeTree = new Composite(compositeRootComposite, SWT.NONE);
				compositeTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				compositeTree.setLayout(new GridLayout(1, false));
			}
			
			Composite compositeAction = new Composite(compositeRootComposite, SWT.NONE);
			compositeAction.setLayout(new GridLayout(2, false));
			compositeAction.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
			
			txtParent = new Text(compositeAction, SWT.BORDER);
			txtParent.setEditable(false);
			txtParent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			new Label(compositeAction, SWT.NONE);
			
			ListViewer listViewer = new ListViewer(compositeAction, SWT.BORDER | SWT.V_SCROLL);
			
			
			listViewer.setContentProvider(new ArrayContentProvider());
			listViewer.setInput(new ArrayList());
			listViewer.addDropSupport (  DND.DROP_MOVE | DND.DROP_LINK | DND.DROP_COPY, transferTypes, 
					new MyDropListener(listViewer, transferTypes) {
 				
				@Override
				public void drop(DropTargetEvent event) {
					
					if (event.data instanceof List) {
						
			    		InfoKategory p = (InfoKategory) ((List)event.data).get(0) ;
			    		
			    		List l = (List) listViewer.getInput();
			    		l.clear();
			    		l.add(p);
			    		listViewer.refresh(true);
			    		
			    	}
					
					
				}
				
				
			});
	
			listViewer.setLabelProvider(
					new ColumnLabelProvider() {
					    @Override
					    public String getText(Object element) {
					    	
					    	if (element instanceof InfoKategory) {
					    		InfoKategory p = (InfoKategory) element;
						        return p.getName();
					    	}
					    	
					    	return element.toString();
					        
					    }
					}
					);
			
			Button btnUpdate = new Button(compositeAction, SWT.NONE);
			btnUpdate.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					
					InfoKategory elemenent = (InfoKategory) ((List)listViewer.getInput()).get(0);
					InfoKategory parent = (InfoKategory) ((IStructuredSelection)
							listInfoWrapper.getTreeViewer().getSelection()).getFirstElement();
					
					try {
						service.updateInfoKategory(elemenent,parent.getIdentifier());
						
						InfoKategory toSend = service.getInfoKategoryByIdentifier(listInfoWrapper.getRootIdentifier());
						eventBroker.send(MyEventConstants.TOPIC_REFRESH_INFOKATEGORY_MODIFIED, toSend);
						
					} catch (MatrosServiceException e1) {
						logger.error(e1);
					}
					
					
				}
			});
			btnUpdate.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
			btnUpdate.setText("Update Parent");

			
			try {
				listInfoWrapper.init(compositeTree,rootIdentifier,true);
				listInfoWrapper.setEditMode(true);
			} catch (Exception e) {
				logger.error(e);
			}
			
	
			// binding
			
			IViewerObservableValue  viewerSelectionObservable = ViewerProperties.singleSelection()
					.observe(listInfoWrapper.getTreeViewer());
			

		

			ISideEffect enableOkButtonSideEffect = ISideEffect.create(viewerSelectionObservable::getValue, 
					e -> { 
						
						if (e != null) {
							
							if (e instanceof InfoKategory) {
					    		InfoKategory p = (InfoKategory) e;
					    		txtParent.setText(p.getName());
					    	}
							
							
						} else {
							
							txtParent.setText("");
						}
						
						txtParent.redraw();
						
						});
			
			compositeTree.addDisposeListener( e->viewerSelectionObservable.dispose() );
			compositeTree.addDisposeListener( e->enableOkButtonSideEffect.dispose() );		

			listInfoWrapper.setEditMode(true);
			
		}

		
	}
	
	
}
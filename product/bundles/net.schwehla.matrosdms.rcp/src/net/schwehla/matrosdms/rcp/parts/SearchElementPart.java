 
package net.schwehla.matrosdms.rcp.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.domain.search.SearchItemInput;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;
import net.schwehla.matrosdms.rcp.controller.SerachResultListController;
import net.schwehla.matrosdms.rcp.dialog.MatrosDisplayTextDialog;
import net.schwehla.matrosdms.rcp.dialog.SearchAttributeDialog;
import net.schwehla.matrosdms.rcp.dialog.SearchInfoTypeTreeDialog;
import net.schwehla.matrosdms.rcp.parts.helper.InfoKategoryListWrapper;
import net.schwehla.matrosdms.rcp.swt.search.SearchItemEditor;




public class SearchElementPart {
	
	@Inject
	IEclipseContext context;
	
	@Inject @Active Shell shell;
	
	@Inject
	private IMatrosServiceService service;
	
	@Inject
	SerachResultListController searchController;
	
	@Inject
	InfoKategoryListWrapper art;
	
	@Inject Logger logger;
	
	private SearchItemEditor swtQueryWidget;
	
	@Inject
	public SearchElementPart() {
		
	}
	
	
	
	SearchItemInput input;
	private Button btnSearch;
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginWidth = 0;
		composite.setLayout(gl_composite);
		
		Composite compositeTopMenue = new Composite(composite, SWT.NONE);
		compositeTopMenue.setLayout(new FillLayout(SWT.HORIZONTAL));
		compositeTopMenue.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));

		
		Composite compositeSearcharea = new Composite(composite, SWT.NONE);
		compositeSearcharea.setLayout(new GridLayout(2, false));
		compositeSearcharea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		swtQueryWidget = new SearchItemEditor(compositeSearcharea);
		swtQueryWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		
		btnSearch = new Button(compositeSearcharea, SWT.NONE);
		btnSearch.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		
		
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (input == null) {
					input = new SearchItemInput();
				}
				
				
				input = swtQueryWidget.getSearchItemInput();
				
				
				try {
					searchController.reload(input);
				} catch(MatrosServiceException mse) {
					
					MatrosDisplayTextDialog dialog = new MatrosDisplayTextDialog(shell, mse.getMessage() );
				    dialog.open();

					// logger.info("Suche nicht erfolgreich " + mse.getMessage() );
					
				}
			}
		});

		btnSearch.setText("Search");
			
		
		Composite compositeAttributes = new Composite(composite, SWT.NONE);
		compositeAttributes.setLayout(new GridLayout(5, false));
		compositeAttributes.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
		
		Button btnWer = new Button(compositeAttributes, SWT.NONE);
		btnWer.setText("Wer");
		addPopupToControl(btnWer, MyGlobalConstants.ROOT_WER);
		
		Button btnWas = new Button(compositeAttributes, SWT.NONE);
		btnWas.setText("Was");
		addPopupToControl(btnWas, MyGlobalConstants.ROOT_WAS);

		
		Button btnWo = new Button(compositeAttributes, SWT.NONE);
		btnWo.setText("Wo");
		addPopupToControl(btnWo, MyGlobalConstants.ROOT_WO);
		
		Button btnArt = new Button(compositeAttributes, SWT.NONE);
		btnArt.setText("Art");
		addPopupToControl(btnArt, MyGlobalConstants.ROOT_ART);
		btnArt.toDisplay(btnArt.getLocation());
		
		
		Button btnAttribute = new Button(compositeAttributes, SWT.NONE);
		btnAttribute.setText("Attribute");
		
		btnAttribute.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				

				if (btnAttribute.getData() == null) {
					
					
					IEclipseContext tempContext = EclipseContextFactory.create();
			        
			        // Composite parent, String text,  List<InfoKategory> supplier 
					
					SearchAttributeDialog  dialog = ContextInjectionFactory.make(SearchAttributeDialog.class, context, tempContext);
					btnAttribute.setData(dialog);
					
					dialog.open();
					
					
				} else {
					SearchAttributeDialog dialog = (SearchAttributeDialog) btnAttribute.getData(); 
					dialog.getShell().setVisible(!dialog.getShell().getVisible());
				}
				
				
				
			}
			
		} );

		btnAttribute.toDisplay(btnAttribute.getLocation());
		
		
		
		
		input = new SearchItemInput();
		
		createBinding();
		
	}
	
	private void addPopupToControl(Button btnType, Identifier identifier) {

		
		
		
		btnType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (btnType.getData() == null) {
					
					
					IEclipseContext tempContext = EclipseContextFactory.create();
			        
			        // Composite parent, String text,  List<InfoKategory> supplier 
			        
			        tempContext.set(Identifier.class, identifier);
			        
					
					SearchInfoTypeTreeDialog dialog = ContextInjectionFactory.make(SearchInfoTypeTreeDialog.class, context, tempContext);
					btnType.setData(dialog);
					
					dialog.open();
					
					
				} else {
					SearchInfoTypeTreeDialog dialog = (SearchInfoTypeTreeDialog) btnType.getData(); 
					dialog.getShell().setVisible(!dialog.getShell().getVisible());
				}

			}
		});
		
		
		// Control

		/*
	    Composite compositeTopShell = new Composite(popOverComposite.getPopOverCompositeShell(), SWT.NONE);
	    
	    compositeTopShell.setLayout(new GridLayout(1, false));


		 try {
	
			 art.init(compositeTopShell, identifier,false );
			 art.setExpanded(true);
		} catch (Exception e1) {
			logger.error(e1);
		}
		
	//    new InfoKategoryListWrapper(compositeContentArea,MyGlobalConstants.ROOT_WER)
	    
	    
	    Button s = new Button(compositeTopShell, SWT.PUSH);
	    s.setText("close");
	    
	    s.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
	            popOverComposite.fadeOut();
	          }
	        });
	    
	    
	    popOverComposite.setComposite(compositeTopShell);
	    */
	    
		
	}

	private void createBinding() {
	
	

		
//		bindingNewStype.setButtonVisible(btnSearch,firstName);
		
	}
}
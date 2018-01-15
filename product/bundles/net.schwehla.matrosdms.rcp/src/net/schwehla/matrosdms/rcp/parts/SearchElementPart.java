 
package net.schwehla.matrosdms.rcp.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.search.SearchItemInput;
import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;
import net.schwehla.matrosdms.rcp.binding.MatrosBinder;
import net.schwehla.matrosdms.rcp.controller.SerachResultListController;
import net.schwehla.matrosdms.rcp.parts.helper.InfoKategoryListWrapper;
import net.schwehla.matrosdms.rcp.swt.popupshell.widgets.notifications.PopOverComposite;
import org.eclipse.swt.widgets.Label;




public class SearchElementPart {
	
	@Inject @Active Shell shell;
	
	@Inject
	private IMatrosServiceService service;
	
	@Inject
	SerachResultListController searchController;
	
	@Inject
	InfoKategoryListWrapper art;
	
	@Inject Logger logger;
	
	private Text text;
	
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
		
		text = new Text(compositeSearcharea, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		
		btnSearch = new Button(compositeSearcharea, SWT.NONE);
		btnSearch.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		
		
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				searchController.reload(input);
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
		new Label(compositeAttributes, SWT.NONE);
		
  	   btnArt.toDisplay(btnArt.getLocation());
		
		
		input = new SearchItemInput();
		
		createBinding();
		
	}
	
	private void addPopupToControl(Button btnType, Identifier identifier) {
		
	    PopOverComposite popOverComposite = PopOverComposite.createPopOverComposite(btnType);
	    popOverComposite.setPositionRelativeParent(true);
	    
		
	    ControlListener resizeListener = new ControlListener() {
			public void controlResized(ControlEvent e) {
				popOverComposite.relocate();
				
// If needed				
//				popOverComposite.getPopOverCompositeShell().moveAbove(shell);
			}
			
			public void controlMoved(ControlEvent e) {
				popOverComposite.relocate();

// If needed					
//				popOverComposite.getPopOverCompositeShell().moveAbove(shell);		
			}
			
		};
		
		shell.addControlListener( resizeListener);
//		shell.addListener(SWT.Dispose, resizeListener);

		
	
		btnType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				popOverComposite.toggle();
			}
		});
		
		
		// Control

		
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
	    
	    
		
	}

	private void createBinding() {
	
		MatrosBinder bindingNewStype = new MatrosBinder(btnSearch);
	
		// Search always visible
		
		/*
		MatrosBinder.Twoway firstName =  bindingNewStype.bindComposite(text, true)
			.toModelTwoWay(input, "querystring", (x) -> { input.setQuerystring( (String) x); } ).build();
		
		bindingNewStype.setButtonVisible(btnSearch,firstName);
		*/
		
	}
}
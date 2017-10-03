 
package net.schwehla.matrosdms.rcp.parts.masterdata;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.internal.image.GIFFileFormat;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;

import net.schwehla.matrosdms.domain.core.InfoBaseElement;
import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;
import net.schwehla.matrosdms.rcp.parts.helper.InfoKategoryListWrapper;
import net.schwehla.matrosdms.rcp.swt.squarebutton.SquareButton;
import net.schwehla.matrosdms.rcp.swt.squarebutton.SquareButtonGroup;
import org.eclipse.wb.swt.SWTResourceManager;

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

	
	@Inject IMatrosServiceService matrosService;
	
	@Inject
	ESelectionService selectionService;
	
	
	
	@Inject
	InfoKategoryListWrapper wer;
	
	@Inject
	InfoKategoryListWrapper was;

	@Inject
	InfoKategoryListWrapper wo;

	@Inject
	InfoKategoryListWrapper art;

	
	
	@Inject Logger logger;
	
	SquareButtonGroup sg = new SquareButtonGroup();

	InfoBaseElement baseelement;


	ToolItem btnCommons;
	Composite compositeContentArea ;
	
	StackLayout sl_compositeContentArea = new StackLayout();
	Composite compositeWer ;
	Composite compositeWas ;
	Composite compositeWo ;
	Composite compositeArt ;
	

	
	List<ToolItem> toolItemList = new ArrayList<>();


	Composite compositeRoot;
	
	
	InfoContext _localDropfieldContext;
	private Table table;
	private Text txtOk;

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
			compositeWer = new Composite(compositeContentArea, SWT.NONE);
			
			try {
				wer.init(compositeWer,MyGlobalConstants.ROOT_WER);
			} catch (Exception e) {
				logger.error(e);
			}
			
			sl_compositeContentArea.topControl = compositeWer;
			compositeWer.setLayout(new GridLayout(1, false));
		}
		

		{
			compositeWas = new Composite(compositeContentArea, SWT.NONE);

			try {
				was.init(compositeWas,MyGlobalConstants.ROOT_WAS);
				compositeWas.setLayout(new GridLayout(1, false));
			} catch (Exception e) {
				logger.error(e);
			}
			
		}
		
		{
			compositeWo = new Composite(compositeContentArea, SWT.NONE);

			try {
				was.init(compositeWo,MyGlobalConstants.ROOT_WO);
				compositeWo.setLayout(new GridLayout(1, false));
			} catch (Exception e) {
				logger.error(e);
			}
			
		}
		
		{
			compositeArt = new Composite(compositeContentArea, SWT.NONE);

			try {
				was.init(compositeArt,MyGlobalConstants.ROOT_ART);
				compositeArt.setLayout(new GridLayout(1, false));
			} catch (Exception e) {
				logger.error(e);
			}
			
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

			sl_compositeContentArea.topControl = compositeWer;
			compositeContentArea.layout();

			break;
			
		case COMPONENT_WAS:

			sl_compositeContentArea.topControl = compositeWas;
			compositeContentArea.layout();
			
			break;


		case COMPONENT_WO:

			sl_compositeContentArea.topControl = compositeWo;
			compositeContentArea.layout();
			
			break;

			
		case COMPONENT_ART:

			sl_compositeContentArea.topControl = compositeArt;
			compositeContentArea.layout();
			
			break;

			
			

		default:
			break;
		}


		compositeContentArea.getParent().redraw();
		compositeContentArea.getParent().getParent().redraw();


	}
	
}
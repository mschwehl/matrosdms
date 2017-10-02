package net.schwehla.matrosdms.rcp.dialog;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.sideeffect.ISideEffectFactory;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.swt.WidgetSideEffects;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import net.schwehla.matrosdms.domain.api.E_ATTRIBUTETYPE;
import net.schwehla.matrosdms.domain.core.attribute.AttributeType;
import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.rcp.MatrosServiceException;

@Creatable
public class CreateNewAttributeMatrosTitleAreaDialog extends AbstractMatrosTitleAreaCreateAndNewDialog {


	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	
    @Inject
	public CreateNewAttributeMatrosTitleAreaDialog(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
        super(shell);
		setShellStyle(getShellStyle() | SWT.SHEET);
	}
    
    
		
	
	AttributeType attributeType;
	
	private void initNewElement() {
		attributeType = new AttributeType(Identifier.createNEW(),"");
	}


	private ComboViewer comboviewer;
	private Composite compositePreviewInput;
	private Composite compositePreviewMain;
	private Composite containerElementMain;
	private Group groupPreviewMainArea;
	private TabItem item;
	private Label lblDefault;
	private Label lblDescription;
	private Label lblIcon;
	private Label lblKey;
	private Label lblName;
	private Label lblPattern;


	private Label lblTyp;
	private Label lblUnit;
	private Label lblValidate;


	
	private TabItem preview;

	private Text textDefaultScriplet;

	private Text textDescription;
	private Text textIcon;
	private Text textkey;
	private Text textMainArea;
	private Text textName;
	private Text textPreviewInput;
	private Text textUnit;
	private Text txtPattern;
	private Text txtPreviewresult;
	private Text txtValidate;
	

	/**
	 * 
	 * Create contents of the button bar
	 * 
	 * @param parent
	 * 
	 */

	
	@Override
	protected Control createDialogArea(Composite parent) {

		parent.getShell().setText("TITLE");

		Composite area = (Composite) super.createDialogArea(parent);

		TabFolder folder = new TabFolder(area, SWT.FILL);
		folder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		item = new TabItem(folder,SWT.FILL);
		item.setText("Element");
		
		preview = new TabItem(folder,SWT.FILL);
		preview.setText("Preview");
		
		compositePreviewMain = new Composite(folder, SWT.NONE);
		preview.setControl(compositePreviewMain);
		compositePreviewMain.setLayout(new GridLayout(1, false));
		
		compositePreviewInput = new Composite(compositePreviewMain, SWT.NONE);
		compositePreviewInput.setLayout(new FillLayout(SWT.HORIZONTAL));
		compositePreviewInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		textPreviewInput = new Text(compositePreviewInput, SWT.BORDER);
		
		groupPreviewMainArea = new Group(compositePreviewMain, SWT.NONE);
		groupPreviewMainArea.setLayout(new GridLayout(1, false));
		groupPreviewMainArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		groupPreviewMainArea.setText("Preview");
		
		Composite composite = new Composite(groupPreviewMainArea, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));

	    
		txtPreviewresult = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		txtPreviewresult.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
	//	txtPreviewresult.setLayoutData(new GridData(GridData.FILL_BOTH));
		    
	

	    
	    
		Composite compositePreviewBottom = new Composite(compositePreviewMain, SWT.NONE);
		compositePreviewBottom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnTestPreview = new Button(compositePreviewBottom, SWT.NONE);
		btnTestPreview.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				String probe = textPreviewInput.getText();
				
				try {
					
					java.text.Format formatter ; 
					
					
					

					
					
					IStructuredSelection selection  = (IStructuredSelection) comboviewer.getSelection();
				
					if (!selection.isEmpty()) {
						

						
						  E_ATTRIBUTETYPE selectionType = (E_ATTRIBUTETYPE) selection.getFirstElement();
						  
						  switch (selectionType) {
						case NUMBER:
							
							formatter  = new DecimalFormat(txtPattern.getText());
							
							Number parsed = ((NumberFormat) formatter).parse(probe);
							
							txtPreviewresult.setText(formatter.format(parsed));
							txtPreviewresult.redraw();
						
							
							break;

						default:
						
							break;
						}
						  
					}
		
					
		
					
				} catch(Exception ex) {
					ex.printStackTrace();
					txtPreviewresult.setText(ex.getMessage());
				}
				
			}
		});
		btnTestPreview.setBounds(0, 0, 90, 30);
		btnTestPreview.setText("Test preview");
		
		
		containerElementMain = new Composite(folder, SWT.NONE);
		item.setControl(containerElementMain);
		

		containerElementMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		containerElementMain.setLayout(new GridLayout(2, false));

		lblTyp = new Label(containerElementMain, SWT.NONE);

		lblTyp.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		lblTyp.setText("typ");

		
		
		 comboviewer = new ComboViewer(containerElementMain, SWT.READ_ONLY);

		comboviewer.setContentProvider(ArrayContentProvider.getInstance());
		comboviewer.setLabelProvider(new LabelProvider() {
	        @Override
	        public String getText(Object element) {
	                if (element instanceof E_ATTRIBUTETYPE) {
	                		E_ATTRIBUTETYPE person = (E_ATTRIBUTETYPE) element;
	                        return person.name();
	                }
	                return super.getText(element);
	        }
		});
	
		
	
		comboviewer.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		comboviewer.addSelectionChangedListener( new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				
				if (event.getSelection() != null) {
					
					  IStructuredSelection selection = (IStructuredSelection)event.getSelection();
					  
					  if (!selection.isEmpty()) {
						  E_ATTRIBUTETYPE selectionType = (E_ATTRIBUTETYPE) selection.getFirstElement();
						  
						  switch (selectionType) {
						case NUMBER:
							
							textUnit.setEnabled(true);
						
							
							break;

						default:
							
							textUnit.setEnabled(false);
					
							break;
						}
						  
					  }
					  
					
				}
				
				
			}
		});
		

		lblKey = new Label(containerElementMain, SWT.NONE);

		lblKey.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		lblKey.setText("Key");

		textkey = new Text(containerElementMain, SWT.BORDER);
		

		textkey.addVerifyListener(new VerifyListener() {
	      @Override
		public void verifyText(VerifyEvent e) {
	          e.text = e.text.toUpperCase();
	        }
	    });
	    

		textkey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lblName = new Label(containerElementMain, SWT.NONE);

		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		lblName.setText("Name");

		textName = new Text(containerElementMain, SWT.BORDER);

		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lblIcon = new Label(containerElementMain, SWT.NONE);

		lblIcon.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		lblIcon.setText("icon");

		textIcon = new Text(containerElementMain, SWT.BORDER);

		textIcon.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
				lblDefault = new Label(containerElementMain, SWT.NONE);
				
						lblDefault.setText("defaultvalue");
				
						textDefaultScriplet = new Text(containerElementMain, SWT.BORDER);
						
								textDefaultScriplet.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblPattern = new Label(containerElementMain, SWT.NONE);
		lblPattern.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPattern.setText("pattern");
		
		txtPattern = new Text(containerElementMain, SWT.BORDER);
		txtPattern.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblValidate = new Label(containerElementMain, SWT.NONE);
		lblValidate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblValidate.setText("validate");
		
		txtValidate = new Text(containerElementMain, SWT.BORDER);
		txtValidate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblUnit = new Label(containerElementMain, SWT.NONE);
		lblUnit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUnit.setText("unit");
		
		textUnit = new Text(containerElementMain, SWT.BORDER);
		textUnit.setEnabled(false);
		textUnit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
				lblDescription = new Label(containerElementMain, SWT.NONE);
				
						lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
						
								lblDescription.setText("description");
				
						textDescription = new Text(containerElementMain, SWT.BORDER);
						
								textDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
			
		// initial Data

		List types = new ArrayList();
		
		for (E_ATTRIBUTETYPE type:  E_ATTRIBUTETYPE.values()) {
			types.add(type);
		}
		
	

		// set the input of the Viewer,
		// this input is send to the content provider

		comboviewer.setInput(types);
		
		initNewElement();
		

		return area;

	}


	
	@Override
	protected void buildBinding() {
		
		
        ISideEffectFactory sideEffectFactory = WidgetSideEffects.createFactory(textName);

        
        IObservableValue <String> swtItemName = WidgetProperties.text(SWT.Modify).observe(textName);
		IObservableValue<String> myModelUser = PojoProperties.value("name").observe(attributeType);
         
		 // die eine richtung
        sideEffectFactory.create(myModelUser::getValue, swtItemName::setValue);

        sideEffectFactory.create(swtItemName::getValue, x -> {

               if (x != null) {
            	   attributeType.setName(x);
               } else {
            	   attributeType.setName(null);
               }
        });

        
		IObservableValue<Boolean> hasSelectionObservable = ComputedValue
				.create(() -> swtItemName.getValue() != null && swtItemName.getValue().length() > 0 );

		
		sideEffectFactory.create(hasSelectionObservable::getValue,buttonOk::setEnabled);
		sideEffectFactory.create(hasSelectionObservable::getValue,buttonCreateAndNew::setEnabled);
		
		
	}
	
	@Override

	protected Point getInitialSize() {

		this.getShell().pack();

		Point p = super.getInitialSize();

	//	p.x = p.x / 2;

		// for some reasons not all fit in :-(

		p.y = p.y + 20;

		return p;

	}
	

	
	
	
	@Override
	protected void createNewElementInDatabase() throws MatrosServiceException {


		
		
	    Objects.requireNonNull(attributeType ,"need new AttributeType"); //$NON-NLS-1$

		StructuredSelection sel = (StructuredSelection) comboviewer.getSelection();
		E_ATTRIBUTETYPE element = (E_ATTRIBUTETYPE) sel.getFirstElement();
		

		attributeType.setName(textName.getText());
		attributeType.setDescription(textDescription.getText());
		attributeType.setIcon(textIcon.getText());
		attributeType.setKey(textkey.getText());
		attributeType.setType(element);
		
		attributeType.setDefaultValueScript(textDefaultScriplet.getText());
		attributeType.setValidateScript(txtPattern.getText());
		attributeType.setPattern(txtPattern.getText());
		
		attributeType.setUnit(textUnit.getText());
		

	    matrosService.createAttributeType(attributeType);
		lastRecentSavedKategory = attributeType;
		
		clearGuiFields();
		initNewElement();
	    
		
	}
	private void clearGuiFields() {
		// TODO Auto-generated method stub
		
	}

}



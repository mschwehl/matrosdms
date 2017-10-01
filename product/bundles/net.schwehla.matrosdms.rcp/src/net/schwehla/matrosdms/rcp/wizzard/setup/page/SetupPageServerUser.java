package net.schwehla.matrosdms.rcp.wizzard.setup.page;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.databinding.observable.sideeffect.ISideEffect;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.jface.databinding.viewers.IViewerObservableValue;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;

import net.schwehla.matrosdms.domain.admin.MatrosUser;
import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.rcp.wizzard.model.setup.Masterdata;
import net.schwehla.matrosdms.rcp.wizzard.page.dialog.UserDialog;


@Creatable
public class SetupPageServerUser extends WizardPage {

	@Inject
	@Translation MatrosMessage messages;
	
	@Inject
	IEclipseContext context;
	

	@Inject
	Masterdata masterData;
	
	@Inject Logger logger;
	
	/**
	 * Create the wizard.
	 */
	@Inject
	public SetupPageServerUser() {
		super("wizardPage");
		setTitle("Welcome to Matros");
		setDescription("xyx");
	}



	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		

		
		Composite compositeInboxPage = new Composite(parent, SWT.NULL);

		setControl(compositeInboxPage);
		compositeInboxPage.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TabFolder tabFolder = new TabFolder(compositeInboxPage, SWT.NONE);
		
		TabItem tbtmInbox = new TabItem(tabFolder, SWT.FILL);
		tbtmInbox.setText("Benutzer");
		
		Composite compositeWholeContent = new Composite(tabFolder, SWT.FILL);
		tbtmInbox.setControl(compositeWholeContent);
		compositeWholeContent.setLayout(new GridLayout(1, false));
		
		Composite compositeDescription = new Composite(compositeWholeContent, SWT.NONE);
		compositeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeDescription.setLayout(new GridLayout(1, false));
		
		Label lblDescription = new Label(compositeDescription, SWT.NONE);
		lblDescription.setText("Verwalten Sie die Benutzer des Matros-DMS");
		
		Composite compositeTreeAndButtons = new Composite(compositeWholeContent, SWT.NONE);
		compositeTreeAndButtons.setLayout(new GridLayout(2, false));
		compositeTreeAndButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeTree = new Composite(compositeTreeAndButtons, SWT.NONE);
		compositeTree.setLayout(new GridLayout(1, false));
		compositeTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		
		TableViewer treeViewer = new TableViewer(compositeTree, SWT.BORDER);
		Table tree = treeViewer.getTable();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		treeViewer.setContentProvider(new ArrayContentProvider());
		treeViewer.setLabelProvider(new MyTreeLabelProvider());
		 
		
		 
		Composite compositeButtons = new Composite(compositeTreeAndButtons, SWT.NONE);
		compositeButtons.setLayout(new GridLayout(1, false));
		compositeButtons.setLayoutData(new GridData(SWT.NONE, SWT.TOP, false, true, 1, 1));
		
		Button btnAddElement = new Button(compositeButtons, SWT.NONE);
		btnAddElement.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				MatrosUser user ;
				

					user = new MatrosUser(Identifier.createNEW(), "");
					
				    UserDialog dialog;
					try {
						dialog = new UserDialog(Display.getCurrent().getActiveShell(),user);
						
					    dialog.create();
						  
		                // get the new values from the dialog
		                if (dialog.open() == Window.OK) {
		                	masterData.getUserList().add(user);
		                }

			            treeViewer.refresh(true);
			            getContainer().updateButtons();
						
					} catch (Exception e1) {
						logger.error(e1);
					}
				    
			
	   
			}
		});
		btnAddElement.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnAddElement.setText("Benutzer hinzuf\u00FCgen");
		
		Button btnRemoveElement = new Button(compositeButtons, SWT.NONE);
		btnRemoveElement.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (treeViewer.getSelection() != null 
						&& ! treeViewer.getSelection().isEmpty()) {
					
					List list2 = treeViewer.getStructuredSelection().toList();
					masterData.getUserList().removeAll(list2);
					
					treeViewer.refresh();
					getContainer().updateButtons();
				}
			
			}
		});
		btnRemoveElement.setEnabled(false);
		btnRemoveElement.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRemoveElement.setText("Benutzer l\u00F6schen");
		
		Button btnEdit = new Button(compositeButtons, SWT.NONE);
		btnEdit.setEnabled(false);
		btnEdit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnEdit.setText("Editieren");
		
		btnEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (treeViewer.getSelection() != null 
						&& ! treeViewer.getSelection().isEmpty()) {
					
					MatrosUser user = (MatrosUser) treeViewer.getStructuredSelection().toList().get(0);
				
					
				    UserDialog dialog;
					try {
						dialog = new UserDialog(Display.getCurrent().getActiveShell(),user);
						
						  
		                // get the new values from the dialog
		                if (dialog.open() == Window.OK) {
		                }
						
		                
					} catch (Exception e1) {
							logger.error(e1);
					}
			
					treeViewer.refresh();
					getContainer().updateButtons();
				}
			
			}
		});
		
		
		
		
		
		// init data
		

		treeViewer.setInput(masterData.getUserList());

		
		IViewerObservableValue  widgetSelection =
				   ViewerProperties
				      .singleSelection()
				      .observe(treeViewer);
		 

 
		
		// track whether the selection list is empty or not
		// (viewerSelectionObservable.isEmpty() is a tracked getter!)
		IObservableValue<Boolean> hasSelectionObservable = ComputedValue
				.create(() -> widgetSelection.getValue() != null);

		// once the selection state(Boolean) changes the ISideEffect will
		// update the button
		ISideEffect deleteButtonEnablementSideEffect = ISideEffect.create(hasSelectionObservable::getValue,
				btnRemoveElement::setEnabled);

		btnRemoveElement.addDisposeListener(e -> deleteButtonEnablementSideEffect.dispose());
		
		
		ISideEffect editButtonEnablementSideEffect = ISideEffect.create(hasSelectionObservable::getValue,
				btnEdit::setEnabled);

		btnRemoveElement.addDisposeListener(e -> editButtonEnablementSideEffect.dispose());
		
		

		
	}
	
	
	
	 public class MyTreeLabelProvider extends LabelProvider {

		   @Override
		   public String getText(Object element) {
			   
		     if (element instanceof MatrosUser) {
		       return ((MatrosUser) element).getName();
		     } 
		     return null;
		   }
	 }
	 

	 
	 public class MyTreeContentProvider implements ITableLabelProvider {

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			
			if (element instanceof MatrosUser) {
				return ((MatrosUser) element).getName();
			}
			
			return null;
			
		}
		 
		 
	 }

	 
     @Override
    public boolean isPageComplete() {
    	return masterData.getUserList().size() > 0;
    }
     


}

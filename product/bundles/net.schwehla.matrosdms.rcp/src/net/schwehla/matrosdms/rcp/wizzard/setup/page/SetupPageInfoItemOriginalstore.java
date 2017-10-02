package net.schwehla.matrosdms.rcp.wizzard.setup.page;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.databinding.observable.sideeffect.ISideEffect;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.jface.databinding.viewers.IViewerObservableValue;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;

import net.schwehla.matrosdms.domain.core.InfoOrginalstore;
import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.rcp.dialog.OriginalstoreDialog;
import net.schwehla.matrosdms.rcp.wizzard.model.setup.Masterdata;


@Creatable
public class SetupPageInfoItemOriginalstore extends WizardPage {

	@Inject
	@Translation
	MatrosMessage messages;
	
	@Override
	public boolean isPageComplete() {
		return true;
	}
	
	@Inject
	Masterdata masterData;
	
	/**
	 * Create the wizard.
	 */
	@Inject
	public SetupPageInfoItemOriginalstore(@Translation MatrosMessage messages) {
		super("wizardPage");
		setTitle(messages.wizzard_orginalstore_title);
		setDescription(messages.wizzard_orginalstore_description);
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
		tbtmInbox.setText(messages.wizzard_orginalstore_ablage);
		
		Composite compositeWholeContent = new Composite(tabFolder, SWT.FILL);
		tbtmInbox.setControl(compositeWholeContent);
		compositeWholeContent.setLayout(new GridLayout(1, false));
		
		Composite compositeDescription = new Composite(compositeWholeContent, SWT.NONE);
		compositeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeDescription.setLayout(new GridLayout(1, false));
		
		Label lblDescription = new Label(compositeDescription, SWT.NONE);
		lblDescription.setText(messages.wizzard_orginalstore_control_description);
		
		Composite compositeTreeAndButtons = new Composite(compositeWholeContent, SWT.NONE);
		compositeTreeAndButtons.setLayout(new GridLayout(2, false));
		compositeTreeAndButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeTree = new Composite(compositeTreeAndButtons, SWT.NONE);
		compositeTree.setLayout(new GridLayout(1, false));
		compositeTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		
		TreeViewer treeViewer = new TreeViewer(compositeTree, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		treeViewer.setContentProvider(new MyTreeContentProvider());
		treeViewer.setLabelProvider(new MyTreeLabelProvider());
		 
	
		
		 
		Composite compositeButtons = new Composite(compositeTreeAndButtons, SWT.NONE);
		compositeButtons.setLayout(new GridLayout(1, false));
		compositeButtons.setLayoutData(new GridData(SWT.NONE, SWT.TOP, false, true, 1, 1));
		
		Button btnAddElement = new Button(compositeButtons, SWT.NONE);
		btnAddElement.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			
	            treeViewer.refresh(true);
	            getContainer().updateButtons();
	            
					// storage-Dialog
				
            		InfoOrginalstore orginalNew = new InfoOrginalstore(Identifier.createNEW(),"");
				    
			        OriginalstoreDialog dialog = new OriginalstoreDialog(Display.getCurrent().getActiveShell(), orginalNew );
			       
				    dialog.create();
					  
				     // get the new values from the dialog
	                if (dialog.open() == Window.OK) {
	                	
					    masterData.getOrignalStoreList().add(orginalNew);
					    
					    treeViewer.refresh(true);
					    getContainer().updateButtons();
					    
	                }
				
			    		
			}
		});
		
		
		btnAddElement.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnAddElement.setText(messages.wizzard_orginalstore_add_element);
		
		Button btnRemoveElement = new Button(compositeButtons, SWT.NONE);
		btnRemoveElement.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (treeViewer.getSelection() != null 
						&& ! treeViewer.getSelection().isEmpty()) {
					
					List list2 = treeViewer.getStructuredSelection().toList();
					masterData.getOrignalStoreList().removeAll(list2);
					
					treeViewer.refresh();
					getContainer().updateButtons();
				}
			
			}
		});
		btnRemoveElement.setEnabled(false);
		btnRemoveElement.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRemoveElement.setText(messages.wizzard_orginalstore_delete_element);
		
		Button btnEdit = new Button(compositeButtons, SWT.NONE);
		btnEdit.setEnabled(false);
		btnEdit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnEdit.setText(messages.wizzard_orginalstore_edit_element);
		
		// init data
		

		treeViewer.setInput(masterData.getOrignalStoreList());

		
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
		

		
	}
	
	
	
	 public class MyTreeLabelProvider extends LabelProvider {

		   @Override
		   public String getText(Object element) {
		     if (element instanceof InfoOrginalstore) {
		       return ((InfoOrginalstore) element).getName();
		     } 
		     return null;
		   }
	 }
	 
     private static final Object[] EMPTY_ARRAY = new Object[0];

	 
	 public class MyTreeContentProvider implements ITreeContentProvider {


	     //Called just for the first-level objects.
	     //Here we provide a list of objects
	     @Override
	     public Object[] getElements(Object inputElement) {
	       if (inputElement instanceof List)
	         return ((List) inputElement).toArray();
	       else
	         return EMPTY_ARRAY;
	     }

	     //Queried to know if the current node has children
	     @Override
	     public boolean hasChildren(Object element) {
	       return false;
	     }

	     //Queried to load the children of a given node
	     @Override
	     public Object[] getChildren(Object parentElement) {
	       return EMPTY_ARRAY;
	     }

	     @Override
	     public void dispose() {
	     }

	     @Override
	     public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	     }

	     @Override
	     public Object getParent(Object element) {
	       return null;
	     }

	   }
}

package net.schwehla.matrosdms.rcp.preferences;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.core.databinding.observable.sideeffect.ISideEffect;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.jface.databinding.viewers.IViewerObservableValue;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;

import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;
import net.schwehla.matrosdms.rcp.parts.helper.MatrosPreferenceInbox;
import net.schwehla.matrosdms.rcp.wizzard.model.setup.Masterdata;

@Creatable
public class InboxPreferencePage extends PreferencePage {
	
    public InboxPreferencePage()
    {
        super();
        setTitle("Inbox");
    }
    
	@Inject
	@Translation
	MatrosMessage messages;
	
	
	@Inject
	Masterdata masterData;
	
	
	private static final Object[] EMPTY_ARRAY = new Object[0];

	
	@Override
	public boolean performOk() {

		performApply() ;
		
		return true;
	}
	
	@Override
	protected void performApply() {
		
        
     	 String inboxPath = masterData.getInboxList().stream().map(MatrosPreferenceInbox::getPath)
     			 .collect(Collectors.joining(MyGlobalConstants.Preferences.DELIMITER));
     	 
     	
     	 getPreferenceStore().setValue(MyGlobalConstants.Preferences.INBOX_PATH, inboxPath);
     	
		
	}
	
	/**
	 * Create contents of the preference page.
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		
		
		String inbox = getPreferenceStore().getString(MyGlobalConstants.Preferences.INBOX_PATH);
		String[] inboxArray = inbox.split(MyGlobalConstants.Preferences.DELIMITER);
		
		masterData.getInboxList().clear();
		
		for (String box : inboxArray) {
			MatrosPreferenceInbox p = new MatrosPreferenceInbox();
			p.setPath(box);
			masterData.getInboxList().add(p);
		}
		
		
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite compositeTreeAndButtons = new Composite(container, SWT.NONE);
		compositeTreeAndButtons.setLayout(new GridLayout(2, false));
		
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
				
			    DirectoryDialog dialog = new DirectoryDialog( Display.getDefault().getActiveShell());
			    
			    String result = dialog.open();
			    
			    if (result != null) {
			    	
			    	MatrosPreferenceInbox inbox = new MatrosPreferenceInbox();	
				    inbox.setPath(result);
				    
				    masterData.getInboxList().add(inbox);
				    
				    treeViewer.refresh(true);
				    getContainer().updateButtons();
			    }
			    		
			}
		});
		btnAddElement.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnAddElement.setText(messages.wizzard_inbox_add_folder);
		
		Button btnRemoveElement = new Button(compositeButtons, SWT.NONE);
		btnRemoveElement.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (treeViewer.getSelection() != null 
						&& ! treeViewer.getSelection().isEmpty()) {
					
					List list2 = treeViewer.getStructuredSelection().toList();
					masterData.getInboxList().removeAll(list2);
					
					treeViewer.refresh();
					getContainer().updateButtons();
				}
			
			}
		});
		btnRemoveElement.setEnabled(false);
		btnRemoveElement.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRemoveElement.setText(messages.wizzard_inbox_remove_folder);
		
		Button btnEdit = new Button(compositeButtons, SWT.NONE);
		btnEdit.setEnabled(false);
		btnEdit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnEdit.setText(messages.common_edit);
		
		// -- Setup data
		
	    treeViewer.setInput(masterData.getInboxList());
	    
	
		// create the bindings
		
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
		
		
		return container;
	}



	 public class MyTreeLabelProvider extends LabelProvider {

		   @Override
		   public String getText(Object element) {
		     if (element instanceof MatrosPreferenceInbox) {
		       return ((MatrosPreferenceInbox) element).getPath();
		     } 
		     return null;
		   }
	 }
	 
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
    
    @Override
    public String toString() {
    	return "1_inbox";
    }
	

}

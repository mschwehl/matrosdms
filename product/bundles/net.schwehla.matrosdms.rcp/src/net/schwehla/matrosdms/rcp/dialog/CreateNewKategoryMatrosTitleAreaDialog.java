package net.schwehla.matrosdms.rcp.dialog;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.sideeffect.ISideEffect;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.IViewerObservableValue;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.domain.util.ObjectCloner;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.swt.contentprovider.TaggraphContentprovider;
import net.schwehla.matrosdms.rcp.swt.labelprovider.TaggraphLabelProvider;

/**
 * Creates a new Kategory
 */
@Creatable
public class CreateNewKategoryMatrosTitleAreaDialog extends AbstractMatrosTitleAreaCreateAndNewDialog  {
	

	
	// the current tree
	InfoKategory localTreeroot ;
	
	// selection in the current tree
	InfoKategory selectedParentForNewRoot = null;
	



	private Button btnObject;
	private Text   kategoryName;
	private Text   parentName;
	private Text   kategoryIcon;

	
	TreeViewer treeViewer;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	
    @Inject
	public CreateNewKategoryMatrosTitleAreaDialog(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
        super(shell);
		setShellStyle(getShellStyle() | SWT.SHEET);
	}



	

	
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle(messages.createNewKategoryDialog_title);
		
		
		Composite area = (Composite) super.createDialogArea(parent);
		
		Composite composite = new Composite(area, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		composite.setLayout(gridLayout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		
		Group group = new Group(composite, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		group.setText("Graph");
		group.setLayout(new GridLayout(1, false));
		
		treeViewer = new TreeViewer(group, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		treeViewer.setUseHashlookup(true) ;
		
		
		Tree tree = treeViewer.getTree();
		GridData gd_tree = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tree.heightHint = 200;
		tree.setLayoutData(gd_tree);
		

		
		Group group_2 = new Group(composite, SWT.NONE);
		group_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		group_2.setText("Kategorie");
		group_2.setLayout(new GridLayout(2, false));
		
		
		Label labelParent = new Label(group_2, SWT.NONE);
		labelParent.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelParent.setText(messages.createNewKategoryDialog_label_parent);
		
		parentName = new Text(group_2, SWT.BORDER | SWT.NO_FOCUS);
		parentName.setEditable(false);
		parentName.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
		
		
		Label label = new Label(group_2, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText(messages.createNewKategoryDialog_label_name);
		
		kategoryName = new Text(group_2, SWT.BORDER);
		kategoryName.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
		
		Label label_1 = new Label(group_2, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("Icon");
		
		kategoryIcon = new Text(group_2, SWT.BORDER);
		kategoryIcon.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label label_2 = new Label(group_2, SWT.NONE);
		label_2.setText(messages.createNewKategoryDialog_label_object);
		
		btnObject = new Button(group_2, SWT.CHECK);

		kategoryName.setFocus();
		
		
		treeViewer.setContentProvider(new TaggraphContentprovider());
		treeViewer.setLabelProvider(
				new DelegatingStyledCellLabelProvider(new TaggraphLabelProvider()));

		// Swt tree have a bug the root needs a pseudoroot to display the first element
		
		InfoKategory pseudoRoot = new InfoKategory(Identifier.createNEW(),"PSEUDOROOT");
		pseudoRoot.connectWithChild(localTreeroot);
		
		
		treeViewer.setInput(pseudoRoot);
		
		if (selectedParentForNewRoot != null) {
			treeViewer.setExpandedState(selectedParentForNewRoot, true);
			treeViewer.setSelection( new StructuredSelection(selectedParentForNewRoot));
		} else {
			
			treeViewer.setSelection( new StructuredSelection(localTreeroot));
			selectedParentForNewRoot = localTreeroot;
			treeViewer.setExpandedState(selectedParentForNewRoot, true);
			treeViewer.expandToLevel(2);
		}




		return area;
	}


	@Override
	protected void buildBinding() {
		
		

		IViewerObservableValue  viewerSelectionObservable = ViewerProperties.singleSelection()
				.observe(treeViewer);

	
		// Binding the new Style
		IObservableValue<String> treeViewerValueObserveDetailValue = PojoProperties
				.value((Class) viewerSelectionObservable.getValueType(), "name", String.class)
				.observeDetail(viewerSelectionObservable);
	
		// Binding the new Style
		IObservableValue<InfoKategory> mySeleted =  viewerSelectionObservable;
		
		
		// binding must not be emtpy
        IObservableValue <String> swtNameBinding = WidgetProperties.text(SWT.Modify).observe(kategoryName);

        // Bind selected Root to summary
    	ISideEffect sideEffect =
				ISideEffect.create(
						() -> {
							return "" + treeViewerValueObserveDetailValue.getValue();
						}, parentName::setText);
    	
    	
     	ISideEffect sideEffectCurrentSelection =
		ISideEffect.create(mySeleted::getValue, e -> { 
			this.selectedParentForNewRoot = e; } 
		);
    	
    	
    	
		// track whether the selection list is empty or not
		// (viewerSelectionObservable.isEmpty() is a tracked getter!)
		IObservableValue<Boolean> hasSelectionObservable = ComputedValue
				.create(() -> swtNameBinding.getValue() != null && swtNameBinding.getValue().length() > 0 );

		
		
		
		ISideEffect enableOkButtonSideEffect = ISideEffect.create(hasSelectionObservable::getValue,buttonOk::setEnabled);
		ISideEffect enableOkButtonSideEffectCreateAndNew = ISideEffect.create(hasSelectionObservable::getValue,buttonCreateAndNew::setEnabled);
		
		// Dispose the bindings
        
        treeViewer.getTree().addDisposeListener(e -> enableOkButtonSideEffect.dispose());
        treeViewer.getTree().addDisposeListener(e -> enableOkButtonSideEffectCreateAndNew.dispose());
        treeViewer.getTree().addDisposeListener(e -> sideEffectCurrentSelection.dispose());        
        
        treeViewer.getTree().addDisposeListener(e -> sideEffect.dispose());
        treeViewer.getTree().addDisposeListener(e -> treeViewerValueObserveDetailValue.dispose());
//        treeViewer.getTree().addDisposeListener(e -> sideEffect2.dispose());
        treeViewer.getTree().addDisposeListener(e -> mySeleted.dispose());
		
	}
	

		public InfoKategory getRoot() {
			return localTreeroot;
		}

		public void setClonedRoot(InfoKategory root) {
			
			try {
				this.localTreeroot = new ObjectCloner<InfoKategory>().cloneThroughSerialize(root);
			} catch (Exception e2) {
				logger.error(e2);
				
				close();
				return;
			}

			
		}

		
		public InfoKategory getSelected() {
			return selectedParentForNewRoot;
		}

		public void setSelectedRoot(InfoKategory selected) {
			this.selectedParentForNewRoot = selected;
		}

		
		@Override
		public void createNewElementInDatabase() throws MatrosServiceException {
			
		
			    Objects.requireNonNull(getSelected() ,"Parent must be selected"); //$NON-NLS-1$

				InfoKategory newElementToSave = new InfoKategory(Identifier.createNEW(), kategoryName.getText());
				newElementToSave.setIcon(kategoryIcon.getText());
				newElementToSave.setObject( btnObject.getSelection());

				matrosService.createInfoKategory(newElementToSave, getSelected().getIdentifier() );
				
				clearGuiFields();
			
				lastRecentSavedKategory = newElementToSave;
		
				InfoKategory pseudo = (InfoKategory) treeViewer.getInput();
				pseudo.getChildren().clear();
				
				setClonedRoot( matrosService.getInfoKategoryByIdentifier(localTreeroot.getIdentifier() ));
				
				
				pseudo.connectWithChild(localTreeroot);
				
				treeViewer.refresh(true);
				
				treeViewer.setExpandedState(getSelected(), true);
				treeViewer.setAutoExpandLevel( treeViewer.ALL_LEVELS);
				treeViewer.setSelection( new StructuredSelection(getSelected()));
				treeViewer.expandToLevel(newElementToSave,treeViewer.ALL_LEVELS);
				
				treeViewer.getTree().update();
				
				if (treeViewer.getStructuredSelection() != null
						&& !treeViewer.getStructuredSelection().isEmpty()) {

					InfoKategory k = (InfoKategory) treeViewer.getStructuredSelection().getFirstElement();
					
					// XXX evaluate
					// BUG IN BINDING:: VALUE OF PARENT SOMETIMES NULL
					parentName.setText(k.getName());
					
				}
 				

				
		
				// Focus for new Element
				kategoryName.setFocus();
				

		}




		private void clearGuiFields() {
			
			btnObject.setSelection(false);
			kategoryName.setText("");
			parentName.setText("");
			kategoryIcon.setText("");
			
			
		}


		
}

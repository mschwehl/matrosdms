package net.schwehla.matrosdms.rcp.wizzard.setup.page;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.sideeffect.ISideEffect;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.IViewerObservableValue;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.rcp.internal.KategoryContentProvider;
import net.schwehla.matrosdms.rcp.internal.KategoryLabelProvider;


public abstract class AbstractSetupPageKategory extends WizardPage {

	
	@Override
	public boolean isPageComplete() {
		return true;
	}
	
	private Tree _treeTargetdefinition;
	private Tree _treeExample;
	private TreeViewer _treeViewerExample;
	private TreeViewer _treeViewerTarget;
	private Text _swtTextName;
	private Text _swtTextIcon;

	/**
	 * 
	 * Create the wizard.
	 * 
	 */


	public AbstractSetupPageKategory(String name) {

		super(name);
		setTitle("Welcome to Matros2");
		setDescription("Document mangement System");

	}

	private ImageDescriptor createImageDescriptor() {
		return null;
	}

	/**
	 * 
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 * 
	 */

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		
		Composite compositeInboxPage = new Composite(parent, SWT.NULL);
		setControl(compositeInboxPage);
		compositeInboxPage.setLayout(new FillLayout(SWT.HORIZONTAL));

		TabFolder tabFolder = new TabFolder(compositeInboxPage, SWT.NONE);

		TabItem tbtmInbox = new TabItem(tabFolder, SWT.FILL);
		tbtmInbox.setText("123");


		Composite compositeWholeContent = new Composite(tabFolder, SWT.FILL);
		tbtmInbox.setControl(compositeWholeContent);
		compositeWholeContent.setLayout(new GridLayout(1, false));

		Composite compositeDescription = new Composite(compositeWholeContent, SWT.NONE);
		compositeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeDescription.setLayout(new GridLayout(1, false));

		Label lblDescription = new Label(compositeDescription, SWT.NONE);
		lblDescription.setText("Description");

		Composite compositeTreeAndButtons = new Composite(compositeWholeContent, SWT.NONE);
		compositeTreeAndButtons.setLayout(new GridLayout(1, false));
		compositeTreeAndButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		
		Composite container = new Composite(compositeTreeAndButtons, SWT.NULL);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		container.setLayout(new GridLayout(1, false));
		Composite compositeTreeMapping = new Composite(container, SWT.FILL);
		compositeTreeMapping.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeTreeMapping.setLayout(new GridLayout(2, true));

		Group grpZielstruktur = new Group(compositeTreeMapping, SWT.NONE);
		grpZielstruktur.setText("Zielstruktur");

		grpZielstruktur.setLayout(new GridLayout(1, false));

		grpZielstruktur.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		

		
	
		_treeViewerTarget = new TreeViewer(grpZielstruktur, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

		_treeTargetdefinition = _treeViewerTarget.getTree();

		_treeTargetdefinition.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// Data

		_treeViewerTarget.setContentProvider(new KategoryContentProvider());

		_treeViewerTarget.setLabelProvider(
				new DelegatingStyledCellLabelProvider(new KategoryLabelProvider(createImageDescriptor())));

		_treeViewerTarget.getTree().addMouseListener(new MouseAdapter() {

			@Override

			public void mouseDoubleClick(MouseEvent e) {

			}

		});

		_treeViewerTarget.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// _swtTextName.selectAll();
				// _swtTextName.setFocus();

			}
		});


		// XXX
		if (root != null) {
			_treeViewerTarget.setInput(root);
		}


		_treeViewerTarget.refresh();

		// Drag and Drop
		DragSource source = new DragSource(_treeViewerTarget.getControl(),
				DND.DROP_MOVE | DND.DROP_LINK | DND.DROP_COPY);

		// Drag and Drop
		DropTarget droptarted = new DropTarget(_treeViewerTarget.getControl(),
				DND.DROP_MOVE | DND.DROP_LINK | DND.DROP_COPY);

		Composite composite_1 = new Composite(grpZielstruktur, SWT.NONE);

		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

		composite_1.setLayout(new GridLayout(2, false));

		Button btnCreateElement = new Button(composite_1, SWT.NONE);

		btnCreateElement.setText("Add Child");

		btnCreateElement.addSelectionListener(new SelectionAdapter() {

			@Override

			public void widgetSelected(SelectionEvent e) {

				TreeItem selectedNode = getSeletedNode();

				if (selectedNode != null) {

					InfoKategory parent = (InfoKategory) selectedNode.getData();

					InfoKategory child = new InfoKategory(Identifier.createNEW(), "NEW CATEGORY");
					parent.connectWithChild(child);

					_treeViewerTarget.refresh(true);

					_treeViewerTarget.expandAll();

					IStructuredSelection selection = new StructuredSelection(child);
					_treeViewerTarget.setSelection(selection);

					Display.getCurrent().asyncExec(new Runnable() {
						@Override
						public void run() {
							synchronized (_swtTextName) {
								// _swtTextName.setFocus();
								_swtTextName.selectAll();
							}

						}
					});

				}

			}

		});

		Button btnDeleteElement = new Button(composite_1, SWT.NONE);

		btnDeleteElement.addSelectionListener(new SelectionAdapter() {

			@Override

			public void widgetSelected(SelectionEvent e) {

				TreeItem item = getSeletedNode();

				if (item != null) {

					// Löschen über den Parent und in Child/Parent-Collection
					// entfernen

				}

			}

		});

		btnDeleteElement.setText("Delete");

		Group groupExample = new Group(compositeTreeMapping, SWT.NONE);

		groupExample.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));

		groupExample.setText("Example");

		groupExample.setLayout(new GridLayout(1, false));

		_treeViewerExample = new TreeViewer(groupExample, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

		_treeExample = _treeViewerExample.getTree();

		_treeExample.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite composite_2 = new Composite(groupExample, SWT.NONE);

		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

		composite_2.setLayout(new GridLayout(1, false));

		Combo comboSelectExample = new Combo(composite_2, SWT.NONE);

		Composite composite = new Composite(container, SWT.NONE);

		composite.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));

		composite.setLayout(new GridLayout(1, false));

		Group grpKategorie = new Group(composite, SWT.NONE);

		grpKategorie.setLayout(new GridLayout(2, false));

		grpKategorie.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		grpKategorie.setText("Kategorie");

		Label lblName = new Label(grpKategorie, SWT.NONE);

		lblName.setText("Name");

		_swtTextName = new Text(grpKategorie, SWT.BORDER);

		_swtTextName.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));

		Label lblIcon = new Label(grpKategorie, SWT.NONE);

		lblIcon.setText("Icon");

		_swtTextIcon = new Text(grpKategorie, SWT.BORDER);

		_swtTextIcon.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Label lblIsobject = new Label(grpKategorie, SWT.NONE);

		lblIsobject.setText("Objekt");

		Button btnCheckButton = new Button(grpKategorie, SWT.CHECK);

		ControlDecoration controlDecoration = new ControlDecoration(btnCheckButton, SWT.LEFT | SWT.TOP);

		controlDecoration.setDescriptionText("Object is a thing that physically exists like a person or a company");

		// TODO Recherchieren wie das klappen soll

		// ISWTObservableValue personFirstNameTextObservable =
		// WidgetProperties.text(SWT.Modify).observe(swtIcon);

		// Binding über die Observable




		// Step 1: Get JFace's LocalSelectionTransfer instance
		final LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();

		source.setTransfer(new Transfer[] { transfer });

		source.addDragListener(new DragSourceAdapter() {

			@Override
			public void dragStart(DragSourceEvent event) {

				Tree table = (Tree) source.getControl();
				TreeItem[] selection = table.getSelection();

				if (selection != null && selection.length == 1) {
					event.doit = true;
				} else {
					event.doit = false;
				}

			}

			public void dragSetData(DragSourceEvent event) {

				Tree table = (Tree) source.getControl();
				TreeItem[] selection = table.getSelection();

				if (selection != null && selection.length == 1) {

					// xxx

					TreeItem i = selection[0];
					transfer.setSelection(new StructuredSelection(i));
					event.data = i.getData();

				} else {

				}

			}

			@Override
			public void dragFinished(DragSourceEvent event) {

				if (event.detail == DND.DROP_MOVE) {
					//
				}

			}

		});
		droptarted.setTransfer(new Transfer[] { transfer });

		droptarted.addDropListener(new DropTargetAdapter() {

			public void dragOver(DropTargetEvent event) {

				Tree table = (Tree) source.getControl();

				// http://jexp.ru/index.php/Java_Tutorial/SWT/SWT_Drag_Drop#17._Drag_leaf_items_in_a_tree

				event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
				if (event.item != null) {
					TreeItem item = (TreeItem) event.item;
					Point pt = getShell().getDisplay().map(null, table, event.x, event.y);
					Rectangle bounds = item.getBounds();
					if (pt.y < bounds.y + bounds.height / 3) {
						event.feedback |= DND.FEEDBACK_INSERT_BEFORE;
					} else if (pt.y > bounds.y + 2 * bounds.height / 3) {
						event.feedback |= DND.FEEDBACK_INSERT_AFTER;
					} else {
						event.feedback |= DND.FEEDBACK_SELECT;
					}
				}

			}

			@Override
			public void drop(DropTargetEvent event) {

				if (event.data == null) {
					event.detail = DND.DROP_NONE;
					return;
				}

				StructuredSelection s = (StructuredSelection) event.data;

				Tree tree = (Tree) source.getControl();

				if (event.item == null) {
					// create new element
				} else {

					System.out.println(event.item.toString());

					TreeItem item = (TreeItem) event.item;
					Point pt = source.getDisplay().map(null, tree, event.x, event.y);
					Rectangle bounds = item.getBounds();
					TreeItem parent = item.getParentItem();
					if (parent != null) {
						TreeItem[] items = parent.getItems();
						int index = 0;
						for (int i = 0; i < items.length; i++) {
							if (items[i] == item) {
								index = i;
								break;
							}
						}
						if (pt.y < bounds.y + bounds.height / 3) {

							// oben
							System.out.println("1");

						} else if (pt.y > bounds.y + 2 * bounds.height / 3) {

							// mitte
							// TreeItem newItem = new TreeItem(parent, SWT.NONE,
							// index + 1);
							// newItem.setText(text);

							System.out.println("2");

						} else {

							// Neu
							System.out.println("3");

						}
					} else {
						TreeItem[] items = tree.getItems();
						int index = 0;
						for (int i = 0; i < items.length; i++) {
							if (items[i] == item) {
								index = i;
								break;
							}
						}
						if (pt.y < bounds.y + bounds.height / 3) {

							// oben

							System.out.println("4");

						} else if (pt.y > bounds.y + 2 * bounds.height / 3) {

							// mitte

							System.out.println("5");

						} else {

							// neu
							System.out.println("6");
						}
					}
				}

			}

		});
		
		

		// observe the selection list of a viewer

		




		// create new Context
		

		IViewerObservableValue viewerSelectionObservable = ViewerProperties.singleSelection()
				.observe(_treeViewerTarget);

		// create the observables, which should be bound by the SideEffect

		IObservableValue<String> textModifyObservable = WidgetProperties.text(SWT.DefaultSelection)
				.observe(_swtTextName);

		IObservableValue<String> textModifyObservableFocus = WidgetProperties.text(SWT.FocusOut).observe(_swtTextName);


		// Binding the new Style
		IObservableValue<String> treeViewerValueObserveDetailValue = PojoProperties
				.value((Class) viewerSelectionObservable.getValueType(), "name", String.class)
				.observeDetail(viewerSelectionObservable);
		
		ISideEffect sideEffect =

				ISideEffect.create(

						() -> {
							return "" + treeViewerValueObserveDetailValue.getValue();
						}, _swtTextName::setText);

		
		
		_treeTargetdefinition.addDisposeListener(e -> sideEffect.dispose());

		_treeTargetdefinition.addDisposeListener(e -> viewerSelectionObservable.dispose());

		_treeTargetdefinition.addDisposeListener(e -> treeViewerValueObserveDetailValue.dispose());
		
		viewerSelectionObservable.addChangeListener(e -> _swtTextName.setFocus());

		
	

		

		DataBindingContext ctx = new DataBindingContext();

		ctx.bindValue(textModifyObservable, treeViewerValueObserveDetailValue);

		ctx.bindValue(textModifyObservableFocus, treeViewerValueObserveDetailValue);

		treeViewerValueObserveDetailValue.addChangeListener(e -> _treeViewerTarget.refresh());
		
	
		_treeTargetdefinition.addDisposeListener(e -> ctx.dispose());

		
		parent.addDisposeListener(e -> _treeTargetdefinition.dispose());
	}


	/**
	 * 
	 * Getting the selected Node
	 * 
	 * @return
	 * 
	 */

	private TreeItem getSeletedNode() {

		TreeItem[] selection = _treeViewerTarget.getTree().getSelection();

		if (selection != null && selection.length == 1) {

			TreeItem i = selection[0];

			return i;

		}

		return null;

	}

    
    InfoKategory root;
    
    public void setRoot(InfoKategory root) {
    	
    	this.root = root;
    }
    
    

}
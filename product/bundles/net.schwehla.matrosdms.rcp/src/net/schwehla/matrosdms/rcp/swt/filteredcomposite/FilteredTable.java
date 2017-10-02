package net.schwehla.matrosdms.rcp.swt.filteredcomposite;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;

import net.schwehla.matrosdms.rcp.parts.filter.ItemFilter;

/**
 * A simple control that provides a text widget and a tree viewer. The contents
 * of the text widget are used to drive a PatternFilter that is on the viewer.
 * 
 * @see org.eclipse.ui.dialogs.PatternFilter
 * @since 3.2
 */
public class FilteredTable extends Composite {

	/**
	 * The filter text widget to be used by this tree. This value may be
	 * <code>null</code> if there is no filter widget, or if the controls have
	 * not yet been created.
	 */
    protected Text filterText;
    
	/**
	 * The control representing the clear button for the filter text entry. This
	 * value may be <code>null</code> if no such button exists, or if the
	 * controls have not yet been created.
	 */
    protected ToolBarManager filterToolBar;

	/**
	 * The viewer for the filtered tree. This value should never be <code>null</code>
	 * after the widget creation methods are complete.
	 */
    protected TableViewer tableViewer;

    /**
     * The Composite on which the filter controls are created. This is used to set 
     * the background color of the filter controls to match the surrounding controls.
     */
    protected Composite filterComposite;
    
	/**
	 * The pattern filter for the tree. This value must not be <code>null</code>.
	 */
    private ItemFilter patternFilter;

    /**
     * The text to initially show in the filter text control. 
     */
    protected String initialText = ""; //$NON-NLS-1$
    
    /**
     * The job used to refresh the tree.
     */
    private Job refreshJob;

    /**
     * Whether or not to show the filter controls (text and clear button).
     * The default is to show these controls.  This can be overridden by 
     * providing a setting in the product configuration file.  The setting  
     * to add to not show these controls is:
     * 
     * org.eclipse.ui/SHOW_FILTERED_TEXTS=false
     */
    protected boolean showFilterControls;
    
    /**
     * Image descriptor for enabled clear button.
     */
    private static final String CLEAR_ICON = "org.eclipse.ui.internal.dialogs.CLEAR_ICON"; //$NON-NLS-1$

    /**
     * Image descriptor for disabled clear button.
     */
    private static final String DCLEAR_ICON = "org.eclipse.ui.internal.dialogs.DCLEAR_ICON"; //$NON-NLS-1$

    /**
     * Get image descriptors for the clear button.
     */


    /**
     * Create a new instance of the receiver.
     * 
     * @param parent
     *            parent <code>Composite</code>
     * @param treeStyle
     *            the style bits for the <code>Tree</code>
     * @param filter
     *            the filter to be used
     */
    public FilteredTable(Composite parent, int treeStyle, ItemFilter filter) {
        super(parent, SWT.NONE);
        patternFilter = filter;
        showFilterControls = true;
        createControl(parent, treeStyle);
        createRefreshJob();
        setInitialText("");
		setFont(parent.getFont());
		
		   

	    
    }
    
    
 
    
    
    /**
     * Create the filtered tree's controls.  
     * Subclasses should override.
     * 
     * @param parent
     * @param tableStyle
     */
    protected void createControl(Composite parent, int tableStyle)
    {
    	GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		setLayout(layout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        if (showFilterControls)
        {
        	filterComposite = new Composite(this, SWT.NONE);
            GridLayout filterLayout = new GridLayout(2, false);
            filterLayout.marginHeight = 0;
            filterLayout.marginWidth = 0;
            filterComposite.setLayout(filterLayout);
            filterComposite.setFont(parent.getFont());
            
        	createFilterControls(filterComposite);
        	filterComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
					true, false));
        }
        
        createTableControl(this, tableStyle); 
    }
    
    /**
     * Create the filter controls.  By default, a text and corresponding tool bar 
     * button that clears the contents of the text is created.
     * Subclasses may override.  
     * 
     * @param parent parent <code>Composite</code> of the filter controls
     * @return the <code>Composite</code> that contains the filter controls
     */
    protected Composite createFilterControls(Composite parent){
        createFilterText(parent);
        createClearText(parent);
        
        filterToolBar.update(false);
        // initially there is no text to clear
        filterToolBar.getControl().setVisible(false);
        return parent;
    }
    
    /**
     * Create the tree.  Subclasses may override.
     * 
     * @param parent parent <code>Composite</code>
     * @param style SWT style bits used to create the tree
     * @return the tree
     */
    protected Control createTableControl(Composite parent, int style){
        tableViewer = new TableViewer(parent, style);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        tableViewer.getControl().setLayoutData(data);
        tableViewer.getControl().addDisposeListener(new DisposeListener(){
        	/* (non-Javadoc)
        	 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
        	 */
        	public void widgetDisposed(DisposeEvent e) {
        		refreshJob.cancel();
        	}
        });
        tableViewer.addFilter(patternFilter);  
        return tableViewer.getControl();
    }
    
    /**
     * Return the first item in the tree that matches the filter pattern.
     * 
     * @param items
     * @return the first matching TreeItem
     */
    private TableItem getFirstMatchingItem(TableItem[] items)
    {
    	// why if true !?
    	
		for (int i = 0; i < items.length; i++)
		{
			if (true) 
			{
				return items[i];
			}
		}
		return null;
    }
    
    
	private void createRefreshJob() {
		refreshJob = doCreateRefreshJob();
		refreshJob.setSystem(true);
	}
	
	
	
	
	  /**
     * Create the refresh job for the receiver.
     *
     */
	private Job doCreateRefreshJob() {
		
		return new BasicUIJob("Refresh Filtertree", getDisplay()) {//$NON-NLS-1$
			/* (non-Javadoc)
			 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
			 */
			public IStatus runInUIThread(IProgressMonitor monitor) {
				if(tableViewer.getControl().isDisposed()) {
					return Status.CANCEL_STATUS;
				}
				
				String text = getFilterString();
				if (text == null) {
					return Status.OK_STATUS;
				}
				
		        boolean initial = initialText != null && initialText.equals(text); 
		        if (initial) {
		            patternFilter.setSearchString(null);
		        } else if (text != null){
		            patternFilter.setSearchString(text);
		        }
		        
		        try {
			        // don't want the user to see updates that will be made to the tree
			        tableViewer.getControl().setRedraw(false);
			        tableViewer.refresh(true);
			       
			        if (text.length() > 0 && !initial) 
			        {
//			        	/* Expand elements one at a time.  After each is expanded, check
//			        	 * to see if the filter text has been modified.  If it has, then 
//			        	 * cancel the refresh job so the user doesn't have to endure 
//			        	 * expansion of all the nodes.
//			        	 */
//			        	IStructuredContentProvider provider = (IStructuredContentProvider) tableViewer
//								.getContentProvider();
//						Object[] elements = provider.getElements(tableViewer
//								.getInput());
//						for (int i = 0; i < elements.length; i++) {
//							if (monitor.isCanceled()) {
//								return Status.CANCEL_STATUS;
//							}
//							tableViewer.expandToLevel(elements[i], AbstractTreeViewer.ALL_LEVELS);
//						}
	
			            TableItem[] items = getViewer().getTable().getItems();
			            
			            if (items.length > 0) {
			            	// to prevent scrolling
							tableViewer.getTable().showItem(items[0]);	
						}
			            
			            // Select first element
			            if (items.length == 1) {
			            	tableViewer.setSelection( new StructuredSelection(items[0].getData()));
			            }
			            
			            // enabled toolbar - there is text to clear
			            // and the list is currently being filtered		
			            updateToolbar(true);
			        } else {
						// disabled toolbar - there is no text to clear
				        // and the list is currently not filtered		        	
			        	updateToolbar(true);
			        }
	        	}
	        	finally {
			        // done updating the tree - set redraw back to true
			        tableViewer.getControl().setRedraw(true);
	        	}	
		        return Status.OK_STATUS;
			}
			
		};
		
}
	
	


	protected void updateToolbar(boolean visible){
		if (filterToolBar != null) {
			filterToolBar.getControl().setVisible(visible);
		}
	}
	
	/**
	 * creates the widget instance for the filter text - override this if you want to
	 * apply field-assist controls
	 * 
	 * @param parent
	 * @param style
	 * @param layoutData
	 * 
	 * @return Text
	 */
	protected Text createFilterTextControl(Composite parent, int style, Object layoutData) {
		Text text = new Text(parent,style);
		text.setLayoutData(layoutData);
		return text;
	}
	
	/**
	 * Create the filter text and adds listeners.
	 * 
	 * @param parent <code>Composite</code> of the filter text
	 */
	protected void createFilterText(Composite parent) {
		filterText =  createFilterTextControl(parent, SWT.SINGLE | SWT.BORDER, new GridData(SWT.FILL, SWT.BEGINNING, true, false) );
		filterText.getAccessible().addAccessibleListener(
				new AccessibleAdapter(){
					/* (non-Javadoc)
					 * @see org.eclipse.swt.accessibility.AccessibleListener#getName(org.eclipse.swt.accessibility.AccessibleEvent)
					 */
					public void getName(AccessibleEvent e) {
						String filterTextString = filterText.getText();
						if(filterTextString.length() == 0){
							e.result = initialText;
						} else {
							e.result = filterTextString;
						}
					}
				});

		filterText.addFocusListener(
				new FocusAdapter(){
					/* (non-Javadoc)
					 * @see org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt.events.FocusEvent)
					 */
					public void focusGained(FocusEvent e) {
						/* Running in an asyncExec because the selectAll() does not   
						 * appear to work when using mouse to give focus to text.
						 */
						Display display = filterText.getDisplay();
						display.asyncExec(new Runnable() {
		                    public void run() {
		                    	if (!filterText.isDisposed()){
									if (getInitialText().equals(filterText.getText().trim())){
										filterText.selectAll();
									}
		                    	}
		                    }
						});
					}
				});
		
        filterText.addKeyListener(new KeyAdapter() {
            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
             */
            public void keyPressed(KeyEvent e) {
            	// on a CR we want to transfer focus to the list
            	boolean hasItems = getViewer().getTable().getItemCount() > 0;
            	if(hasItems && e.keyCode == SWT.ARROW_DOWN){
                    	tableViewer.getTable().setFocus();
            	} else if (e.character == SWT.CR){
					return;
            	}
            }
        });
        
        // enter key set focus to tree
        filterText.addTraverseListener(new TraverseListener () {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
					if (getViewer().getTable().getItemCount() == 0) {
						Display.getCurrent().beep();
					} else {
						// if the initial filter text hasn't changed, do not try to match
						boolean hasFocus = getViewer().getTable().setFocus();
						boolean textChanged = !getInitialText().equals(
								filterText.getText().trim());
						if (hasFocus && textChanged
								&& filterText.getText().trim().length() > 0) {
							TableItem item = getFirstMatchingItem(getViewer()
									.getTable().getItems());
							if (item != null) {
								getViewer().getTable().setSelection(
										new TableItem[] { item });
								ISelection sel = getViewer().getSelection();
								getViewer().setSelection(sel, true);
							}
						}						
					} 
				}
			}
		});
        
        filterText.addModifyListener(new ModifyListener(){
        	/* (non-Javadoc)
        	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
        	 */
        	public void modifyText(ModifyEvent e) {
        		textChanged();
        	}
        });
	}

	/**
     * Update the receiver after the text has changed.
     */
    protected void textChanged() {
    	// cancel currently running job first, to prevent unnecessary redraw
    	refreshJob.cancel();
    	refreshJob.schedule(200);
    }

    /**
     * Set the background for the widgets that support the filter text area.
     * 
     * @param background background <code>Color</code> to set
     */
    public void setBackground(Color background) {
        super.setBackground(background);
        if (filterComposite != null) {
			filterComposite.setBackground(background);
		}
        if (filterToolBar != null && filterToolBar.getControl() != null) {
			filterToolBar.getControl().setBackground(background);
		}
    }

    /**
     * Create the button that clears the text.
     * 
     * @param parent parent <code>Composite</code> of toolbar button 
     */
    private void createClearText(Composite parent) {
        ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.HORIZONTAL);
        filterToolBar = new ToolBarManager(toolBar);

        IAction clearTextAction = new Action("", IAction.AS_PUSH_BUTTON) {//$NON-NLS-1$
            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.action.Action#run()
             */
            public void run() {
                clearText();
            }
        };

        clearTextAction
				.setToolTipText("clear");
        clearTextAction.setImageDescriptor(JFaceResources.getImageRegistry()
                .getDescriptor(CLEAR_ICON));
        clearTextAction.setDisabledImageDescriptor(JFaceResources
                .getImageRegistry().getDescriptor(DCLEAR_ICON));

        filterToolBar.add(clearTextAction);
    }

    /**
     * Clears the text in the filter text widget.  Also removes the optional 
     * additional filter that is provided via addFilter(ViewerFilter).
     */
    protected void clearText() {
        setFilterText(""); //$NON-NLS-1$
        textChanged();
    }

    /**
     * Set the text in the filter control.
	 * @param string 
	 */
	protected void setFilterText(String string) {
		if (filterText != null){
			filterText.setText(string);
			selectAll();		
		}
	}

	/**
	 * Returns the pattern filter used by this tree.
	 * 
	 * @return The pattern filter; never <code>null</code>.
	 */
	public final ItemFilter getPatternFilter() {
		return patternFilter;
	}
	
	/**
     * Get the tree viewer of the receiver.
     * 
     * @return the tree viewer
     */
    public TableViewer getViewer() {
        return tableViewer;
    }

    /**
     * Get the filter text for the receiver, if it was created. 
     * Otherwise return <code>null</code>.
     * 
     * @return the filter Text, or null if it was not created
     */
    public Text getFilterControl() {
        return filterText;
    }
    
    /**
     * Convenience method to return the text of the filter control.
     * If the text widget is not created, then null is returned.
     * 
     * @return String in the text, or null if the text does not exist
     */
    protected String getFilterString(){
    	return filterText != null ? filterText.getText() : null;
    }

    /**
     * Set the text that will be shown until the first focus.
     * A default value is provided, so this method only need be 
     * called if overriding the default initial text is desired.
     * 
     * @param text initial text to appear in text field
     */
    public void setInitialText(String text) {
        initialText = text;
    	setFilterText(initialText);
        textChanged();
    }

    /**
     * Select all text in the filter text field.
     *
     */
	protected void selectAll() {
		if (filterText != null) {
			filterText.selectAll();
		}
	}

	/**
	 * Get the initial text for the receiver.
	 * @return String
	 */
	protected String getInitialText() {
		return initialText;
	}

	
	
}
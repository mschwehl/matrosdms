 
package net.schwehla.matrosdms.rcp.parts;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.core.services.translation.TranslationService;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.dnd.DomainClassTransfer;
import net.schwehla.matrosdms.rcp.dnd.MyDragListener;
import net.schwehla.matrosdms.rcp.dnd.MyDropListener;
import net.schwehla.matrosdms.rcp.parts.filter.ItemFilter;
import net.schwehla.matrosdms.rcp.parts.helper.AutoResizeTableLayout;
import net.schwehla.matrosdms.rcp.parts.helper.DesktopHelper;
import net.schwehla.matrosdms.rcp.parts.helper.ItemPartElementWrapper;
import net.schwehla.matrosdms.rcp.parts.helper.ItemPartElementWrapper.Type;
import net.schwehla.matrosdms.rcp.swt.labelprovider.LinkLabelProvider;
import net.schwehla.matrosdms.rcp.swt.labelprovider.LinkOpener;

public class ItemStackPart {
	
    private DateFormat df;
    
	
	private TableViewer swtItemlistTableViewer;
	
    private WritableList input;
    
	private Text swtTxtSerarchfield;
	
	@Inject ItemFilter filter;

	@Inject
	IMatrosServiceService service;

	@Inject
	UISynchronize sync;
	
	@Inject 
	DesktopHelper desktopHelper;
	
	@Inject Logger logger;

	@Inject

	@Translation

	MatrosMessage messages;

    @Inject
    EMenuService menuservice;
	
	@Inject
	 @Named(TranslationService.LOCALE) Locale locale;
	
	@Inject 
	EPartService partService;
	
    @Inject ESelectionService selectionService;
    
	@Inject
	@Optional
	private void subscribeTOPIC_TOPIC__REFRESH_ITEM_ARCHIVE(
			@UIEventTopic(MyEventConstants.TOPIC__REFRESH_ITEM_ARCHIV)  InfoItem infoItem) {
		
		List baseset = (List) swtItemlistTableViewer.getInput();
		
		if (baseset.remove(infoItem)) {
			swtItemlistTableViewer.refresh(false);
		}
		
	}

	
	
	@Inject
	@Optional
	private void subscribeTOPIC_TOPIC__REFRESH_ITEM_ADD(
			@UIEventTopic(MyEventConstants.TOPIC__REFRESH_ITEM_MODIFIED) InfoItem type) {
		
		List baseset = (List) swtItemlistTableViewer.getInput();
		
		if (baseset.contains(type)) {
			baseset.set(baseset.indexOf(type), type);
		}

		
	}
	
	
	
	
	@Inject

	@Optional

	private void subscribeTOPIC_CONTEXT_SELECTED_SELECT(
			@UIEventTopic(MyEventConstants.TOPIC_CONTEXT_SELECTED_SELECT) InfoContext infoContext) {


		if (swtItemlistTableViewer != null && !swtItemlistTableViewer.getTable().isDisposed()) {

			if (infoContext != null) {
				
				swtItemlistTableViewer.refresh(true);
			} 

		}

	}
	
	

	@Inject
	public ItemStackPart() {
		
	}
	

	
	@PostConstruct
	public void postConstruct(Composite parent) {
		
// register-problem...
		
		
//		if (parent.getParent() instanceof CTabFolder) {
//			
//			CTabFolder folder = (CTabFolder) parent.getParent();
//			
//			DropTarget dropTarget = new DropTarget(folder, DND.DROP_MOVE | DND.DROP_LINK | DND.DROP_COPY);
//			final Transfer transfer = DomainClassTransfer.getTransfer(InfoItem.class);
//
//			dropTarget.setTransfer(new Transfer[] { transfer });
//			
//			dropTarget.addDropListener( new DropTargetAdapter() {
//				
//				@Override
//				public void dragEnter(DropTargetEvent event) {
//										  
//		   			  MPart items = partService.findPart("net.schwehla.matrosdms.rcp.part.itemstack");
//		   			  if (items != null) {
//		   					partService.showPart(items, PartState.ACTIVATE);
//		   			  }
//				}
//				
//			});
//			
//			
//		}
//		
		

		df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		 
		parent.setLayout(new GridLayout(1, false));

		swtTxtSerarchfield = new Text(parent, SWT.BORDER);
		swtTxtSerarchfield.setMessage(messages.itemstackpart_searchfield_helptext);
		swtTxtSerarchfield.addModifyListener(new ModifyListener() {

			@Override

			public void modifyText(ModifyEvent e) {

				filter.setSearchString(swtTxtSerarchfield.getText());

				swtItemlistTableViewer.refresh(false);

				if (swtItemlistTableViewer.getTable().getItemCount() == 1) {

					swtItemlistTableViewer.setSelection(
							new StructuredSelection(swtItemlistTableViewer.getTable().getItems()[0].getData()));

				}

			}

		});

		swtTxtSerarchfield.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// SWT.VIRTUAL makes problems with filtering

		swtItemlistTableViewer = new TableViewer(parent, SWT.MULTI | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);

		swtItemlistTableViewer.getTable().addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyPressed(KeyEvent e)
		    {
		        if (e.stateMask == SWT.CTRL && e.keyCode == 'a') {
		        	swtItemlistTableViewer.getTable().selectAll();
		            e.doit = false;
		        }
		    }
		});
		
		
		swtItemlistTableViewer.getTable().addKeyListener(new KeyAdapter() {
			

			
			@Override
			public void keyPressed(KeyEvent e) {
				
				if (e.keyCode == SWT.DEL) {
				
						int[] indizies =  swtItemlistTableViewer.getTable().getSelectionIndices();
						if (indizies.length > 0) {

							List baseset = (List) swtItemlistTableViewer.getInput();
							
							Set o = new HashSet();
							for (int i : indizies) {
								o.add(baseset.get(i));
							}
							
							if (baseset.removeAll(o)) {
								swtItemlistTableViewer.refresh(false);
							}
							
						
						}
				}
				
				super.keyPressed(e);

			}
			
			
		});
	
		// Speedup a little bit

		swtItemlistTableViewer.setUseHashlookup(true);

		swtItemlistTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		swtItemlistTableViewer.setInput(new ArrayList<>());

		AutoResizeTableLayout layout = new AutoResizeTableLayout(swtItemlistTableViewer.getTable());

		createColumns(layout);

		// Stetch the Table itself to maximum

		GridData gridData = new GridData();

		gridData.grabExcessHorizontalSpace = true;

		gridData.grabExcessVerticalSpace = true;

		gridData.horizontalAlignment = GridData.FILL;

		gridData.verticalAlignment = GridData.FILL;

		swtItemlistTableViewer.getTable().setLayoutData(gridData);

		swtItemlistTableViewer.addFilter(filter);

		swtItemlistTableViewer.getTable().addMouseListener(new MouseAdapter() {

			@Override

			public void mouseDoubleClick(MouseEvent e) {

				InfoItem doc = (InfoItem) swtItemlistTableViewer.getTable().getSelection()[0].getData();

				
				ItemPartElementWrapper wrapper = new ItemPartElementWrapper(Type.EXISTING);
				wrapper.setInfoItem(doc);
				
					
				MPart itemPart = partService.createPart("net.schwehla.matrosdms.rcp.partdescriptor.ItemPart");
				// XXX
				itemPart.setIconURI("pdf");
				
				itemPart.setObject(wrapper);
				
				itemPart.setLabel(doc.getName());
				
				// XXX not allow double open

				partService.showPart(itemPart, PartState.ACTIVATE);
			

			}

		});

	       // viewer is a JFace Viewer
		swtItemlistTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
      	  
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                    IStructuredSelection selection = swtItemlistTableViewer.getStructuredSelection();
                    selectionService.setSelection(selection.getFirstElement());
            }
                
        });

		
		Transfer[] transferTypes = { DomainClassTransfer.getTransfer(InfoItem.class)};
		
		swtItemlistTableViewer.addDropSupport( DND.DROP_MOVE | DND.DROP_LINK | DND.DROP_COPY, transferTypes, 
				new MyDropListener(swtItemlistTableViewer, transferTypes) {
			
			@Override
			public void drop(DropTargetEvent event) {
				// TODO Auto-generated method stub
				super.drop(event);
			}
			
			
		});
		
			
		swtItemlistTableViewer.addDragSupport( DND.DROP_MOVE | DND.DROP_LINK | DND.DROP_COPY,transferTypes
				, new MyDragListener(swtItemlistTableViewer,InfoItem.class)   {
			
			@Override
			public void dragStart(DragSourceEvent event) {
				
				// XXX evtl contextlist open
				super.dragStart(event);
			}
			
			
			@Override
			public void dragFinished(DragSourceEvent event) {
				
				
				
				IStructuredSelection se = swtItemlistTableViewer.getStructuredSelection();
				
				Set cloned = new HashSet<>(se.toList());
				((List)swtItemlistTableViewer.getInput() ). removeAll(cloned);
				
				swtItemlistTableViewer.refresh();
			}

			
		});
		
		

      	menuservice.registerContextMenu(swtItemlistTableViewer.getTable(),
    			"net.schwehla.matrosdms.rcp.popupmenu.infoitempopup");
      	
				
	
      	
		
	}
	

  		

	private void createColumns(AutoResizeTableLayout layout) {

		swtItemlistTableViewer.getTable().setLayout(layout);

		swtItemlistTableViewer.getTable().setHeaderVisible(true);

		swtItemlistTableViewer.getTable().setLinesVisible(true);

		TableViewerColumn colIcon = new TableViewerColumn(swtItemlistTableViewer, SWT.NONE);

		colIcon.getColumn().setText(messages.itemstackpart_colIcon);

		colIcon.getColumn().setMoveable(true);

		layout.addColumnData(new ColumnWeightData(15));

		
		
		LinkOpener <InfoItem> linkHandler = new LinkOpener<InfoItem>() {
			@Override
			public void openLink(InfoItem rowObject) {
				
	        	try {
            	 	String local = desktopHelper.getLocallink(rowObject);
                	desktopHelper.openUrl(local);
            	}catch(Exception ex) {
            		logger.error(ex);
            	}
           
			}
		};
		
		colIcon.setLabelProvider(new LinkLabelProvider(new ColumnLabelProvider() {
			
			@Override

			public String getText(Object element) {

				InfoItem item = (InfoItem) element;
				
				return item.getMetadata().getMimetype();

			}
			
		}, linkHandler));
		
		/*
		
		colIcon.setLabelProvider(new ColumnLabelProvider() {

			@Override

			public String getText(Object element) {

				InfoItem item = (InfoItem) element;
				
				return item.getMetadata().getMimetype();

			}
			
		});

		*/
		
		
		TableViewerColumn colContext = new TableViewerColumn(swtItemlistTableViewer, SWT.LEFT);

		colContext.getColumn().setText(messages.contextlistpart_table_col_context);

		colContext.getColumn().setMoveable(true);

		colContext.setLabelProvider(new ColumnLabelProvider() {

			@Override

			public String getText(Object element) {

				InfoItem c = (InfoItem) element;

				return c.getContext().getName();

			}

		});

		layout.addColumnData(new ColumnWeightData(10));
	
		
		
		TableViewerColumn colDate = new TableViewerColumn(swtItemlistTableViewer, SWT.NONE);

		colDate.getColumn().setText(messages.itemstackpart_colDate);

		colDate.getColumn().setMoveable(true);

		layout.addColumnData(new ColumnWeightData(20));

		colDate.setLabelProvider(new ColumnLabelProvider() {

			@Override

			public String getText(Object element) {

				InfoItem item = (InfoItem) element;
				
				return df.format(item.getDateCreated());

			}
			
		});
		
		
		
		TableViewerColumn colType = new TableViewerColumn(swtItemlistTableViewer, SWT.LEFT);

		colType.getColumn().setText(messages.contextlistpart_table_col_type);

		colType.getColumn().setMoveable(true);

		layout.addColumnData(new ColumnWeightData(40));

		colType.setLabelProvider(new OwnerDrawLabelProvider() {

			// http://stackoverflow.com/questions/26463520/eclipse-ownerdrawlabelprovider-makes-selection-background-dark-blue

			@Override

			protected void erase(Event event, Object element)

			{

				// Don't call super to avoid selection draw

				// Elswise the Background-Color is wrong

			}

			@Override

			protected void measure(Event event, Object element) {

				// Element is filtered

				if (element == null) {

					return;

				}

				event.width = swtItemlistTableViewer.getTable().getColumn(event.index).getWidth();

				if (event.width == 0) {

					return;

				}

				InfoItem entry = (InfoItem) element;

				StringBuffer sb = new StringBuffer();

				buildContent(entry, sb);
				
				Point size = event.gc.textExtent(sb.toString());

				event.height = size.y;

			}

			@Override

			protected void paint(Event event, Object element) {

				// Element is filtered

				if (element == null) {

					return;

				}

				InfoItem entry = (InfoItem) element;

				StringBuffer sb = new StringBuffer();
				
				
				buildContent(entry, sb);
				
				


				event.gc.drawText(sb.toString(), event.x, event.y, true);

			}

			private void buildContent(InfoItem entry, StringBuffer sb) {
				Iterator<InfoKategory> itwas =  entry.getTypList().iterator();
				
				while (itwas.hasNext()) {
					sb.append(itwas.next().getName());
					if (itwas.hasNext()) {
						sb.append("+");
					}
				}
				
				sb.append("\n");
			}
		}

		);

		TableViewerColumn colName = new TableViewerColumn(swtItemlistTableViewer, SWT.NONE);

		colName.getColumn().setText(messages.contextlistpart_table_col_name);

		colName.getColumn().setMoveable(true);

		layout.addColumnData(new ColumnWeightData(30));

		colName.setLabelProvider(new ColumnLabelProvider() {

			@Override

			public String getText(Object element) {

				InfoItem c = (InfoItem) element;
				return  c.getName(); 
			}

		});



	}
	
	
}
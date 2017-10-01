package net.schwehla.matrosdms.rcp.swt.composite;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;

import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategoryList;
import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.dnd.DomainClassTransfer;
import net.schwehla.matrosdms.rcp.dnd.MyDropListener;

@Creatable
public class MatrosTagGroup extends Composite {

	
	// http://www.vogella.com/tutorials/SWTCustomWidget/article.html
	
	private static final int LINE_WIDTH = 100;
    private static final int LINE_HEIGHT = 80;

	
	Consumer consumer;
	
	MyDropListener myDropListener;
	
	public void addChangelistener(Consumer consumer) {
		this.consumer = consumer;
		
		if (myDropListener != null) {
			myDropListener.addChangelistener(consumer);
		}

	}
    
	
	
	TableViewer viewer;

	public TableViewer getViewer() {
		return this.viewer;
	}
	
	public static MatrosTagGroup buildTagGroupViaEclipseContext(IEclipseContext context, Composite parent , InfoKategoryList collection) {
		IEclipseContext tempContext = EclipseContextFactory.create();
        
        // Composite parent, String text,  List<InfoKategory> supplier 
        
        tempContext.set(Composite.class, parent);
        tempContext.set(InfoKategoryList.class, collection);
        
        
        MatrosTagGroup group = (MatrosTagGroup) ContextInjectionFactory.make(MatrosTagGroup.class, context, tempContext);
        
		GridData gd__swtGrpType = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd__swtGrpType.widthHint = LINE_WIDTH;
		gd__swtGrpType.heightHint = LINE_HEIGHT;
		group.setLayoutData(gd__swtGrpType);


        

        /*
         
		try {
			
			// Array because of Plattform-Independence
		    GridData gd__swtTableType = new GridData();
		    gd__swtTableType.horizontalAlignment = SWT.CENTER;
		    gd__swtTableType.widthHint  = who.getViewer().getTable().getFont().getFontData()[0].getHeight() * 10 + 3;
		    gd__swtTableType.heightHint = who.getViewer().getTable().getFont().getFontData()[0].getHeight() * 4 + 5;
		    who.setLayoutData(gd__swtTableType);

		} catch (Exception e) {
			e.printStackTrace();
		}

		*/
			
        return group;
	}

	

	

    	
	// Default-Supplier for the gui-builder
    InfoKategoryList  kategoryList ;

	@Inject
	MApplication application;
	
	@Inject
	private IEventBroker eventBroker;
	
	@Inject
	@Optional
	private void subscribeTopicInboxDragStart(
			@UIEventTopic(MyEventConstants.TOPIC_DROPFIELD_KATEGORY_ACTIVATED) Identifier givenType) {
		
		if (this.isDisposed()) {
			return;
		}
	
		if (kategoryList.getIdentifier().equals(givenType)) {
			setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		} else {
			clearDropBackground();
		}
			
	}
	
	
	@Inject
	public MatrosTagGroup(Composite parent,  InfoKategoryList kategoryList ) {
		super(parent, SWT.NONE);
	
		this.kategoryList = kategoryList;
		
		setLayout(new GridLayout(1, false));
		
		Group g = new Group(this, SWT.NONE);
		
		// XXX LOCALIZATION
		g.setText(kategoryList.getName());
		g.setLayout(new GridLayout(1, false));
		g.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				
		viewer = new TableViewer(g, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI );
		
		buildComponent();
		
	}


	private void buildComponent() {
	
		Table table = viewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		viewer.setContentProvider( ArrayContentProvider.getInstance() );

		
		viewer.setLabelProvider( new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				InfoKategory k = (InfoKategory) element;
				return k.getName();
			}
		});
		
		// GUI-Builder cannot resolve supplier :-(
		if (kategoryList != null) {
		 	viewer.setInput(kategoryList);
		}
		
// MOUSE -> FOKUS
		
		viewer.getTable().getParent().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				super.mouseDown(e);
				
				setFocus();
				
				eventBroker.send(MyEventConstants.TOPIC_DROPFIELD_KATEGORY_ACTIVATED, kategoryList.getIdentifier());
				
				
			}
		});
		
		viewer.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				super.mouseDown(e);
				
				setFocus();
				
				eventBroker.send(MyEventConstants.TOPIC_DROPFIELD_KATEGORY_ACTIVATED, kategoryList.getIdentifier());
				
				
			}
		});
		
		
		
// DND		
		
		Transfer[] transferTypes = { DomainClassTransfer.getTransfer(InfoKategory.class)};
		
		myDropListener = new MyDropListener(viewer, transferTypes);
		
		
		viewer.addDropSupport( DND.DROP_MOVE | DND.DROP_LINK | DND.DROP_COPY, transferTypes, 
				myDropListener );
		
// KEYLISTENER
		
		viewer.getTable().addKeyListener(new KeyAdapter() {
			

			
			@Override
			public void keyPressed(KeyEvent e) {
				
				if (e.keyCode == SWT.DEL) {
				
						int[] indizies =  viewer.getTable().getSelectionIndices();
						
						if (indizies.length > 0) {
							
							Set o = new HashSet();
							for (int i : indizies) {
								o.add(kategoryList.get(i));
							}
							
							boolean changed = kategoryList.removeAll(o);
							
							if (changed && consumer != null) {
								consumer.accept(null);
							}
							
							viewer.refresh();
						}
				}
				
				super.keyPressed(e);

			}
			
			
		});
		
		
		
	}
	
	
    public void clearDropBackground() {
    	setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
	}


	@Override
    public Point computeSize(int wHint, int hHint, boolean changed) {

            // try to consider the given hints. Here we decided to use the smallest
            // value so that the line would not be bigger than 30x2.
            // In case the SWT.DEFAULT flag for the hints is used, we simply stick to
            // the LINE_WIDTH and LINE_HEIGHT.
            int width = wHint != SWT.DEFAULT ? Math.max(wHint, LINE_WIDTH) : LINE_WIDTH;
            int height = hHint != SWT.DEFAULT ? Math.max(hHint, LINE_HEIGHT) : LINE_HEIGHT;

            return new Point(width, height);
    }

	public void refresh() {
		getViewer().refresh();
		
	}
	
	public void bindViewToModel() {
		
		
	
	}
    
}

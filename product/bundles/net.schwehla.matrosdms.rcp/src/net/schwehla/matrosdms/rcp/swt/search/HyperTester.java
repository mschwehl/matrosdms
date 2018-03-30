package net.schwehla.matrosdms.rcp.swt.search;


import java.util.ArrayList;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.domain.core.InfoBaseElement;
import net.schwehla.matrosdms.domain.core.attribute.InfoBooleanAttribute;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.rcp.dnd.DomainClassTransfer;

public class HyperTester {

  public static void main(String[] a) {
    
    final Display d = new Display();
    final Shell shell = new Shell(d);
    shell.setSize(521, 217);
    shell.setLayout(new GridLayout(1, false));
    
    Composite compositeTop = new Composite(shell, SWT.NONE);
    compositeTop.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    
    SearchItemEditor swtQueryWidget = new SearchItemEditor(compositeTop);
    swtQueryWidget.setSize(500, 100);
	swtQueryWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    
    
    Composite compositeBottom = new Composite(shell, SWT.NONE);
    compositeBottom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    
    ListViewer listViewer = new ListViewer(compositeBottom, SWT.BORDER | SWT.V_SCROLL);
    List list_1 = listViewer.getList();
    list_1.setSize(200, 50);
    
    Button btnNewButton = new Button(compositeBottom, SWT.NONE);
    btnNewButton.addSelectionListener(new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
    		
    		System.out.println(swtQueryWidget.getSearchItemInput());
    		
    		
    		
    	}
    });
    btnNewButton.setBounds(410, 25, 75, 25);
    btnNewButton.setText("New Button");
    

    

         
    
    
    Label lblNewLabel = new Label(compositeBottom, SWT.NONE);
    lblNewLabel.setBounds(216, 4, 55, 15);
    lblNewLabel.setText("New Label");
    
    Button btnWas = new Button(compositeBottom, SWT.NONE);
    
    MatrosPopup w = new MatrosPopup(btnWas);
    
    
    btnWas.addSelectionListener(new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
    		
    		
    		
    	}
    });
    btnWas.setBounds(304, 25, 75, 25);
    btnWas.setText("Was");
   
    listViewer.setContentProvider( new ArrayContentProvider());
 
    
    InfoKategory k = new InfoKategory(Identifier.createNEW(), "DUMMY");
    InfoKategory k2 = new InfoKategory(Identifier.createNEW(), "DUMMY");
    
    InfoKategory root = new InfoKategory(Identifier.create(1L, "ROOT_WER"), "root");
    

    InfoBooleanAttribute boolElement = new InfoBooleanAttribute(Identifier.createNEW(), "Steuer");
    
    root.connectWithChild(k);
    root.connectWithChild(k2);

    java.util.List listArry = new ArrayList();
    listArry.add(k);
    listArry.add(k2);
    listArry.add(k2);
    
    listViewer.setInput(listArry);
    
    
    
    
    
    DragSource source = new DragSource(listViewer.getControl(), DND.DROP_MOVE | DND.DROP_COPY);
    source.setTransfer( DomainClassTransfer.getTransfer(InfoBaseElement.class) );

    source.addDragListener(new DragSourceAdapter()
    {
        @Override
        public void dragSetData(DragSourceEvent event)
        {
            // Get the selected items in the drag source
            DragSource ds = (DragSource) event.widget;
            
            java.util.List last = new ArrayList();
            last.add(k);;
            last.add(boolElement);;
            
            
            
            
            event.data = last; //new ArrayList(listArry.get(0));
        }
    });
    


    
    

	Realm.runWithDefault(SWTObservables.getRealm(d), new Runnable() {
		public void run() {
		
			shell.open();
			while (!shell.isDisposed()) {
			      if (!d.readAndDispatch())
			          d.sleep();
			}
		}
	});
	d.dispose();
	
	

   /*
    shell.open();
    while (!shell.isDisposed()) {
      if (!d.readAndDispatch())
        d.sleep();
    }
    d.dispose();
	*/
	
  }
}
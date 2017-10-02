 
package net.schwehla.matrosdms.rcp.parts;

import java.util.Comparator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.search.SearchItemInput;
import net.schwehla.matrosdms.domain.search.SearchedInfoItemElement;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.controller.SerachResultListController;
import net.schwehla.matrosdms.rcp.parts.filter.SearchItemFilter;
import net.schwehla.matrosdms.rcp.parts.helper.AutoResizeTableLayout;
import net.schwehla.matrosdms.rcp.parts.helper.DesktopHelper;
import net.schwehla.matrosdms.rcp.swt.filteredcomposite.FilteredTable;
import net.schwehla.matrosdms.rcp.swt.labelprovider.LinkLabelProvider;
import net.schwehla.matrosdms.rcp.swt.labelprovider.LinkOpener;

public class SearchResultPart {
	
    private Text 		swtTxtSerarchfield;
    private TableViewer swtTableViewer;
    
    
    @Inject
    private IMatrosServiceService service;
	
	@Inject
	@Translation
	MatrosMessage messages;
	
	@Inject 
	Logger logger;
	
    @Inject ESelectionService selectionService;

    @Inject
    SerachResultListController listController;
    
	@Inject 
	DesktopHelper desktopHelper;
    
	@Inject
	@Optional
	private void subscribeTOPIC_REFRESH_CONTEXTLIST(@UIEventTopic(MyEventConstants.TOPIC_REFRESH_SEARCHRESULT) SearchItemInput type) {

	
		swtTableViewer.refresh(true);
		
		// XXX selectFirst();
		
	//	swtContextListTableViewer.getTable().setFocus();
		
	}
	
	
	
	@Inject
	public SearchResultPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		
        parent.setLayout(new GridLayout(1, false));
        
		
        FilteredTable ft = new FilteredTable(parent, SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.VIRTUAL, new SearchItemFilter());

        
        swtTableViewer = ft.getViewer();
        swtTableViewer.setUseHashlookup(true);
        
        swtTableViewer.setContentProvider(ArrayContentProvider.getInstance());
        
        AutoResizeTableLayout layout = new AutoResizeTableLayout(swtTableViewer.getTable());
        createColumns(layout);

	    swtTableViewer.setInput(listController.getList());
        swtTableViewer.refresh(true);
        
	}

	private void createColumns(AutoResizeTableLayout layout) {
		
		swtTableViewer.getTable().setLayout(layout);
		swtTableViewer.getTable().setHeaderVisible(true);
		swtTableViewer.getTable().setLinesVisible(true);
		
		
	          
	     
	    TableViewerColumn colName = new TableViewerColumn(swtTableViewer, SWT.LEFT);
	    colName.getColumn().setText(messages.searchlistpart_col_contextname);
	    layout.addColumnData(new ColumnWeightData(100));
	    
	    
	    colName.setLabelProvider(new ColumnLabelProvider () {
            
            @Override
            public String getText(Object element) {
            	SearchedInfoItemElement c = (SearchedInfoItemElement) element;
            	return c.getContextName();  
            }
            
	    });    
        

	    
	    TableViewerColumn colItemName = new TableViewerColumn(swtTableViewer, SWT.LEFT);
	    colItemName.getColumn().setText(messages.searchlistpart_col_itemname);
	    layout.addColumnData(new ColumnWeightData(100));
	    
	    
	    
		
		LinkOpener <SearchedInfoItemElement> linkHandler = new LinkOpener<SearchedInfoItemElement>() {
			@Override
			public void openLink(SearchedInfoItemElement rowObject) {
				
	        	try {
	        		
            	 	String local = desktopHelper.getLocallink(rowObject);
                	desktopHelper.openUrl(local);
            	}catch(Exception ex) {
            		logger.error(ex);
            	}
           
			}
		};
		

		colItemName.setLabelProvider(new LinkLabelProvider(new ColumnLabelProvider() {
			
			@Override

			public String getText(Object element) {

				SearchedInfoItemElement item = (SearchedInfoItemElement) element;
				
				return item.getName();

			}
			
		}, linkHandler));
		
	    

	    
	    
	    TableViewerColumn colArchivedName = new TableViewerColumn(swtTableViewer, SWT.LEFT);
	    colArchivedName.getColumn().setText(messages.searchlistpart_col_archived);
	    layout.addColumnData(new ColumnWeightData(20));
	    
	    
	    colArchivedName.setLabelProvider(new ColumnLabelProvider () {
            
            @Override
            public String getText(Object element) {

            	SearchedInfoItemElement c = (SearchedInfoItemElement) element;
            	
            	if (c.isEffectiveArchived()) {
            		return "true";
            	}
            	
            	return "false";  
            }
            
	    });    


		
	}
	
	
	
	
}
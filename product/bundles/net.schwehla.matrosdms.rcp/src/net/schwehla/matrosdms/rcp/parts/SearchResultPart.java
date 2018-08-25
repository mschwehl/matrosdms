 
package net.schwehla.matrosdms.rcp.parts;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.core.services.translation.TranslationService;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.domain.search.SearchItemInput;
import net.schwehla.matrosdms.domain.search.SearchedInfoItemElement;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.controller.SerachResultListController;
import net.schwehla.matrosdms.rcp.parts.filter.SearchItemFilter;
import net.schwehla.matrosdms.rcp.parts.helper.AutoResizeTableLayout;
import net.schwehla.matrosdms.rcp.parts.helper.DesktopHelper;
import net.schwehla.matrosdms.rcp.parts.helper.MatrosTableBuilder;
import net.schwehla.matrosdms.rcp.swt.filteredcomposite.FilteredTable;
import net.schwehla.matrosdms.rcp.swt.labelprovider.LinkLabelProvider;
import net.schwehla.matrosdms.rcp.swt.labelprovider.LinkOpener;

public class SearchResultPart {
	

    private TableViewer swtTableViewer;
    
	@Inject
	@Named(TranslationService.LOCALE) Locale locale;

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
    
	
    DateFormat df;
    
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
		
		df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		
		
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
		
		
		
		
	      MatrosTableBuilder <SearchedInfoItemElement> builder = new MatrosTableBuilder<SearchedInfoItemElement>(swtTableViewer);
			
			
			
	    	builder.makeColumn(messages.searchlistpart_col_issuedate).setFunction(e -> {
	    		
	    		if (e.getIssueDate() != null) {
					return df.format(e.getIssueDate());
				} else {
					return "" ; // df.format(item.getDateCreated());
				}
	    		
	    		
	    		
	    	}).append(20);
	    	
	    	
		
		
	        TableViewerColumn colType = builder.makeColumn(messages.searchlistpart_col_contextname).setFunction(
	        		e -> {
	        			return e.getContextName();  
	        		} ).append(25);

	          
	     
	    
	    TableViewerColumn colItemName = new TableViewerColumn(swtTableViewer, SWT.LEFT);
	    colItemName.getColumn().setText(messages.searchlistpart_col_itemname);
	    layout.addColumnData(new ColumnWeightData(100));
	    
	    
	    
		
		LinkOpener <SearchedInfoItemElement> linkHandler = new LinkOpener<SearchedInfoItemElement>() {
			@Override
			public void openLink(SearchedInfoItemElement rowObject) {
				
	        	try {
	        		
	        		// load metadat
	        		
	        		InfoItem item = service.loadInfoItemByIdentifier(rowObject.getIdentifier());
	        		
            	 	String local = desktopHelper.getLocallink(item);
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
		
		

        TableViewerColumn colStore = builder.makeColumn(messages.searchlistpart_col_archived).setFunction(
        		e -> {
        		
        		
    				
    				return  MessageFormat.format("{0}->{1}", e.getStoreIdentifier().getPk(), e.getStoreItemNumber());
    				
                	
        		} ).append(10);
        
	    
        TableViewerColumn colArchivedName = builder.makeColumn(messages.searchlistpart_col_archived).setFunction(
        		e -> {
        			
        			if (e.isEffectiveArchived()) {
                		return "true";
                	}
                	
                	return "false";  
                	
        		} ).append(25);

        
        
        builder.addSorter();
        
		
        
	    
		
	}
	
	
	
	
}
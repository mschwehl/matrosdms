package net.schwehla.matrosdms.rcp.parts.helper;

import java.text.DateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.function.Function;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class MatrosTableBuilder<X> {
	
	private static final String DATA_COMPARATOR = "comparator";
	
	TableViewer viewer;
	Table table;
	AutoResizeTableLayout layout;
	
	public MatrosTableBuilder(TableViewer viewer) {
		this.viewer = viewer;
		this.table = (Table) viewer.getControl();

	    table.setHeaderVisible(true);
  	    layout = new AutoResizeTableLayout(table);
                  
	}
	

	public ColumnBuilder<X> makeColumn(String name) {
		ColumnBuilder <X>x = new ColumnBuilder <X>();
		x.setColumnName(name);
		return x;
	}
	
	


	
	public void addSorter() {
		

	    viewer.setComparator( new ViewerComparator() {
	    	
	    	@Override
	    	public int compare(Viewer viewer, Object e1, Object e2) {

	            TableColumn sortColumn = ((Table)viewer.getControl()).getSortColumn();
	            Comparator comparator = sortColumn == null ? null : (Comparator) sortColumn.getData(DATA_COMPARATOR);
	            if (comparator != null && ((Table)viewer.getControl()).getSortDirection() == SWT.UP) {
	                comparator = comparator.reversed();
	            }
	            
	            if (e1 == null) {
	            	return 1;
	            }
	            if (e2 == null) {
	            	return -1;
	            }
	            
	            return comparator == null ? 0 : comparator.compare(e1, e2);
	            
	    	}
	    	
	    	
	    });
	    

	    for (TableColumn column : ((Table)viewer.getControl()).getColumns()) {
	        column.addListener(SWT.Selection, e -> {
	            final Item sortColumn = ((Table)viewer.getControl()).getSortColumn();
	            int direction = ((Table)viewer.getControl()).getSortDirection();

	            if (column.equals(sortColumn)) {
	                direction = direction == SWT.UP ? SWT.DOWN : SWT.UP;
	            } else {
	            	((Table)viewer.getControl()).setSortColumn(column);
	                direction = SWT.UP;
	            }
	            ((Table)viewer.getControl()).setSortDirection(direction);
	            viewer.refresh();
	        });
	    }
	    
	    
	    
		
	}

	public class ColumnBuilder <X> {
		
		String colName;
		Function<X, Comparable> function;
		int weight;

		public ColumnBuilder<X> setFunction( Function<X, Comparable> f) {
			this.function = f;
			return this;
		}



		public void setColumnName(String name) {
			colName = name;
			
			
		}


		  private DateFormat df =  DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
			 
			

		public  TableViewerColumn append(int weight) {
			
		    TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		    viewerColumn.setLabelProvider(new ColumnLabelProvider() {
		        @Override
		        public String getText(Object element) {
		        	
		        	if (function != null && element != null) {
		        		
		        	    Object o = function.apply((X) element);
		        	    
		        	    if (o == null) {
		        	    	return "";
		        	    }
		        	    
		        	    if (o instanceof Date) {
		        	    	
		        	    	return df.format(o);
		        	    	
		        	    }
		        		
			            return "" + o ;
		        	}
		        	
		        	
		        	return "<empty>";

		        }
		    });

		    TableColumn column = viewerColumn.getColumn();
		    column.setText(colName);
		    column.setWidth(100);
		    column.setData(DATA_COMPARATOR, (Comparator) (v1, v2) -> {
		    
		    	Comparable oeins = function.apply( (X) v1);
		    	Comparable oZwei = function.apply( (X) v2);
		    	
		    	if (oeins == null) {
		    		return -1;
		    	}
		    	if (oZwei == null) {
		    		return 1;
		    	}
		    	
		    	return oeins.compareTo(oZwei);
		    	
		    
		    	
		    });
		    
		    column.setResizable(true);
		    column.setMoveable(true);
	   
	        layout.addColumnData(new ColumnWeightData(weight));          
	   
		    return viewerColumn;
		}
		
		
	}
	

}

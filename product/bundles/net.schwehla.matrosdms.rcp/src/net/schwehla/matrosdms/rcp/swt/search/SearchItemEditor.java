package net.schwehla.matrosdms.rcp.swt.search;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import net.schwehla.matrosdms.domain.core.InfoBaseElement;
import net.schwehla.matrosdms.domain.core.attribute.AttributeType;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.domain.search.SearchItemInput;
import net.schwehla.matrosdms.domain.search.parameter.EMatrosQueryFeature;
import net.schwehla.matrosdms.domain.search.parameter.EMatrosQueryType;
import net.schwehla.matrosdms.domain.search.parameter.MatrosQueryParameter;
import net.schwehla.matrosdms.rcp.dialog.SearchConstraintDialog;
import net.schwehla.matrosdms.rcp.dnd.DomainClassTransfer;

// http://www.vogella.com/tutorials/SWTCustomWidget/article.html
public class SearchItemEditor extends Composite {

	// https://www.regextester.com/15
	static String placeholder = "\uFFFC";
	private static final Pattern DELIMETERS = Pattern.compile(placeholder + "|" + "((?!" + placeholder +")\\S)*");

	
	
	StyledText styledText;
	Font font;

	int MARGIN = 2;

	public SearchItemEditor(Composite parent) {
		super(parent, SWT.NULL);
		buildControls();
	}

	private void buildControls() {

		setLayout(new FillLayout());

		styledText = new StyledText(this, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		styledText.setWordWrap(true);
		
		
		FontData fontData = styledText.getFont().getFontData()[0];
		font = new Font(Display.getCurrent(), new FontData(fontData.getName(), fontData
		     .getHeight(), SWT.BOLD));
		 
		
		addListener(SWT.Dispose, e -> {
			font.dispose();
			styledText.dispose();
			
		} );
		
		
		 styledText.addVerifyListener(new VerifyListener() {
		      public void verifyText(VerifyEvent e) {
		        int start = e.start;
		        int replaceCharCount = e.end - e.start;
		        int newCharCount = e.text.length();
		        
		        StyleRange[] range = styledText.getStyleRanges(0, styledText.getText().length());
		        
		        if (range.length ==0 ) {
		        	return;
		        }
		        
		        int[] offsets = new int[range.length];
		        
		        for (int i=0; i < offsets.length; i++) {
		        	offsets[i] = range[i].start;
		        }
		        
		        for (int i = 0; i < offsets.length; i++) {
		          int offset = offsets[i];
		          if (start <= offset && offset < start + replaceCharCount) {
		            // this widget is being deleted from the text
		        	  Control c = ((MatrosControlRangeStlye)range[i]).control;
		        	  if (!c.isDisposed()) {
		        		  c.dispose();
		        	  }
		        	  offset = -1;
		          }
		          if (offset != -1 && offset >= start)
		            offset += newCharCount - replaceCharCount;
		          offsets[i] = offset;
		        }
		      }
		    });
		 
	
	    // reposition widgets on paint event
	    styledText.addPaintObjectListener(new PaintObjectListener() {
	      public void paintObject(PaintObjectEvent event) {
	        StyleRange style = event.style;
	        
	        if (style instanceof MatrosControlRangeStlye ) {
	        	
	        	Control c = ((MatrosControlRangeStlye) style).getControl();
	        	
	        	if (c.isDisposed()) {
	        		// todo: logging
	        		
	        	} else {
	        		
		        	Point pt = ((MatrosControlRangeStlye) style).getControl().getSize();
	           	    int x = event.x + MARGIN;
		            int y = event.y + event.ascent - 2 * pt.y / 3;
		            c.setLocation(x, y);
		            
	        	}
	        		        	
	        }

	      }
	    });
	    
	    
		Transfer[] transferTypes = {  DomainClassTransfer.getTransfer(InfoBaseElement.class), DomainClassTransfer.getTransfer(InfoKategory.class)};
		
		
		DropTarget target = new DropTarget(styledText,
				DND.DROP_DEFAULT | DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
		
		
		target.setTransfer(transferTypes);

		target.addDropListener(new DropTargetAdapter() {
			@Override
			public void dragEnter(DropTargetEvent e) {
				if (e.detail == DND.DROP_DEFAULT)
					e.detail = DND.DROP_COPY;
			}

			@Override
			public void dragOperationChanged(DropTargetEvent e) {
				if (e.detail == DND.DROP_DEFAULT)
					e.detail = DND.DROP_COPY;
			}

			@Override
			public void drop(DropTargetEvent e) {

				if (e.data instanceof List) {

					List.class.cast(e.data).stream().filter( i -> {return i instanceof  InfoBaseElement; } ).forEach(x -> {
						handleDropElement((InfoBaseElement) x);
					});
				}
				if (e.data instanceof InfoBaseElement) {
					handleDropElement((InfoBaseElement) e.data);
				}

			}

			private void handleDropElement(InfoBaseElement baseElement) {
				
			    StyleRange[] oldStyleRange = styledText.getStyleRanges(0, styledText.getText() .length());
			    

				if (baseElement instanceof InfoKategory) {
			
					InfoKategory infoKategory = InfoKategory.class.cast(baseElement);
					ElementWithConstraint ewc = new ElementWithConstraint(infoKategory) {
						
						@Override
						public MatrosQueryParameter toParameter() {
							
					    	  
					    	  Button label  = (Button) control;
					    	  
					    	  MatrosQueryParameter mqp = new MatrosQueryParameter();
					    	  mqp.setType(EMatrosQueryType.INFO_KATEGORY);
					    	  
					    	  if (label.getSelection()) {
					    		  mqp.getFeature().add(EMatrosQueryFeature.Recursive);
					    	  }

					    	  mqp.setIdentifier( element.getIdentifier() );
					    	  mqp.setConstraint(constraint);
					    	  
					    	  return mqp;
					    	  
						}
						
						public void initControl() {
							
							
							Button label = new Button(styledText, SWT.CHECK); //new Label(styledText, SWT.NONE);
							label.setFont(font);
							label.setText(getLabel());
					    	  label.addMouseListener(new MouseListener()
					    		{
					    			public void mouseDown(MouseEvent e)
					    			{
					    	//		System.out.println("Mouse Down.");
					    			}
					    			public void mouseUp(MouseEvent e)
					    			{
					    	//			System.out.println("Mouse Up.");
					    			}
					    			public void mouseDoubleClick(MouseEvent e)
					    			{
					    	//			System.out.println("Mouse Double click.");
					    			}
					    		
					    		});
					    	  
					    	  setControl(label);
					    	 
							
						};
						
					};
					
					
	
					
					
					
					

					appendElement(ewc, oldStyleRange);

				} 
				
				if (baseElement instanceof AttributeType) {
					
					AttributeType infoAttribute = (AttributeType) baseElement;
					
					ElementWithConstraint ewc = new ElementWithConstraint(infoAttribute) {
						
						public MatrosQueryParameter toParameter() {
							
							 
					    	  Label label  = (Label) control;
					    	  
					    	  MatrosQueryParameter mqp = new MatrosQueryParameter();
					    	  mqp.setType(EMatrosQueryType.ATTRIBUTE);
					    	  
					   

					    	  mqp.setIdentifier( element.getIdentifier() );
					    	  mqp.setConstraint(constraint);
	
					    	  return mqp;
					    	  
					    	  
							
						}
						
							public void initControl() {
							
							
							Label label = new Label(styledText, SWT.BOLD); //new Label(styledText, SWT.NONE);
							label.setFont(font);
							label.setText(getLabel());
					    	  label.addMouseListener(new MouseListener()
					    		{
					    			public void mouseDown(MouseEvent e)
					    			{
					    	//		System.out.println("Mouse Down.");
					    			}
					    			public void mouseUp(MouseEvent e)
					    			{
					    	//			System.out.println("Mouse Up.");
					    			}
					    			public void mouseDoubleClick(MouseEvent e)
					    			{
					    				
					    				SearchConstraintDialog dialog = new SearchConstraintDialog(Display.getCurrent().getActiveShell(), infoAttribute );
									       
									    dialog.create();
									    
									    dialog.getText_constraint().setText( getConstraint());
										  
									     // get the new values from the dialog
						                if (dialog.open() == Window.OK) {
						                	String constraitTxt = dialog.getConstraitStr();
						                	setConstraint(constraitTxt);
						                }
						                
						                doUpdate();
						                
					    				
					    			}
							
					    		
					    		});
					    	  
					    	  setControl(label);
					    	 
							
						};
						
						
						
						
					};
					
					SearchConstraintDialog dialog = new SearchConstraintDialog(Display.getCurrent().getActiveShell(), infoAttribute );
				       
				    dialog.create();
					  
				     // get the new values from the dialog
	                if (dialog.open() == Window.OK) {
	                	String constraitTxt = dialog.getConstraitStr();
	                	ewc.setConstraint(constraitTxt);
	                }
					appendElement(ewc, oldStyleRange);
					
					
					
				}
				
				

			}

			private void appendElement(ElementWithConstraint baseElement, StyleRange[] oldStyleRange ) {
				String newText = styledText.getText() + placeholder ;
				
				styledText.setSelection( newText.length()-1);
				styledText.insert(placeholder);
				
				
//					styledText.setSelection(newText.length()) ;
				styledText.setCaretOffset(newText.length());
//					styledText.setSelection(styledText.getCharCount()) ;
				
				
				
				MatrosControlRangeStlye style = new MatrosControlRangeStlye(baseElement.getControl(), baseElement);
				style.start = newText.length() -1;
				style.length = 1;
				baseElement.getControl().pack();
				Rectangle rect = baseElement.getControl().getBounds();
				int ascent = 2 * rect.height / 3;
				int descent = rect.height - ascent;
				style.metrics = new GlyphMetrics(ascent + MARGIN, descent + MARGIN, rect.width + 2 * MARGIN);

				MatrosControlRangeStlye[] newOne = Stream.concat(Arrays.stream(oldStyleRange), Stream.of(style))
						.toArray(MatrosControlRangeStlye[]::new);

				styledText.setStyleRanges(newOne);
				
				styledText.forceFocus();
			}

		});


		
		
	}
	
	
	static class MatrosControlRangeStlye extends StyleRange {
		
		Control control;
		Object data;
		public MatrosControlRangeStlye(Control control, Object data) {
			this.control = control;
			this.data = data;
		}
		public Control getControl() {
			return control;
		}
		public void setControl(Control control) {
			this.control = control;
		}
		
		public Object getData() {
			return data;
		}
		
		
	}
		

	
	

	public SearchItemInput getSearchItemInput() {
		
		SearchItemInput result = new SearchItemInput();
		
		String text = styledText.getText();
		
		if (text == null || text.trim().length() == 0) {
			return result;
		}
		
		StringBuffer sb = new StringBuffer("");
		
		Matcher matcher = DELIMETERS.matcher(text);
		
		while (matcher.find()) {
			

			String txt = matcher.group();
			
			if (txt.trim().length() ==0) {
				continue;
			}
	
			
			
				if (placeholder.equals(txt)) {
					
				       StyleRange[] range = styledText.getStyleRanges(matcher.start()
				    		   , txt.length());
				       
				       MatrosControlRangeStlye matros = (MatrosControlRangeStlye) range[0];
				       
				      if (matros.getData() instanceof ElementWithConstraint) {
				    	  
				    	  ElementWithConstraint k = (ElementWithConstraint) matros.getData();

				    	  result.getQueryParameter().add(k.toParameter());
				    	  

				    	  
				    	  sb.append( " $ " );
				    	  
				      }
				       
				     
				       
					
					
				} else {
					
					sb.append(" ");
					sb.append(txt);
					sb.append(" ");
					
			
				}
 				
				
			}
			


	
		result.setQueryString(sb.toString().trim());
		
		return result;
		
	}
	
	
	static abstract class ElementWithConstraint {
		
		
		MatrosControlRangeStlye styleRange;

		public MatrosControlRangeStlye getStyleRange() {
			return styleRange;
		}


		public void setStyleRange(MatrosControlRangeStlye styleRange) {
			this.styleRange = styleRange;
		}


		public ElementWithConstraint(InfoBaseElement element) {
			this.element = element;
			initControl();
		}
		
		
		public Control getControl() {
			return control;
		}


		public void setControl(Control control) {
			this.control = control;
		}


		public abstract MatrosQueryParameter toParameter();
		
		public abstract void initControl();


		InfoBaseElement element;
		String constraint;
		Control control;
		
		public InfoBaseElement getElement() {
			return element;
		}
		public void setElement(InfoBaseElement element) {
			this.element = element;
		}
		public String getConstraint() {
			return constraint;
		}
		public void setConstraint(String constraint) {
			this.constraint = constraint;
		}
		
		public String getLabel() {
			
			String label = element.getName();
			
			if(constraint != null) {
				label += "[" + constraint + "]";
			}
			
			return label;
			
		}
		
		public void doUpdate() {
			
		}
		
	}

	

}

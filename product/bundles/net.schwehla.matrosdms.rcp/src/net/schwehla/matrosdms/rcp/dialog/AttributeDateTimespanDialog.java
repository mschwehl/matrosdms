package net.schwehla.matrosdms.rcp.dialog;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.beanutils.BeanUtils;
import
org.eclipse.core.databinding.observable.sideeffect.ISideEffectFactory;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.SelectObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.swt.WidgetSideEffects;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.core.attribute.AbstractInfoAttribute;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.rcp.FixCalendar;

public class AttributeDateTimespanDialog extends Dialog {
	
    static String select1="Selected: 1"; //$NON-NLS-1$
    static String select2="Selected: 2"; //$NON-NLS-1$
    static String select3="Selected: 3"; //$NON-NLS-1$
    
     MatrosMessage messages;

     private AbstractInfoAttribute infoAttribute;
     
     FixCalendar fixCalendar = new FixCalendar();
     
     // XXX inject
     DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());


     private Button btnRadioButtonUnlimited;
     private Button btnRadioButtonTimespan;
     private Button btnRadioButtonYear;
     private Composite compositeUnlimited;
     private Composite compositeTimespan;
     private Composite compositeYear;
     private Text textYear;
     private Label lblFom;
     private Text textFrom;
     private Text textTo;

     public AbstractInfoAttribute getInfoAttribute() {
         return infoAttribute;
     }



     public void setInfoAttribute(AbstractInfoAttribute infoAttribute) {
         this.infoAttribute = infoAttribute;
     }



     public AttributeDateTimespanDialog(Shell parentShell) {
         super(parentShell);
     }

     SelectObservableValue selectedRadioButtonObservable ;


     @Override
     protected Control createDialogArea(Composite parent) {
         Composite container = (Composite) super.createDialogArea(parent);
         container.setLayout(new GridLayout(2, false));

         btnRadioButtonUnlimited = new Button(container, SWT.RADIO); 
         btnRadioButtonUnlimited.setText(messages.datedialog_nolimit);

         compositeUnlimited = new Composite(container, SWT.NONE);
         compositeUnlimited.setLayout(new GridLayout(1, false));
         compositeUnlimited.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

         btnRadioButtonTimespan = new Button(container, SWT.RADIO); btnRadioButtonTimespan.setText(messages.datedialog_timerange);

         compositeTimespan = new Composite(container, SWT.NONE);
         compositeTimespan.setLayout(new GridLayout(2, false));
         compositeTimespan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

         textFrom = new Text(compositeTimespan, SWT.BORDER );
         
         textFrom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
         
         
         
        // textFrom.set

         lblFom = new Label(compositeTimespan, SWT.NONE);
         lblFom.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
         lblFom.setText(messages.datedialog_timerange_from);

         textTo = new Text(compositeTimespan, SWT.BORDER);
         textTo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

         Label lblTo = new Label(compositeTimespan, SWT.NONE);
         lblTo.setText(messages.datedialog_timerange_to);

         btnRadioButtonYear = new Button(container, SWT.RADIO);
        
         btnRadioButtonYear.setText(messages.datedialog_year);

         compositeYear = new Composite(container, SWT.NONE);
         compositeYear.setLayout(new GridLayout(1, false));
         compositeYear.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

         textYear = new Text(compositeYear, SWT.BORDER);
         textYear.setFocus();

// http://stackoverflow.com/questions/16780670/deselect-all-swt-radio-buttons-of-a-group

         // Binding
         selectedRadioButtonObservable = new SelectObservableValue();

         IObservableValue buttonObserveUnlimited = WidgetProperties.selection().observe(btnRadioButtonUnlimited);
         IObservableValue buttonObserveTimespan = WidgetProperties.selection().observe(btnRadioButtonTimespan);
         IObservableValue buttonObserveYear = WidgetProperties.selection().observe(btnRadioButtonYear);

         IObservableValue <String> swtrelevaneFromBinding = WidgetProperties.text(SWT.Modify).observe(textFrom);
         IObservableValue <String> swtrelevaneToBinding = WidgetProperties.text(SWT.Modify).observe(textTo);
         IObservableValue <String> swtYear = WidgetProperties.text(SWT.Modify).observe(textYear);


    

         selectedRadioButtonObservable.addOption(select1 , buttonObserveUnlimited);
         selectedRadioButtonObservable.addOption(select2 , buttonObserveTimespan);
         selectedRadioButtonObservable.addOption(select3 , buttonObserveYear);

         
         ISideEffectFactory sideEffectFactory = WidgetSideEffects.createFactory(btnRadioButtonUnlimited);
         
 		WritableValue<IStatus> yearValidation = new WritableValue<>();
 		WritableValue<IStatus> relevanceFromValidation = new WritableValue<>();
 		WritableValue<IStatus> relevanceToValidation = new WritableValue<>();

// initial unlimited ... warum auch immer         
         
         sideEffectFactory.create(buttonObserveUnlimited::getValue,  x -> {
//
         if (Boolean.TRUE.equals(x)) {
        	 
        	 if (fixCalendar.isEmpty()) {
         		 
          	   textFrom.setEnabled(true);
                 textTo.setEnabled(true);
                 textYear.setEnabled(true);
                 

        	 } else {
            	   textFrom.setEnabled(false);
                   textTo.setEnabled(false);
                   textYear.setEnabled(false);
        	 }
        	 

         }
         });

         
         sideEffectFactory.create(buttonObserveTimespan::getValue,  x -> {

         if (Boolean.TRUE.equals(x)) {
        	 
        	   textFrom.setEnabled(true);
        	   textTo.setEnabled(true);
               textFrom.forceFocus();
               textFrom.setEnabled(true);
               textYear.setEnabled(false);
         }
         });


         sideEffectFactory.create(buttonObserveYear::getValue,  x -> {

         if (Boolean.TRUE.equals(x)) {
        	 	 
        	 	textYear.setEnabled(true);
                textYear.forceFocus();
                
        	 	textFrom.setEnabled(false);
                textTo.setEnabled(false);
         }
         });

         

         sideEffectFactory.create(swtrelevaneFromBinding::getValue,  x -> {
        	 
	         if (x instanceof String && x.length() > 0) {
	                 selectedRadioButtonObservable.setValue(select2);

	                 
	                 try {
	                	 Date date = df.parse(x);
	                	 GregorianCalendar g = new GregorianCalendar();
	                	 g.setTime(date);
	                	 fixCalendar.setRelevancefromCalendar(g);
	                	 relevanceFromValidation.setValue(Status.OK_STATUS);
	                	 
	                 } catch(Exception e) {
	                		relevanceFromValidation.setValue(ValidationStatus.error("Date not parsable"));
	                	 fixCalendar.setRelevancefrom(null);
	                 }
	                 
	                 
	         } else {
	        	 fixCalendar.setRelevancefrom(null);
	        	 relevanceFromValidation.setValue(Status.OK_STATUS);
	         }
	         
         });

         sideEffectFactory.create(swtrelevaneToBinding::getValue,  x -> {
        	 
	         if (x instanceof String && x.length() > 0) {
	                 selectedRadioButtonObservable.setValue(select2);

	                 // XXX inject
	                 DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
	                 
	                 try {
	                	 Date date = df.parse(x);
	                	 GregorianCalendar g = new GregorianCalendar();
	                	 g.setTime(date);
	                	 fixCalendar.setRelevancetoCalendar(g);
	                	 relevanceToValidation.setValue(Status.OK_STATUS);
	                	 
	                 } catch(Exception e) {
		                	 relevanceToValidation.setValue(ValidationStatus.error("Date not parsable"));
	                	 fixCalendar.setRelevanceto(null);
	                 }
	                 
	                 
	         } else {
	        	 fixCalendar.setRelevanceto(null);
	        	 relevanceToValidation.setValue(Status.OK_STATUS);
	         }
	         
         });
         

         sideEffectFactory.create(swtYear::getValue,  x -> {
        	 
         if (x instanceof String && x.length() > 0) {
        	 
     		
        	 try {
				Integer.parseInt(x);
			} catch (Exception e) {
				yearValidation.setValue(ValidationStatus.error("Date not parsable"));
				return;
			}
        	 
     
     		yearValidation.setValue(Status.OK_STATUS);
     		

             Calendar fromCal = new GregorianCalendar();
             fromCal.clear();
             fromCal.set(Calendar.MONTH, Calendar.JANUARY);
             fromCal.set(Calendar.DAY_OF_MONTH,1);
             fromCal.set(Calendar.YEAR, Integer.parseInt(x));
             fixCalendar.setRelevancefromCalendar(fromCal);
        	 
        	 
             Calendar toCal = new GregorianCalendar();
             toCal.clear();
             toCal.set(Calendar.MONTH, Calendar.DECEMBER);
             toCal.set(Calendar.DAY_OF_MONTH,31);
             toCal.set(Calendar.YEAR, Integer.parseInt(x));
             fixCalendar.setRelevancetoCalendar(toCal);
        	 
        	 selectedRadioButtonObservable.setValue(select3);
         } else {
        	 
     	    fixCalendar.setRelevancefromCalendar(null);
            fixCalendar.setRelevancetoCalendar(null);
        	 
      		yearValidation.setValue(Status.OK_STATUS);
         }
         
         
         });

         

         
 		ControlDecorationSupport.create(relevanceFromValidation, SWT.TOP | SWT.LEFT, swtrelevaneFromBinding);
 		ControlDecorationSupport.create(relevanceToValidation, SWT.TOP | SWT.LEFT, swtrelevaneToBinding);
		ControlDecorationSupport.create(yearValidation, SWT.TOP | SWT.LEFT, swtYear);
         
		init();

         return container;
     }

     private void init() {
    	  
         fixCalendar = new FixCalendar();
         
         try {
			BeanUtils.copyProperties(fixCalendar, infoAttribute.getFixcalendar());
			
			if (fixCalendar.isYearOnly() ) {
				textYear.setText("" + fixCalendar.getYearOnlyValue() ); //$NON-NLS-1$
			} else {
				if (fixCalendar.getRelevancefrom() != null) {
					textFrom.setText( df.format( fixCalendar.getRelevancefrom()));
				} 
				if (fixCalendar.getRelevanceto() != null) {
					textTo.setText( df.format( fixCalendar.getRelevanceto()));
				} 
			}
			
		} catch (IllegalAccessException | InvocationTargetException e) {
			super.close();
			throw new RuntimeException(e);
		}
		
	}



	/**
      * Create contents of the button bar
      * @param parent
      */
     @Override
     protected void createButtonsForButtonBar(Composite parent) {
         createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
                 true);
         createButton(parent, IDialogConstants.CANCEL_ID,
                 IDialogConstants.CANCEL_LABEL, false);
     }

     @Override
     protected void okPressed() {
    	 
    	 // errro Prüfen
    	 
    	 if (!select1.equals(selectedRadioButtonObservable.getValue())) {
    		 try {
    				BeanUtils.copyProperties(infoAttribute.getFixcalendar(), fixCalendar);
    			} catch (IllegalAccessException | InvocationTargetException e) {
    				close();
    				throw new RuntimeException(e);
    			}
    	 } else {
    		 infoAttribute.getFixcalendar().reset();
    	 }
         
         
         
        super.okPressed();
     }



     public void setMessages(MatrosMessage messages) {
          this.messages = messages;
     }

     

}

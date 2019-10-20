
package net.schwehla.matrosdms.rcp.parts;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.sideeffect.ISideEffectFactory;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.translation.TranslationService;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.swt.WidgetSideEffects;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.util.ObjectCloner;
import net.schwehla.matrosdms.notification.INotificationService;
import net.schwehla.matrosdms.notification.NotificationNote;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;
import net.schwehla.matrosdms.rcp.swt.composite.MatrosTagGroup;

public class ContextPart {

	MPart _part;
	InfoContext _contextClone;

	private Text txtName;
	private Text txtDescription;
	private Text txtIcon;
	private Text textUUID;	
	private Text textStage;
	
	@Inject
	IEclipseContext context;
	
	@Inject IMatrosServiceService matrosService;
	
	@Inject
	private IEventBroker eventBroker;

	
	@Inject Logger logger;
	
	@Inject
	@Optional
	@Active
	Shell shell;

	@Inject
	@Named(TranslationService.LOCALE) Locale locale;
	
	@Inject 
	EPartService partService;
	
	@Inject INotificationService notificationService;

	
	@Inject
	public ContextPart() {

	}

	@PostConstruct
	public void postConstruct(MPart part, Composite parent) {

		this._part = part;
		
		InfoContext original  = (InfoContext) part.getObject();
		try {
			_contextClone = new ObjectCloner<InfoContext>().cloneThroughSerialize(original);
		} catch (Exception e2) {
			logger.error(e2);
			return;
		}

		parent.setLayout(new GridLayout(1, false));

		Group grpTags = new Group(parent, SWT.NONE);
		grpTags.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpTags.setText("Tags");
		grpTags.setLayout(new GridLayout(3, false));
		

		
		
		MatrosTagGroup wer = MatrosTagGroup.buildTagGroupViaEclipseContext(context, grpTags, _contextClone.getDictionary().get(MyGlobalConstants.ROOT_WER));
		wer.bindViewToModel();
		
		
		MatrosTagGroup was = MatrosTagGroup.buildTagGroupViaEclipseContext(context, grpTags, _contextClone.getDictionary().get(MyGlobalConstants.ROOT_WAS));
		was.bindViewToModel();
		
		
		MatrosTagGroup wo  = MatrosTagGroup.buildTagGroupViaEclipseContext(context, grpTags, _contextClone.getDictionary().get(MyGlobalConstants.ROOT_WO));
		wo.bindViewToModel();
		 
        /*
		MatrosTagGroup groupWho = new MatrosTagGroup(grpTags, "who",
				_context.getDictionary().get(MyGlobalConstants.ROOT_WER));
		groupWho.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
         */
        
		
		Group grpProperties = new Group(parent, SWT.NONE);
		grpProperties.setText("Properties");
		grpProperties.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpProperties.setLayout(new GridLayout(2, false));

		Label lblUUID = new Label(grpProperties, SWT.NONE);
		lblUUID.setText("uuid");

		textUUID = new Text(grpProperties, SWT.BORDER);
		textUUID.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textUUID.setEditable(false);

		Label lblName = new Label(grpProperties, SWT.NONE);

		lblName.setText("name");

		txtName = new Text(grpProperties, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Label lblIcon = new Label(grpProperties, SWT.NONE);

		lblIcon.setText("icon");

		txtIcon = new Text(grpProperties, SWT.BORDER);
		txtIcon.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Label lblDescription = new Label(grpProperties, SWT.NONE);
		lblDescription.setText("description");

		txtDescription = new Text(grpProperties, SWT.BORDER | SWT.MULTI);
		txtDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblStage = new Label(grpProperties, SWT.NONE);
		lblStage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStage.setText("stage");
		
		textStage = new Text(grpProperties, SWT.BORDER);
		textStage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, false, 1, 1));

		bind();
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				try {
					
					updateElement();
					
						
						NotificationNote note = new NotificationNote();
						note.setHeading("Update");
				
						notificationService.openPopup(note);

						// update potential store numbers
						eventBroker.send(MyEventConstants.TOPIC__REFRESH_CONTEXT_MODIFIED,  _contextClone);
						
					

				} catch (MatrosServiceException e1) {
					logger.error(e1);
					
					MessageBox dialog =
						        new MessageBox(shell, SWT.ICON_ERROR | SWT.OK| SWT.CANCEL);
						dialog.setText("My info");
						dialog.setMessage("Element not updated not saved");

						// open dialog and await user selection
						dialog.open();
						 
						NotificationNote note = new NotificationNote();
						note.setHeading("Element nicht gespeichert, bitte Logfile �berpr�fen");
					
						notificationService.openPopup(note);
						
				}
				
				// http://stackoverflow.com/questions/18715369/how-to-programmatically-close-a-eclipse-rcp-4-mwindow
 				partService.hidePart(part,true);
 				
 				_contextClone = null;
 				
				
			}


		});

		btnNewButton.setText("Update");


	}


	private void updateElement() throws MatrosServiceException {
		
		matrosService.updateInfoContext(_contextClone);
		
	}
	


	private void bind() {

		// Initial value
		txtName.setText( _contextClone.getName() != null ? _contextClone.getName() : "" );
		txtDescription.setText( _contextClone.getDescription() != null ? _contextClone.getDescription() : "" );
		txtIcon.setText( _contextClone.getIcon() != null ? _contextClone.getIcon() : "" );
		textUUID.setText( _contextClone.getIdentifier().getUuid() != null ? _contextClone.getIdentifier().getUuid() : "" );

		textStage.setText( _contextClone.getStage() != null ? ""+_contextClone.getStage() : "" );
		
		
		// Bind Gui to Model, oneway

		IObservableValue <String> swttxtNameBinding = WidgetProperties.text(SWT.Modify).observe(txtName);
        IObservableValue <String> swttxtDescriptionBinding = WidgetProperties.text(SWT.Modify).observe(txtDescription);
        IObservableValue <String> swttxtIconBinding = WidgetProperties.text(SWT.Modify).observe(txtIcon);
		IObservableValue <String> swttxtStageBinding = WidgetProperties.text(SWT.Modify).observe(textStage);
		
        
        IObservableValue <String> pojoObserverName = PojoProperties.value("name").observe(_contextClone);
        IObservableValue <String> pojoObserverDescription = PojoProperties.value("description").observe(_contextClone);
        IObservableValue <String> pojoObserverIcon = PojoProperties.value("icon").observe(_contextClone);
        
        ISWTObservableValue xxx = WidgetProperties.text(SWT.Modify)
        	    .observe(textStage);
        
        
        IObservableValue <Integer> pojoObserverStage = PojoProperties.value("stage").observe(_contextClone);
        
        
        ISideEffectFactory sideEffectFactory = WidgetSideEffects.createFactory(txtName);
        
        sideEffectFactory.create(swttxtNameBinding::getValue, pojoObserverName::setValue);
        sideEffectFactory.create(swttxtDescriptionBinding::getValue, pojoObserverDescription::setValue);
        sideEffectFactory.create(swttxtIconBinding::getValue, pojoObserverIcon::setValue);
        
        sideEffectFactory.create(xxx::getValue, e -> {
        	
        	if (e == null || e.toString().trim().length() == 0) {
        		_contextClone.setStage(null);
        	} else {
        		_contextClone.setStage(Integer.parseInt("" + e));	
        	}
        	
        	
        });
        
		
		
	}
}
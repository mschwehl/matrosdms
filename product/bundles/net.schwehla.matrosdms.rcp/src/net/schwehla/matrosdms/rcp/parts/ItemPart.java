 
package net.schwehla.matrosdms.rcp.parts;

import java.io.File;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.sideeffect.ISideEffectFactory;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.core.services.translation.TranslationService;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
//import org.eclipse.e4.ui.progress.IProgressConstants;
//import org.eclipse.e4.ui.progress.IProgressService;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.swt.WidgetSideEffects;
import org.eclipse.jface.databinding.viewers.IViewerObservableValue;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.domain.core.InfoOrginalstore;
import net.schwehla.matrosdms.domain.core.attribute.AbstractInfoAttribute;
import net.schwehla.matrosdms.domain.core.attribute.AttributeType;
import net.schwehla.matrosdms.domain.core.attribute.InfoBooleanAttribute;
import net.schwehla.matrosdms.domain.core.attribute.InfoDateAttribute;
import net.schwehla.matrosdms.domain.core.attribute.InfoNumberAttribute;
import net.schwehla.matrosdms.domain.core.attribute.InfoTextAttribute;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.domain.metadata.MatrosMetadata;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.lucene.ILuceneService;
import net.schwehla.matrosdms.notification.INotificationService;
import net.schwehla.matrosdms.notification.NotificationNote;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.progress.IProgressConstants;
import net.schwehla.matrosdms.progress.IProgressService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;
import net.schwehla.matrosdms.rcp.celleditor.AttributeValidToDialogEditor;
import net.schwehla.matrosdms.rcp.celleditor.MatrosDateCellEditor;
import net.schwehla.matrosdms.rcp.celleditor.MatrosNumberCellEditor;
import net.schwehla.matrosdms.rcp.celleditor.MultilineTextCellEditor;
import net.schwehla.matrosdms.rcp.dialog.ItemMetadataDialog;
import net.schwehla.matrosdms.rcp.parts.helper.AutoResizeTableLayout;
import net.schwehla.matrosdms.rcp.parts.helper.DesktopHelper;
import net.schwehla.matrosdms.rcp.parts.helper.ItemPartElementWrapper;
import net.schwehla.matrosdms.rcp.parts.helper.ItemPartElementWrapper.Type;
import net.schwehla.matrosdms.rcp.parts.helper.Jobber;
import net.schwehla.matrosdms.rcp.parts.include.ItemPartFixedAttributesGroup;
import net.schwehla.matrosdms.rcp.parts.include.ItemPartMetadataGroup;
import net.schwehla.matrosdms.rcp.swt.TypedComboBox;
import net.schwehla.matrosdms.rcp.swt.TypedComboBoxLabelProvider;
import net.schwehla.matrosdms.resourcepool.IMatrosResource;


public class ItemPart {


    DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
    
	@Inject
	@Named(TranslationService.LOCALE) Locale locale;
	
	@Inject 
	EPartService partService;
	
	@Inject
	MApplication application;

	@Inject 
	DesktopHelper desktopHelper;
	
	
    @Inject
    ILuceneService indexService;
    
    IObservableValue<Boolean> hasOrginalStoredTrueObservable;
	
	@Inject
	IProgressService progressService;
	
	@Inject
	@Optional
	@Active
	Shell shell;

	@Inject IMatrosServiceService matrosService;

	@Inject
	@Translation
	MatrosMessage messages;

	@Inject
	IEclipseContext context;

	TableViewer _selected;
	private Text textOrderNumber;
	private Button checkBoxAutonnumber;
	
	private Color defaultListColor;


	private Color defaultAttributeColor;
	
	private Button btnNewButton;

	@Inject Logger logger;
	
	
	@Inject INotificationService notificationService;

	TypedComboBox<InfoOrginalstore> box;
	
	TypedComboBox<Stage> boxInfoItemState;
	
	
	Link link_showMetadata;
	Link link_moveNotProcessed;
	
	
	@Inject
	@Preference(nodePath = MyGlobalConstants.Preferences.NODE_COM_MATROSDMS) 
	IEclipsePreferences preferences ;
	
	
	@Inject
	public ItemPart() {
		
	}
	

	@Inject
	private MDirtyable _dirty;

	@Inject
	private IEventBroker eventBroker;




	
	ItemPartElementWrapper _wrapper;
	
	
	Text lblSelectedContext;
	
	
	 @Inject
	 IMatrosResource poolOfResources;


	@ Inject ItemPartMetadataGroup _partMetaIncludeContent;
	@ Inject ItemPartFixedAttributesGroup _itemPartFixedAttributesGroup;

	

	  private Group 				_swtGroupActions;
	  private Group 				_swtGroupAttributes;
	  private TableViewer 			_swtTableViewerAttributes;
	  private MPart _part;
	  
		
		
		@Inject Shell activeShell;


		
	
	
	@Inject
	@Optional
	private void subscribeTOPIC_TOPIC__REFRESH_ITEM_ADD(
			@UIEventTopic(MyEventConstants.TOPIC__REFRESH_ITEM_STAR) InfoItem type) {
		
		// self-part: About to be closed
		if (type.getContext() != _wrapper.getInfoItem().getContext()) {
	
			if (!textOrderNumber.isDisposed() && textOrderNumber.isEnabled() && box != null) {
						updateOriginalNumber();
			}
			
		}
		
	}
	
	
	
	
	    @Inject
		@Optional
		
		// XXX nur g�ltig auf den ActivPart
		
		private void subscribeTopicTOPIC_TAGGRAPH_DOUBLEKLICK_ADD_ELEMENT(@UIEventTopic(MyEventConstants.TOPIC_TAGGRAPH_DOUBLEKLICK_ADD_ELEMENT) InfoKategory type, @Active MPart part) {
		
			
			if (part.getObject() == this) {
				
				if (type != null 
						&& _wrapper.getInfoItem() != null) {
					
					if (type.getRoot().getIdentifier().equals( MyGlobalConstants.ROOT_ART )) {
						
						 if (! _wrapper.getInfoItem().getTypList().contains(type ) ) {
							 _wrapper.getInfoItem().getTypList().add(type);
								
				// XXX NPE if new Kategory is doubleklicked
							 _partMetaIncludeContent.refresh(true);
						
					
						 }
						 
					}
					
				}
				
			}
		}
	    
	    


	@PostConstruct
	public void createComposite(MPart part, Composite parent) {
	    	
    	this._part = part;
    	
    	Object o = part.getObject();
    	
    	_wrapper = (ItemPartElementWrapper) o;
    	_part.getTags().add(MyGlobalConstants.TAG_REMOVEONCLOSE);
	    _part.setDescription("TODO");
				
    	
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite compositeContent = new Composite(parent, SWT.NONE);
		
		
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginHeight = 0;
		gl_composite.verticalSpacing = 0;
		gl_composite.marginWidth = 0;
		gl_composite.horizontalSpacing = 0;		
		compositeContent.setLayout(gl_composite);


		_partMetaIncludeContent.appendElementsToParentComposit(_wrapper.getInfoItem(), compositeContent);
		
		_partMetaIncludeContent.get_swtTextContext().setText( _wrapper.getInfoItem().getContext().getName() );
		
		_partMetaIncludeContent.get_swtFileName().setText(_wrapper.getInfoItem().getName());
		
	
		if (_wrapper.getType() == Type.NEW) {
			_partMetaIncludeContent.get_swtFileName().setText( _wrapper.getInboxFile().getName());		
			
		} else {
			_partMetaIncludeContent.get_swtTxtItemname().setText( _wrapper.getInfoItem().getMetadata().getFilename());
		}
		 
	    createGroupDocuments(compositeContent);
	    createGroupOriginalstore(compositeContent);
   		createGroupAttributes(compositeContent);
   		

   		
		
		Composite compositeButtons = new Composite(compositeContent, SWT.NONE);
		compositeButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		compositeButtons.setLayout(new GridLayout(2, false));
		

		boxInfoItemState = new TypedComboBox<>(compositeButtons);
		
		List <Stage> stateList = new ArrayList<>();
		Stage s1 = new Stage();
		s1.setDisplaytext("ADDED");
		s1.setValue(1);
		stateList.add(s1);
		
		Stage s2 = new Stage();
		s2.setDisplaytext("COMPLETE");
		s2.setValue(2);
		stateList.add(s2);
		
		Stage selected = new Stage();
		selected.value = _wrapper.getInfoItem().getStage();

				
		boxInfoItemState.setLabelProvider(new TypedComboBoxLabelProvider<Stage>() {

	        @Override
	        public String getSelectedLabel(Stage element) {
	            return element.getDisplaytext();
	        }

	        @Override
	        public String getListLabel(Stage element) {
	            return element.getDisplaytext();
	        }
	    });
		

	    
		boxInfoItemState.setContent(stateList);
		
		
		boxInfoItemState.setSelection(selected);
		

	    btnNewButton = new Button(compositeButtons, SWT.NONE);
	    btnNewButton.setEnabled(false);

	    
		btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	    
		if (_wrapper.getType() == Type.NEW) {
			
		    btnNewButton.setText("processing...");
	

			
		 // create or update		
		 		btnNewButton.addSelectionListener(new SelectionAdapter() {
		 			
		 			@Override
		 			public void widgetSelected(SelectionEvent e) {
		 				
		 					if (btnNewButton.isDisposed() || ! btnNewButton.isEnabled()) {
		 						return;
		 					}
		 				
		
		 					// XXX bind
		 					_wrapper.getInfoItem().setName( _partMetaIncludeContent.get_swtTxtItemname().getText());
		 					
		 					// XXX Check if Background-job is running
		 					try {
		 						matrosService.appendContainer(_wrapper.getInboxFile(),  _wrapper.getInfoItem());
		 						
//		 						// XXX better send event
//		 						_localInfoItem.getContext().getStorableInfoItemContainerListProxy().setCount( 
//		 								_localInfoItem.getContext().getStorableInfoItemContainerListProxy().getCount() +1 );

		 						// Inform the originalstore
		 						// todo: service 
		 						
		 						NotificationNote note = new NotificationNote();
		 						
		 						String store = ": no store";
		 						if (box.getSelection() != null) {
		 							store = ": " + box.getSelection().getName() + "@" +	_wrapper.getInfoItem().getStoreItemNumber();
		 						}
		 						
		 						note.setHeading("Dokument gespeichert "+ store);
		 						
		 						logger.info( "Dokument gespeichert "+ store);
		 				
		 						
		 						// try move file to the processedfolder
		 						
		 						
		 						try {
									
		 							
		 				

		 							String inboxProcessed =  preferences.get(MyGlobalConstants.Preferences.PROCESSED_PATH, "" );
		 							inboxProcessed = inboxProcessed.replaceAll(";","");
		 							
		 							if (inboxProcessed == null || inboxProcessed.trim().length() == 0) {
		 								throw new IllegalStateException("no processed folder specified, please go to the preferences");
		 							}
		 							
		 							
		 							File targetProcessed = new File(inboxProcessed);
		 								
		 								if(! targetProcessed.exists()) {
		 									
		 									boolean create = targetProcessed.mkdirs();
		 									
		 									if (!create) {
		 										throw new IllegalStateException("cannot create processed folder");
		 									}

		 								}
		 								
		 							
		 							
		 							Path moveSourcePath = _wrapper.getInboxFile().toPath() ;
		 							
		 							String filename = _wrapper.getInfoItem().getName() ;
		 							
		 							
		 							if (Objects.nonNull( _wrapper.getInfoItem().getStoreIdentifier())) {
		 								
		 								String storeid = matrosService.getOriginalStoreByIdentifer( 
		 										_wrapper.getInfoItem().getStoreIdentifier()).getName();
		 								
		 								storeid += "_" + _wrapper.getInfoItem().getStoreItemNumber();
		 								
		 								filename += "@STORE_" + storeid;
		 								
		 							} else {
		 								
		 								filename += "@DELETED_";
		 								
		 							}
		 						
		 							filename += "@DATE_" + df.format(_wrapper.getInboxFile().lastModified());
		 							
		 							filename += "@UUID_" +  _wrapper.getInfoItem().getIdentifier().getUuid();
		 							filename += DesktopHelper.getExtension(_wrapper.getInfoItem().getMetadata().getFilename());
		 							
		 							Path moveTargetPath = new File(inboxProcessed + File.separator  + filename ).toPath();
		 							
		 							Files.move( moveSourcePath, moveTargetPath );
		 							
		 							
		 							if(! moveTargetPath.toFile().exists()) {
		 								throw new IllegalStateException("targetfile not created " +  moveTargetPath.getFileName());
		 							}
		 							
		 							if( moveSourcePath.toFile().exists()) {
		 								throw new IllegalStateException("sourcefile not moved " +  moveSourcePath.getFileName());
		 							}
		 							
									
									note.setHeading( note.getHeading() + " and document moved");
									
								} catch (Exception e1) {
									
									logger.info( "File not moved from inbox (maybe open ?)  \n" + e1.getMessage());
					
/*									
			 						MessageBox dialog =
			 						        new MessageBox(shell, SWT.ICON_ERROR | SWT.OK| SWT.CANCEL);
			 						dialog.setText("My info");
			 						dialog.setMessage("File not moved to inbox (maybe open ?)  \n" + e1.getMessage());
									dialog.open();
*/
									
									note.setHeading( note.getHeading() + " but document NOT moved");
									
									
								}
		 					 finally {
		 						// synch viewer
		 						eventBroker.send(MyEventConstants.TOPIC_REFRESH_INBOX_FILE_MOVED, _wrapper.getInboxFile());
		 					}
		 						
		 						notificationService.openPopup(note);
		 						
		 						// update potential store numbers
		 						eventBroker.send(MyEventConstants.TOPIC__REFRESH_ITEM_ADD,  _wrapper.getInfoItem());
		 						


		 					} catch ( Exception  e1 ) {
		 					
		 						logger.error(e1);
		 						
		 						MessageBox dialog =
		 						        new MessageBox(shell, SWT.ICON_ERROR | SWT.OK| SWT.CANCEL);
		 						dialog.setText("My info");
		 						dialog.setMessage("File not saved");

		 						// open dialog and await user selection
		 						dialog.open();
		 						 
		 						NotificationNote note = new NotificationNote();
		 						note.setHeading("Element nicht gespeichert, bitte Logfile �berpr�fen");
		 					
		 						notificationService.openPopup(note);
		 			
		 					
		 				}
		 				
		 		
		 				
		 				// http://stackoverflow.com/questions/18715369/how-to-programmatically-close-a-eclipse-rcp-4-mwindow
		 				partService.hidePart(part,true);
		 				
		 				_wrapper = null;
		 				
		 				
		 				// publish
		 				
		 			}
		 		});
		 		
		 		
		} else {
		    btnNewButton.setText("Update");
		    btnNewButton.setVisible(true);

		    
			// initial value
			if (_wrapper.getInfoItem().getStoreIdentifier() != null) {
				try {
					InfoOrginalstore ost = matrosService.getOriginalStoreByIdentifer(_wrapper.getInfoItem().getStoreIdentifier() );
					box.setSelection(ost);
				} catch (MatrosServiceException e) {
					logger.error(e);
				}
			}

			
			 // create or update		
			 		btnNewButton.addSelectionListener(new SelectionAdapter() {
			 			
			 			@Override
			 			public void widgetSelected(SelectionEvent e) {
			 				
			
			 					// XXX Check if Background-job is running
			 				
			 				// if no original than delete grayed selection pre service call
			 					if (! hasOrginalStoredTrueObservable.getValue()) {
			 						_wrapper.getInfoItem().setStoreItemNumber(null);
			 						_wrapper.getInfoItem().setStoreIdentifier(null);
			 					}

			 					
			 					try {
			 						matrosService.updateInfoElement( _wrapper.getInfoItem());
			 						
//			 						// XXX better send event
//			 						_localInfoItem.getContext().getStorableInfoItemContainerListProxy().setCount( 
//			 								_localInfoItem.getContext().getStorableInfoItemContainerListProxy().getCount() +1 );

			 						// Inform the originalstore
			 						// todo: service 
			 						
			 						NotificationNote note = new NotificationNote();
			 						note.setHeading("Dokument update");
			 				
			 						notificationService.openPopup(note);

			 						// update potential store numbers
			 						eventBroker.send(MyEventConstants.TOPIC__REFRESH_ITEM_MODIFIED,  _wrapper.getInfoItem());
			 						

			 					} catch ( Exception  e1 ) {
			 					
			 						logger.error(e1);
			 						
			 						MessageBox dialog =
			 						        new MessageBox(shell, SWT.ICON_ERROR | SWT.OK| SWT.CANCEL);
			 						dialog.setText("My info");
			 						dialog.setMessage("File not saved");

			 						// open dialog and await user selection
			 						dialog.open();
			 						 
			 						NotificationNote note = new NotificationNote();
			 						note.setHeading("Element nicht gespeichert, bitte Logfile �berpr�fen");
			 					
			 						notificationService.openPopup(note);
			 		
			 					
			 				}
			 						
			 				// http://stackoverflow.com/questions/18715369/how-to-programmatically-close-a-eclipse-rcp-4-mwindow
			 				partService.hidePart(part,true);
			 				
			 				_wrapper = null;
			 				
			 				
			 				// publish
			 				
			 				// http://stackoverflow.com/questions/18715369/how-to-programmatically-close-a-eclipse-rcp-4-mwindow
							partService.hidePart(part,true);
							
							_wrapper = null;
							
			 				
			 			}
			 			
			 			
			 		});
			 		
		    
		
				
		}
		



		
		
		
		bind();
		
		// calculateChecksum and analyze 
		
		if (_wrapper.getType() == Type.NEW) {
			executeAsyncAnalyzer() ;
		}
		
		
		

	}



	private void bind() {


        ISideEffectFactory sideEffectFactory = WidgetSideEffects.createFactory(_partMetaIncludeContent.get_swtTxtItemname());

          
         IObservableValue <String> swtItemName = WidgetProperties.text(SWT.Modify).observe(_partMetaIncludeContent.get_swtTxtItemname());
 		 IObservableValue<String> myModelUser = PojoProperties.value("name").observe(_wrapper.getInfoItem());
          
 		 // die eine richtung
         sideEffectFactory.create(myModelUser::getValue, swtItemName::setValue);

         sideEffectFactory.create(swtItemName::getValue, x -> {

                if (x != null) {
               	 _wrapper.getInfoItem().setName(x);
                } else {
              	 _wrapper.getInfoItem().setName(null);
                }
         });


         

			WritableValue<IStatus> firstNameValidation = new WritableValue<>();
			sideEffectFactory.create(() -> {
				String firstName = (String) swtItemName.getValue();
				if (firstName != null && firstName.isEmpty()) {
					
					firstNameValidation.setValue(ValidationStatus.error("Name must not be Empty"));

					return;
				}
				firstNameValidation.setValue(Status.OK_STATUS);

			});
			
         
			ControlDecorationSupport.create(firstNameValidation, SWT.TOP | SWT.LEFT, swtItemName);

		      IObservableValue<Boolean> hasName =
                      ComputedValue.create(() -> Status.OK_STATUS.equals( firstNameValidation.getValue() )  );

				
	          sideEffectFactory.create(hasName::getValue, btnNewButton::setEnabled);
			
			
	          IViewerObservableValue selectedBoxElement = ViewerProperties
	                   .singleSelection().observe(boxInfoItemState.getViewer());

	          
	  		 IObservableValue<Integer> myModelState = PojoProperties.value("stage").observe(_wrapper.getInfoItem());
	  		 
	  		 
	         sideEffectFactory.create(selectedBoxElement::getValue,  x -> {

	               if (x != null) {
	              	 Stage casted = ((Stage) x);	
	              	 	_wrapper.getInfoItem().setStage(casted.getValue());
	              	 
	               } else {
	              	 _wrapper.getInfoItem().setStage(0);

	               }

	        });

	          
	          
		
	}




	private void createGroupAttributes(Composite compositeContent) {
		_swtGroupAttributes = new Group(compositeContent, SWT.NONE);
		 _swtGroupAttributes.setText("Attribute");
		 
		 
		 
		 _swtGroupAttributes.setLayout(new GridLayout(1,false));
		 

		 
		 GridData gd_groupAttributes = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		 gd_groupAttributes.minimumHeight  = 50;
		 
		 _swtGroupAttributes.setLayoutData(gd_groupAttributes);
		 

		 _itemPartFixedAttributesGroup.appendElementsToParentComposit(_wrapper.getInfoItem(), _swtGroupAttributes);
		 
		 
		 // Table einf�gen
		 // http://help.eclipse.org/kepler/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fjface%2Fviewers%2FViewerColumn.html&anchor=setEditingSupport(org.eclipse.jface.viewers.EditingSupport)
        _swtTableViewerAttributes = new TableViewer(_swtGroupAttributes,SWT.FULL_SELECTION);
        
        
        _swtTableViewerAttributes.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1 ));
        
        _swtTableViewerAttributes.setContentProvider(ArrayContentProvider.getInstance());
        

        _swtTableViewerAttributes.setColumnProperties(new String[]{"a" , "b"});
        
		AutoResizeTableLayout layout = new AutoResizeTableLayout(_swtTableViewerAttributes.getTable());
		createAttributeTableColumns(layout);
        
        _swtTableViewerAttributes.getTable().addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				if (e.keyCode == SWT.DEL) {
				
					List list2 = _swtTableViewerAttributes.getStructuredSelection().toList();
					_wrapper.getInfoItem().getAttributeList().removeAll(list2);
					_swtTableViewerAttributes.refresh();
					
				}
				
				super.keyPressed(e);

			}
			
			
		});
		
        
          
        
        _swtTableViewerAttributes.setInput(_wrapper.getInfoItem().getAttributeList());
        
                _swtTableViewerAttributes.refresh();
                
                // Attribute-Drop
                
                
                DropTarget dropTargetAttribute = new DropTarget(_swtTableViewerAttributes.getTable(), DND.DROP_MOVE | DND.DROP_LINK | DND.DROP_COPY);
                
                
                // https://georgiangrec.wordpress.com/2013/08/11/swt-composite-drag-and-drop/
        		final LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();

        		dropTargetAttribute.setTransfer(new Transfer[] { transfer , TextTransfer.getInstance()});
	
        		dropTargetAttribute.addDropListener(new DropTargetAdapter() {
			

			
			@Override
			public void drop(DropTargetEvent event) {

				if (TextTransfer.getInstance().equals( event.currentDataType)) {
					
					// create a dialog with ok and cancel buttons and a question icon
					MessageBox dialog =
					        new MessageBox(shell, SWT.ICON_WARNING | SWT.OK );
					dialog.setText("to much elements" ); 
					dialog.setMessage("just 4 elements");

					// open dialog and await originalstore selection
					int val = dialog.open();
					
					if (val == Window.OK) {
						return;
					}
					
					
				} else {
					
					
					//
					
					final StructuredSelection droppedObj =  (StructuredSelection) event.data;
					
					if (droppedObj != null) {
						
						try {
		  				TableItem i = (TableItem) droppedObj.getFirstElement();
			  				
			  				AttributeType fdata = (AttributeType) i.getData();
			  				
			  				Constructor<?> c = Class.forName( fdata.getType().getJavaModelType() ).getConstructor(Identifier.class, String.class);
			  			
			  				AbstractInfoAttribute newObject = (AbstractInfoAttribute) c.newInstance( Identifier.createNEW(), "" + fdata.getType().name() );
			  				
			  				// set initial values (boolean: true as default)
			  				newObject.init();
			  				newObject.setType(fdata);
			  				
			  				_wrapper.getInfoItem().getAttributeList().add(newObject);

							_swtTableViewerAttributes.refresh(true);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
		
								
					}
					
					//
					
					
				}
						

				
			}
		});
	}



	private void createGroupOriginalstore(Composite compositeContent) {
		Group grpPhysicalStorage = new Group(compositeContent, SWT.NONE);
		
	
		grpPhysicalStorage.setLayout(new GridLayout(2, false));
		grpPhysicalStorage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		
		grpPhysicalStorage.setText(messages.itempart_originalstore);
		
		Composite compositeStorageRadio = new Composite(grpPhysicalStorage, SWT.NONE);
		compositeStorageRadio.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		compositeStorageRadio.setLayout(new GridLayout(1, false));
		

		Button btnRadioButtonOriginalStored = new Button(compositeStorageRadio, SWT.RADIO);
		btnRadioButtonOriginalStored.setText(messages.itempart_btnRadioButtonOriginalStored);
		
		
		Button btnRadioButtonOriginalDeleted = new Button(compositeStorageRadio, SWT.RADIO);
		btnRadioButtonOriginalDeleted.setText(messages.itempart_btnRadioButtonOriginalDeleted);
		
		Composite compositeStorageDetail = new Composite(grpPhysicalStorage, SWT.NONE);
		compositeStorageDetail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeStorageDetail.setLayout(new GridLayout(1, false));
		
		
		 box = new TypedComboBox<>(compositeStorageDetail);
		
		 box.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		 
			
		    box.setLabelProvider(new TypedComboBoxLabelProvider<InfoOrginalstore>() {

		        @Override
		        public String getSelectedLabel(InfoOrginalstore element) {
		            return element.getName();
		        }

		        @Override
		        public String getListLabel(InfoOrginalstore element) {
		            return element.getName();
		        }
		    });

			try {
				
				List <InfoOrginalstore> content = matrosService.loadOriginalStoreList();

				box.setContent(content);
		    
		    
	

			} catch (MatrosServiceException e2) {
				logger.error(e2);
			}
			
			
			
		// http://stackoverflow.com/questions/8168658/eclipse-rcp-clear-button-in-textinput
	
			Composite numbers = new Composite(compositeStorageDetail, SWT.NONE);
			numbers.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			
			numbers.setLayout( new GridLayout(2, false));
			
			textOrderNumber = new Text(numbers,SWT.BORDER);
			textOrderNumber.setEnabled(false);
			
			checkBoxAutonnumber =  new Button(numbers,SWT.CHECK);
			checkBoxAutonnumber.setText("auto");
			checkBoxAutonnumber.addSelectionListener(new SelectionAdapter() {
			    @Override
			    public void widgetSelected(SelectionEvent event) {
			        if (event.detail == SWT.CHECK) {
			            // Now what should I do here to get
			            // Whether it is a checked event or unchecked event.
			        }
			    }
			});
			
			
		
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=293230
// NOT ON WINDOWS :-(		
//	
//		Listener listener = event -> {
//			Text t = (Text) event.widget;
//			String msg = t.getMessage();
//			if (event.detail == SWT.ICON_CANCEL) {
//				System.out.println("Cancel on " + msg);
//			} else if (event.detail == SWT.ICON_SEARCH) {
//				System.out.println("ICON on " + msg);
//			} else {
//				System.out.println("Default selection on " + msg);
//			}
//		};
//		
//		
//		textOrderNumber.addListener(SWT.DefaultSelection, listener);
//		

   		
		if (_wrapper.getType() == Type.EXISTING) {
			
			if (_wrapper.getInfoItem().getStoreItemNumber() != null) {
				textOrderNumber.setText(_wrapper.getInfoItem().getStoreItemNumber() );
			} 
			
				
			
			if (_wrapper.getInfoItem().getStoreIdentifier() != null) {
				textOrderNumber.setText(_wrapper.getInfoItem().getStoreItemNumber() );
			}
			
				
				List <InfoOrginalstore> storeList = box.getContent();
				
				for (InfoOrginalstore tmp: storeList) {
					if ( tmp.getIdentifier().equals( _wrapper.getInfoItem().getStoreIdentifier())) {
						box.setSelection(tmp);
						break;
					}
				}
				
				
				// not selected
				if (_wrapper.getInfoItem().getStoreItemNumber() == null) {
					btnRadioButtonOriginalDeleted.setSelection(true);
					btnRadioButtonOriginalStored.setSelection(false);
				} 
				

			}
			

		
	
       IObservableValue <String> pojoObserverStoreUUID = PojoProperties.value("storeIdentifier.uuid").observe(_wrapper.getInfoItem());
       IObservableValue <String> textOrderNumberObservable = WidgetProperties.text(SWT.Modify).observe(textOrderNumber);
      
       ISWTObservableValue vorhandenBool = WidgetProperties.selection().observe(btnRadioButtonOriginalStored);
       IViewerObservableValue selectedBoxElementBinding = ViewerProperties
                .singleSelection().observe(box.getViewer());


        ISideEffectFactory sideEffectFactory = WidgetSideEffects.createFactory(btnRadioButtonOriginalStored);

        

     hasOrginalStoredTrueObservable =
                              ComputedValue.create(() -> Boolean.TRUE.equals( vorhandenBool.getValue() ) || vorhandenBool.getValue() == null   );

       
        sideEffectFactory.create(hasOrginalStoredTrueObservable::getValue, box.getCombo()::setEnabled);
        sideEffectFactory.create(hasOrginalStoredTrueObservable::getValue, textOrderNumber::setEnabled);
        sideEffectFactory.create(hasOrginalStoredTrueObservable::getValue, textOrderNumber::setEnabled);

   
        sideEffectFactory.create(pojoObserverStoreUUID::getValue, selectedBoxElementBinding::setValue);
        
        sideEffectFactory.create(hasOrginalStoredTrueObservable::getValue, x -> {

               if (Boolean.TRUE.equals(x)) {
              	 	box.getCombo().forceFocus();
               }

        });

        

        sideEffectFactory.create(selectedBoxElementBinding::getValue,  x -> {

               if (x != null) {
              	 
              	 InfoOrginalstore casted = ((InfoOrginalstore) x);	
              	 
              	 	updateOriginalNumber();
              	 	_wrapper.getInfoItem().setStoreIdentifier(casted.getIdentifier());
              	 	
              	 	vorhandenBool.setValue(true);

               } else {
              	 _wrapper.getInfoItem().setStoreIdentifier(null);
           	 	vorhandenBool.setValue(false);

               }


        });


        sideEffectFactory.create(textOrderNumberObservable::getValue, x -> {


               if (x != null) {

              	 _wrapper.getInfoItem().setStoreItemNumber(x);

               } else {

              	 _wrapper.getInfoItem().setStoreItemNumber(null);

               }
        });

        sideEffectFactory.create(pojoObserverStoreUUID::getValue, x -> {

               if (x != null) {
        //           textOrderNumber.setText(x);
               } else {
              	 textOrderNumber.setText("");
               }

        });
        
        
        
        
	    if (box.getCombo().getItemCount() > 0)  {
	    	box.getCombo().setEnabled(true);
	    }

	    

		
  	    
	}


	

	private void updateOriginalNumber() {
		try {
			int nextNumber = matrosService.getNextFreeOriginalstoreNumber(box.getSelection().getIdentifier());
			
				if (textOrderNumber != null && (textOrderNumber.getText().equals("") || checkBoxAutonnumber.getSelection())) {
					
					checkBoxAutonnumber.setSelection(true);
					checkBoxAutonnumber.setText("autoupdate");
					
					textOrderNumber.setText("" + nextNumber );
					textOrderNumber.redraw();
					checkBoxAutonnumber.redraw();
					
					_wrapper.getInfoItem().setStoreItemNumber("" + nextNumber );
					
				}
			
			

			
		} catch(Exception e) {
			logger.error(e);
		}
	}



	private void createGroupDocuments(Composite compositeContent) {
		
		_swtGroupActions =   new Group(compositeContent, SWT.NONE);
		_swtGroupActions.setText(messages.itempart_swtGroupActions);
		
		  RowLayout layout = new RowLayout();
	        // Optionally set layout fields.
	        layout.wrap = true;
	        layout.marginLeft = 5;
	        layout.marginTop = 5;
	        layout.marginRight = 5;
	        layout.marginBottom = 5;
	        layout.spacing = 10;
	        layout.type = SWT.HORIZONTAL;
	        
		_swtGroupActions.setLayout(layout);
		
		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;

		_swtGroupActions.setLayoutData(gridData);
		
		
//		GridData groupDocumentsLayout = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
	//	groupDocumentsLayout.minimumHeight  = 50;
		
//		_swtGroupActions.setLayoutData(groupDocumentsLayout);
		
		
		// add some actions
		
		if (_wrapper.getType() == Type.NEW) {
			Link link_analyze = new Link(_swtGroupActions, SWT.NONE);
			
			link_analyze.setText("<a>Analyze</a>");
					
			link_analyze.addSelectionListener(new SelectionAdapter()
	        {
	            @Override
	            public void widgetSelected(SelectionEvent e)
	            {
	            	
	       // xxx context kann nicht aufgel�st werden
//	            	AnalyzeItemResultDialog dialog = ContextInjectionFactory.make(AnalyzeItemResultDialog.class, context);
//	            
//	            	dialog.create();
//	        		dialog.open();
	            		
	    		  
	            }
	        });
		}
		
		
		Link link_open = new Link(_swtGroupActions, SWT.NONE);
		
		link_open.setText("<a>Open</a>");
		
		switch (_wrapper.getType()) {
		
			case EXISTING:
				
				link_open.setData(new OpenExistingJobber());
				break;
				
			case NEW:
				
				link_open.setData(new OpenNewJobber());
				break;
				
		}
		

				
		link_open.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
            	try {
            	 
            		((Jobber) link_open.getData()).execute();
            		
            	}catch(Exception ex) {
            		logger.error(ex);
            	}
           
            		
    		  
            }
        });

		
		//
		
		
		link_showMetadata = new Link(_swtGroupActions, SWT.NONE);
		
		link_showMetadata.setText("<a>Metadata</a>");
		link_showMetadata.setData(new ShowMetadataJobber());
		
		link_showMetadata.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
            	try {
            	 
            		((Jobber) link_showMetadata.getData()).execute();
            		
            	}catch(Exception ex) {
            		logger.error(ex);
            	}
           
            		
    		  
            }
        });
		
		link_showMetadata.setVisible(false);
		
		
	//	link_moveNotProcessed.setVisible(false);


	}


	@Inject UISynchronize sync;
	
	private void executeAsyncAnalyzer() {

		
				Job job = new Job(messages.global_job_calculate_checksum) {
					
					MatrosMetadata mmd = null;
					
					List <InfoItem> doubles = new ArrayList <>();
			
					  @Override
					  protected IStatus run(IProgressMonitor monitor) {
					    // do something long running
					    //... 
 					  setProperty(IProgressConstants.KEEPONE_PROPERTY, Boolean.TRUE);
						  
 			
						  
				    	 	 try {
								 
				    	 		mmd = indexService.parseMetadata(_wrapper.getInboxFile());
								logger.debug("Hash berechnet" + mmd.getSha256() ) ;
								
								
								  // If you want to update the UI
							    sync.asyncExec(new Runnable() {
							      @Override
							      public void run() {
							    	  	
							    	  _wrapper.getInfoItem().setMetadata(mmd); 
							      }
							    });
							    
							    
														
								doubles = matrosService.checkForDuplicate(mmd);
																
								
				    	 		// auto-sync
				    	 		eventBroker.send(MyEventConstants.STATUSBAR,  mmd.getSha256() );
				    	
				    
							
			    			} catch (Exception e) {
								logger.error(e);
								return Status.CANCEL_STATUS;
							}
					  
							monitor.worked(1);
								
							
							if (doubles != null && ! doubles.isEmpty()) {
								
							    // If you want to update the UI
							    sync.asyncExec(new Runnable() {
							      @Override
							      public void run() {
							    	  	
							     	  btnNewButton.setText("Doublette");
							    	  
							    	  // place to update the gui
							    	  
										MessageBox dialog =
										        new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
										dialog.setText("My info");
										
										
										
										dialog.setMessage("File gibt es schon: " + doubles.get(0).getName() + "-> " + doubles.get(0).getContext().getName() );

										// open dialog and await user selection
										int i = dialog.open();
										btnNewButton.setEnabled(false);
										
								
							      }
							    });
							    
								   return  new Status(IStatus.ERROR , "MATROS", "Doublette"); //$NON-NLS-1$ 

							}
							
					 		
			    	 		  // If you want to update the UI
						    sync.asyncExec(new Runnable() {
						      @Override
						      public void run() {
						    	  
						    	  // User klicks too fast
						    	  if (btnNewButton != null && !btnNewButton.isDisposed() ) {
						    		  btnNewButton.setEnabled(true);
						    	 	  btnNewButton.setText("OK");
								 		btnNewButton.setEnabled(true);
						    	  }
						    	  
						    	  
						    	  link_showMetadata.setVisible(true);
						    //	  link_moveNotProcessed.setVisible(true);
						   
						      }
						    });
						    
							
						   return  new Status(IStatus.OK , "MATROS", (mmd != null ? mmd.getSha256() : "")); //$NON-NLS-1$ 
					 
					  }
					};
					
				
					// Show a busy indicator while the runnable is executed
	    	 		// BusyIndicator.showWhile(Display.getCurrent(), 
	    	 		// xxx
					
					job.setSystem(false);
					job.schedule();
					
			
	}



	private void createAttributeTableColumns(AutoResizeTableLayout layout) {
		
		_swtTableViewerAttributes.getTable().setLayout(layout);
		_swtTableViewerAttributes.getTable().setHeaderVisible(true);
		_swtTableViewerAttributes.getTable().setLinesVisible(true);

		TableViewerColumn colType = new TableViewerColumn(_swtTableViewerAttributes, SWT.NONE);

		colType.getColumn().setText("type");
		colType.getColumn().setMoveable(true);
		layout.addColumnData(new ColumnWeightData(30));
		

        
		colType.setLabelProvider(new ColumnLabelProvider() {
               
               @Override
               public String getText(Object element) {
              
            	   if (element instanceof AbstractInfoAttribute) {
                	   AbstractInfoAttribute casted = (AbstractInfoAttribute) element;
                	   return casted.getType().getName();
            	   }
            	   
            	   return "XXX";
            	   
               }
               
        });    
        
		
		TableViewerColumn colValue = new TableViewerColumn(_swtTableViewerAttributes, SWT.NONE);

		colValue.getColumn().setText("value");
		colValue.getColumn().setMoveable(true);
		layout.addColumnData(new ColumnWeightData(35));
		
   
        
        colValue.setLabelProvider(new ColumnLabelProvider() {
            
            @Override
            public String getText(Object element) {
           
         	   if (element instanceof AbstractInfoAttribute) {
            	   AbstractInfoAttribute casted = (AbstractInfoAttribute) element;
             	   return  casted.getTextDescription();
         	   }
         	   
         	   return "YYY";
         	   
            }

        });    
        
		
        
        /**
         * This method can return validator for your property value.
         * This implementation checks if the value is a valid integer number.
         */
	 ICellEditorValidator  validator = new ICellEditorValidator() {
	
		@Override
		public String isValid(Object arg0) {
			return null;
		}};

    	
      MultilineTextCellEditor  cellValueEditor = new MultilineTextCellEditor(_swtTableViewerAttributes.getTable(),validator);
      CheckboxCellEditor boolEdit = new CheckboxCellEditor(((Composite) colValue.getViewer().getControl()));
      TextCellEditor txtEdit = new TextCellEditor(((Composite) colValue.getViewer().getControl()));
      MatrosDateCellEditor dateEdit = new MatrosDateCellEditor(((Composite) colValue.getViewer().getControl()),SWT.NONE);
      
      MatrosNumberCellEditor numberEdit = new MatrosNumberCellEditor(((Composite) colValue.getViewer().getControl()),SWT.NONE);

      
      AttributeValidToDialogEditor attributeValidEditor = new AttributeValidToDialogEditor(((Composite) colValue.getViewer().getControl()) , SWT.NONE);;
      attributeValidEditor.setMessages(messages);
      
      _swtTableViewerAttributes.setCellEditors( new CellEditor[]{cellValueEditor, boolEdit, txtEdit, attributeValidEditor,dateEdit, numberEdit} );
      
      TableViewerEditor.create(_swtTableViewerAttributes, new ColumnViewerEditorActivationStrategy(_swtTableViewerAttributes) {   
    	  @Override
	      protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
	      	return event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION;
      }}, ColumnViewerEditor.DEFAULT ) ;


      colValue.setEditingSupport(new EditingSupport(_swtTableViewerAttributes) {

          @Override
          protected void setValue(Object element, Object value) {
//          	
             if (element instanceof AbstractInfoAttribute)
              {
              	AbstractInfoAttribute data = (AbstractInfoAttribute)element;
                  data.setValue( value);
              }
              _swtTableViewerAttributes.update(element, null);
          }

          @Override
          protected Object getValue(Object element) {
          	
          	 if (element instanceof AbstractInfoAttribute)
               {
               	AbstractInfoAttribute data = (AbstractInfoAttribute)element;
                   return data.getValue();
               }
          	 
          	 return null;
          }

          @Override
          protected CellEditor getCellEditor(Object element) {
        	  
        	  if (element instanceof InfoBooleanAttribute) {
        		  return boolEdit;
        	  } if (element instanceof InfoTextAttribute) {
        		  return txtEdit;
        	  } if (element instanceof InfoDateAttribute) {
        		  return dateEdit;
        	  }  if (element instanceof InfoNumberAttribute) {
        		  
        		  numberEdit.setAttribute((InfoNumberAttribute)element);
        		  
        		  return numberEdit;
        	  }
        	  
        	  
              return null;
          }

          @Override
          protected boolean canEdit(Object element) {
              return true;
          }
      });
        
      
// valid until 
      
      
  	
		TableViewerColumn colValidUntil = new TableViewerColumn(_swtTableViewerAttributes, SWT.NONE);

		colValidUntil.getColumn().setText("valid date");
		colValidUntil.getColumn().setMoveable(true);
		layout.addColumnData(new ColumnWeightData(35));
		
 
      
      colValidUntil.setLabelProvider(new ColumnLabelProvider() {
          
          @Override
          public String getText(Object element) {
       	   AbstractInfoAttribute casted = (AbstractInfoAttribute) element;
       	 
       	   return casted.getTimeInfomation();
       	   
          }
          
      });    
      

      colValidUntil.setEditingSupport(new EditingSupport(_swtTableViewerAttributes) {
    	  
    	  
    	 

          @Override
          protected void setValue(Object element, Object value) {
//          	
             if (element instanceof AbstractInfoAttribute)
              {
              	AbstractInfoAttribute data = (AbstractInfoAttribute)element;
   //               data.setValue( value);
              }
              _swtTableViewerAttributes.update(element, null);
          }

          @Override
          protected Object getValue(Object element) {
          	
          	 if (element instanceof AbstractInfoAttribute)
               {
               	AbstractInfoAttribute data = (AbstractInfoAttribute)element;
                   return data;
               }
          	 
          	 return null;
          }
          

          @Override
          protected CellEditor getCellEditor(Object element) {
        	  
        	  return attributeValidEditor;
          }

          @Override
          protected boolean canEdit(Object element) {
              return true;
          }
      });
        
		
	}
	
	
	

	
	

	// Drag and Drop Attributes
	
	@Inject
	@Optional
	private void subscribeTopicTOPIC__DRAG_ATTRIBUTE_STARTT(@UIEventTopic(MyEventConstants.TOPIC__DRAG_ATTRIBUTE_START) Map data) {

		// XXX dat if macht einen Sinn
		defaultAttributeColor = _swtGroupAttributes.getBackground();

		if (_swtGroupAttributes != null) {
			
			_swtGroupAttributes.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
			_swtGroupAttributes.redraw();
		}

	}
	
	
	@Inject
	@Optional
	private void subscribeTOPIC__DRAG_ATTRIBUTE_FINISHED(@UIEventTopic(MyEventConstants.TOPIC__DRAG_ATTRIBUTE_FINISHED) Map data) {

		
		// Alte Farbe wieder herstellen
		if (_swtGroupAttributes != null) {
			_swtGroupAttributes.setBackground(defaultListColor);
			_swtGroupAttributes.redraw();
		}

	}
	
	

	@Inject
	public void setSelection(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) String contact) {
		if (contact != null) {
			// tableViewer.add(contact);
		}
	}

	@Focus
	public void setFocus() {
		
		if (_partMetaIncludeContent.get_swtTxtItemname().getText() == null 
			|| _partMetaIncludeContent.get_swtTxtItemname().getText().length() == 0) {
			
			_partMetaIncludeContent.get_swtTxtItemname().forceFocus();
			
		}
		
		
	}

	@Persist
	public void save() {
		_dirty.setDirty(false);
	}




	class Stage {
		
		String key;
		int value;
		public String getDisplaytext() {
			return key;
		}
		public void setDisplaytext(String key) {
			this.key = key;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Stage ) {
				return ((Stage) obj).value == value;
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return value;
		}
	}
	
	// Should also work with functional interfaces !?


	
	class OpenNewJobber extends Jobber {

		@Override
		public void execute() throws Exception {
			
			// Clone else there will be file-lock
        	String path = desktopHelper.getInboxNonBlockingLink(_wrapper.getInboxFile());
        	desktopHelper.openUrl(path);
        	
        	
			
		}
		
		
		
	}
	
	
	class OpenExistingJobber extends Jobber {

		@Override
		public void execute() throws Exception {

			String local = desktopHelper.getLocallink(_wrapper.getInfoItem());

			desktopHelper.openUrl(local);

		}

	}
	
	
	class ShowMetadataJobber extends Jobber {

		@Override
		public void execute() throws Exception {

			if (_wrapper.getInfoItem().getMetadata() != null) {
				
		        IEclipseContext tempContext = EclipseContextFactory.create();
		        tempContext.set(Shell.class, shell);
		        tempContext.set(MatrosMetadata.class, _wrapper.getInfoItem().getMetadata());
				
				ItemMetadataDialog dialog = ContextInjectionFactory.make(ItemMetadataDialog.class, context, tempContext);
				dialog.open();
				
				
			} else {
	            MessageDialog.openInformation(Display.getDefault().getActiveShell(),"Metadata" , "No Metadata available");
			}
		}

	}
	
	
	
	
	
	

}
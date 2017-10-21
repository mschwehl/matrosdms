package net.schwehla.matrosdms.rcp.wizzard.setup.page;

import java.util.function.Consumer;

import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;

import net.schwehla.matrosdms.rcp.wizzard.model.setup.Masterdata;


public abstract class MatrosWizzardPage extends WizardPage implements Consumer <Boolean> {

	public MatrosWizzardPage(String pageName) {
		super(pageName);
	}
	
	
	
	 DataBindingContext m_bindingContext;
		
	
	// Singleton
	@Inject
	Masterdata masterData;
	
	boolean pageComplete = false;
	
	@Override
	public boolean isPageComplete() {
		return pageComplete;
	}
	
	
	@Override
	public boolean canFlipToNextPage() {
		return pageComplete;
	}


	@Override
	public void createControl(Composite parent) {
		buildControl(parent);
		setPageComplete(false);
		
		
		// required to avoid an error in the system ?? 
		//http://www.vogella.com/tutorials/EclipseWizards/article.html
    
		if (masterData != null) {
			m_bindingContext = initDataBindings();
		}
		
		
	    parent.addDisposeListener(new DisposeListener() {
	        public void widgetDisposed(DisposeEvent e) {
	        	m_bindingContext.dispose();
	        }
	    });
		
	}

	public abstract void buildControl(Composite parent);
	public abstract DataBindingContext initDataBindings();
	
	

	@Override
	public void accept(Boolean arg0) {
		
		if (Boolean.TRUE.equals(arg0)) {
			
			setPageComplete(true);
			pageComplete = true;
		} else {
			setPageComplete(false);
			pageComplete = false;
		}

		getContainer().updateButtons();
		
	}

	

}

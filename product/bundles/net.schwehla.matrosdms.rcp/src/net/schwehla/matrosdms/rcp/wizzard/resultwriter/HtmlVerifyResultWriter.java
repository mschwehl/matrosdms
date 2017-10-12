package net.schwehla.matrosdms.rcp.wizzard.resultwriter;

import java.io.File;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;

import net.schwehla.matrosdms.domain.util.VerifyItem;
import net.schwehla.matrosdms.persistenceservice.internal.MatrosConfigReader;
import net.schwehla.matrosdms.rcp.MyEventConstants;

@Creatable
public class HtmlVerifyResultWriter extends AbstractResultWriter {
	
	@Inject
	public HtmlVerifyResultWriter() {
	}
	
	
	@Inject
	@Optional
	private void subscribeTOPIC__VERIFY_ITEM_SUCESS(
			@UIEventTopic(MyEventConstants.TOPIC__VERIFYELEMENT_SUCESS) VerifyItem type) {
		
		
		
	}
	
	
	@Inject
	@Optional
	private void subscribeTOPIC__VERIFY_ITEM_ERROR(
			@UIEventTopic(MyEventConstants.TOPIC__VERIFYELEMENT_ERROR) VerifyItem type) {
		

		
	}


	public String getLocallink(String fileName) {
		
		
		
		MatrosConfigReader mcr = new MatrosConfigReader();
		try {
			
			String appdir = mcr.getApplicationCacheDir();
			return appdir + File.separator + fileName + "_" + System.nanoTime();
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
	}


}

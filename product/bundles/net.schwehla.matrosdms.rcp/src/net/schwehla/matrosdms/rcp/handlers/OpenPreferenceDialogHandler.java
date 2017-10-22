package net.schwehla.matrosdms.rcp.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.rcp.MatrosActivator;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;
import net.schwehla.matrosdms.rcp.preferences.ExportPreferencePage;
import net.schwehla.matrosdms.rcp.preferences.InboxPreferencePage;
import net.schwehla.matrosdms.rcp.preferences.NotProcessedFolderPreferencePage;
import net.schwehla.matrosdms.rcp.preferences.ProcessedFolderPreferencePage;



public class OpenPreferenceDialogHandler
{

//	 @Inject 
//	 GeneralPreferencePage generalTest;
	 

    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, IEclipseContext context)
    {
    	
    	

    	InboxPreferencePage ipp = ContextInjectionFactory.make(InboxPreferencePage.class, context);
    	ExportPreferencePage epp = ContextInjectionFactory.make(ExportPreferencePage.class, context);
    	ProcessedFolderPreferencePage ppp = ContextInjectionFactory.make(ProcessedFolderPreferencePage.class, context); 

    	NotProcessedFolderPreferencePage notppp = ContextInjectionFactory.make(NotProcessedFolderPreferencePage.class, context); 

    	        
        PreferenceManager pm = new PreferenceManager();
        
        // possible generic !? 
    //    pm.addToRoot(new PreferenceNode( MyGlobalConstants.Preferences.NODE_COM_MATROSDMS , new GeneralPreferencePage())); //$NON-NLS-1$
        
        pm.addToRoot(new PreferenceNode( MyGlobalConstants.Preferences.NODE_COM_MATROSDMS ,ipp)); //$NON-NLS-1$
        pm.addToRoot(new PreferenceNode( MyGlobalConstants.Preferences.NODE_COM_MATROSDMS , epp)); //$NON-NLS-1$
        pm.addToRoot(new PreferenceNode( MyGlobalConstants.Preferences.NODE_COM_MATROSDMS , ppp)); //$NON-NLS-1$
        pm.addToRoot(new PreferenceNode( MyGlobalConstants.Preferences.NODE_COM_MATROSDMS , notppp)); //$NON-NLS-1$
        
   //     pm.addToRoot(new PreferenceNode( MyGlobalConstants.Preferences.NODE_COM_MATROSDMS , generalTest)); //$NON-NLS-1$

        PreferenceDialog dialog = new PreferenceDialog(shell, pm);
        dialog.setPreferenceStore(MatrosActivator.getDefault().getPreferenceStore());
        dialog.create();
        dialog.getTreeViewer().setComparator(new ViewerComparator());
        dialog.getTreeViewer().expandAll();
        dialog.open();
    }
}

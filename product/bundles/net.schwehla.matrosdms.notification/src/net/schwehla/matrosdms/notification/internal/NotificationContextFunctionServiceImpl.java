package net.schwehla.matrosdms.notification.internal;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;

import net.schwehla.matrosdms.notification.INotificationService;

public class NotificationContextFunctionServiceImpl extends ContextFunction  {

    @Override
    public Object compute(IEclipseContext context, String contextKey) {
    	
    	
    	INotificationService service = ContextInjectionFactory.make( NotificationServiceImpl .class, context);

    	
            MApplication app = context.get(MApplication.class);
            IEclipseContext appCtx = app.getContext();
            appCtx.set(INotificationService.class, service);

            return service;
    }
    
}

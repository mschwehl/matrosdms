package net.schwehla.matrosdms.persistenceservice.internal;

import java.lang.reflect.Proxy;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.model.application.MApplication;

import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;

public class ServiceContextFunction extends ContextFunction {
	
    
        @Override
        public Object compute(IEclipseContext context, String contextKey) {
        	
        	
      	  // InvocationHandler erzeugen
          MatrosServerProxy handler = new MatrosServerProxy();

      	  handler.setServiceImpl( new MatrosServiceImpl());
  
      	  

      	
      	  // Array mit den Interfaces erzeugen,
      	  // die der Proxy haben soll
      	  Class[] listenerInterfaces = new Class[] { IMatrosServiceService.class };
      	  
      	  // Proxy for service-impl
      	  IMatrosServiceService proxy = (IMatrosServiceService) Proxy.newProxyInstance(IMatrosServiceService.class.getClassLoader(),
      	    listenerInterfaces, handler);
      	  
      	
      	  // Register
      	  
          MApplication app = context.get(MApplication.class);
          IEclipseContext appCtx = app.getContext();
          appCtx.set(IMatrosServiceService.class, (IMatrosServiceService) proxy);
          
          handler.setLogger(context.get(Logger.class));

      	  IEventBroker eventBroker = (IEventBroker) context.get(IEventBroker.class);
      	  handler.setEventBroker(eventBroker);
      	
      	  
          return proxy;
              
       
        }
}
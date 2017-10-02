package net.schwehla.matrosdms.lucene.internal;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;

import net.schwehla.matrosdms.lucene.ILuceneService;

public class ServiceContextFunction extends ContextFunction {
	
    
        @Override
        public Object compute(IEclipseContext context, String contextKey) {
        	
        	
        	ILuceneService indexService =
                                ContextInjectionFactory.make(LuceneServiceProxy.class, context);

        	
                MApplication app = context.get(MApplication.class);
                IEclipseContext appCtx = app.getContext();
                appCtx.set(ILuceneService.class, indexService);

                return indexService;
        }
}
/*******************************************************************************
 * Copyright (c) 2010, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package net.schwehla.matrosdms.progress.internal;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;

import net.schwehla.matrosdms.progress.IProgressService;

public class ProgressServiceCreationFunction extends ContextFunction {

	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		
    	
		IProgressService service = ContextInjectionFactory.make( ProgressServiceImpl .class, context);

    	
            MApplication app = context.get(MApplication.class);
            IEclipseContext appCtx = app.getContext();
            appCtx.set(IProgressService.class, service);

            return service;
            
  
	}
}

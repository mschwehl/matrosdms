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

package net.schwehla.matrosdms.progress;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;

import net.schwehla.matrosdms.progress.internal.PreferenceStore;
import net.schwehla.matrosdms.progress.internal.Preferences;
import net.schwehla.matrosdms.progress.internal.ProgressManager;
import net.schwehla.matrosdms.progress.internal.ProgressViewUpdater;
import net.schwehla.matrosdms.progress.internal.Services;

public class ProgressViewAddon {

	@PostConstruct
	public void init(MApplication application, IEclipseContext context) {
		IEclipseContext appContext = application.getContext();
		appContext.set(Preferences.class, ContextInjectionFactory.make(Preferences.class, appContext));
		appContext.set(PreferenceStore.class, ContextInjectionFactory.make(PreferenceStore.class, appContext));
		ContextInjectionFactory.make(Services.class, context);
		ProgressManager progressManager = ContextInjectionFactory.make(ProgressManager.class, context);
		appContext.set(ProgressManager.class, progressManager);
		// bug-fix: add a ProgressViewUpdater to the application context
		appContext.set(ProgressViewUpdater.class, ContextInjectionFactory.make(ProgressViewUpdater.class, appContext));
	}
}
